from http.server import BaseHTTPRequestHandler, HTTPServer
import json
import time
from urllib.parse import parse_qs, urlparse


HOST = "localhost"
PORT = 8080
ALLOWED_ORIGINS = {"http://localhost:5500", "http://127.0.0.1:5500"}

users = {}
sessions = {}
csrf_tokens = {}
products = [
    {
        "id": 1,
        "name": "Gold Bracelet",
        "description": "Elegant 14k gold bracelet with intricate design",
        "price": 299.99,
        "category": "Bracelets",
        "stockQuantity": 15,
        "imageUrl": "https://via.placeholder.com/200?text=Gold+Bracelet",
    },
    {
        "id": 2,
        "name": "Silver Necklace",
        "description": "Classic sterling silver chain necklace",
        "price": 149.99,
        "category": "Necklaces",
        "stockQuantity": 25,
        "imageUrl": "https://via.placeholder.com/200?text=Silver+Necklace",
    },
]
orders = []
users.update({
    "customer": {"password": "customer123", "role": "CUSTOMER"},
    "admin": {"password": "admin123", "role": "ADMIN"},
})


class Handler(BaseHTTPRequestHandler):
    def end_headers(self):
        origin = self.headers.get("Origin")
        self.send_header("Access-Control-Allow-Origin", origin if origin in ALLOWED_ORIGINS else "http://localhost:5500")
        self.send_header("Access-Control-Allow-Credentials", "true")
        self.send_header("Access-Control-Allow-Headers", "Content-Type, X-CSRF-TOKEN")
        self.send_header("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
        super().end_headers()

    def do_OPTIONS(self):
        self.send_response(204)
        self.end_headers()

    def do_GET(self):
        path = urlparse(self.path).path
        if path == "/api/v1/products":
            return self.json(200, products)
        if path == "/api/v1/products/discounted":
            discounted = [{**item, "originalPrice": round(item["price"] * 1.25, 2)} for item in products]
            return self.json(200, discounted)
        if path == "/api/v1/auth/me":
            username = self.current_user()
            role = users.get(username, {}).get("role") if username else None
            return self.json(200, {"authenticated": bool(username), "username": username, "role": role})
        if path == "/api/v1/auth/csrf":
            token = self.csrf_token()
            return self.json(200, {"token": token, "parameterName": "_csrf", "headerName": "X-CSRF-TOKEN"})
        if path == "/login":
            token = self.csrf_token()
            self.send_response(200)
            self.send_header("Content-Type", "text/html")
            self.end_headers()
            self.wfile.write(f'<input type="hidden" name="_csrf" value="{token}">'.encode())
            return
        if path == "/api/v1/orders":
            if not self.current_user():
                return self.json(401, {"message": "Authentication required. Please log in first."})
            return self.json(200, orders)
        if path == "/":
            return self.json(200, {"message": "ShopEase mock API is running", "frontend": sorted(ALLOWED_ORIGINS)})
        self.json(404, {"message": "Not found"})

    def do_POST(self):
        path = urlparse(self.path).path
        body = self.body()

        if path == "/api/v1/auth/register":
            username = str(body.get("username", "")).strip()
            password = str(body.get("password", ""))
            role = str(body.get("role", "CUSTOMER")).strip().upper()
            errors = []
            if len(username) < 8 or len(username) > 20:
                errors.append("Field 'username' must be 8 to 20 characters")
            if len(password) < 8:
                errors.append("Field 'password' must be at least 8 characters")
            if role not in ("CUSTOMER", "ADMIN"):
                errors.append("Field 'role' must be CUSTOMER or ADMIN")
            if errors:
                return self.json(400, {"timestamp": self.timestamp(), "errors": errors})
            users[username] = {"password": password, "role": role}
            return self.json(200, {"message": "Account created successfully", "username": username, "role": role})

        if path in ("/api/v1/auth/login", "/login"):
            username = str(body.get("username", "")).strip()
            password = str(body.get("password", ""))
            if not self.valid_csrf(body):
                return self.json(403, {"message": "Invalid or missing CSRF token"})
            if users.get(username, {}).get("password") != password:
                return self.json(401, {"message": "Invalid username or password"})
            session_id = f"S{int(time.time() * 1000)}"
            sessions[session_id] = username
            self.send_response(200)
            self.send_header("Content-Type", "application/json")
            self.send_header("Set-Cookie", f"JSESSIONID={session_id}; Path=/; HttpOnly; SameSite=Lax")
            self.end_headers()
            self.wfile.write(json.dumps({"message": "Login successful", "username": username, "role": users[username]["role"]}).encode())
            return

        if path in ("/api/v1/auth/logout", "/logout"):
            if not self.valid_csrf(body):
                return self.json(403, {"message": "Invalid or missing CSRF token"})
            session_id = self.session_id()
            if session_id:
                sessions.pop(session_id, None)
            self.send_response(200)
            self.send_header("Content-Type", "application/json")
            self.send_header("Set-Cookie", "JSESSIONID=; Path=/; Max-Age=0; HttpOnly; SameSite=Lax")
            self.end_headers()
            self.wfile.write(json.dumps({"message": "Signed out."}).encode())
            return

        if path == "/api/v1/products":
            if not self.current_user():
                return self.json(401, {"message": "Authentication required. Please log in first."})
            if users.get(self.current_user(), {}).get("role") != "ADMIN":
                return self.json(403, {"message": "ADMIN role is required for product changes"})
            if not self.valid_csrf(body):
                return self.json(403, {"message": "Invalid or missing CSRF token"})
            errors = self.validate_product(body)
            if errors:
                return self.json(400, {"timestamp": self.timestamp(), "errors": errors})
            price = float(body.get("price") or 0)
            product = {**body, "id": len(products) + 1, "price": price}
            products.append(product)
            return self.json(201, product)

        if path == "/api/v1/orders":
            if not self.current_user():
                return self.json(401, {"message": "Authentication required. Please log in first."})
            if not self.valid_csrf(body):
                return self.json(403, {"message": "Invalid or missing CSRF token"})
            errors = self.validate_order(body)
            if errors:
                return self.json(400, {"timestamp": self.timestamp(), "errors": errors})
            order = {**body, "id": f"ORD-{len(orders) + 1}", "username": self.current_user()}
            orders.insert(0, order)
            return self.json(201, order)

        self.json(404, {"message": "Not found"})

    def body(self):
        length = int(self.headers.get("Content-Length") or 0)
        if length == 0:
            return {}
        raw = self.rfile.read(length).decode()
        content_type = self.headers.get("Content-Type", "")
        if "application/x-www-form-urlencoded" in content_type:
            return {key: values[0] for key, values in parse_qs(raw).items()}
        return json.loads(raw or "{}")

    def session_id(self):
        cookie = self.headers.get("Cookie", "")
        for part in cookie.split(";"):
            name, _, value = part.strip().partition("=")
            if name == "JSESSIONID":
                return value
        return None

    def current_user(self):
        return sessions.get(self.session_id())

    def csrf_token(self):
        session_id = self.session_id() or "anonymous"
        token = csrf_tokens.get(session_id)
        if not token:
            token = f"CSRF-{int(time.time() * 1000)}"
            csrf_tokens[session_id] = token
        return token

    def valid_csrf(self, body):
        token = self.headers.get("X-CSRF-TOKEN") or body.get("_csrf")
        return bool(token and token in csrf_tokens.values())

    def validate_product(self, body):
        errors = []
        if not str(body.get("name", "")).strip():
            errors.append("Field 'name' must not be blank")
        if not str(body.get("description", "")).strip():
            errors.append("Field 'description' must not be blank")
        try:
            if float(body.get("price") or 0) <= 0:
                errors.append("Field 'price' must be a positive number")
        except (TypeError, ValueError):
            errors.append("Field 'price' must be a positive number")
        try:
            if int(body.get("stockQuantity") or 0) <= 0:
                errors.append("Field 'stockQuantity' must be positive")
        except (TypeError, ValueError):
            errors.append("Field 'stockQuantity' must be positive")
        return errors

    def validate_order(self, body):
        errors = []
        if "@" not in str(body.get("customerEmail", "")):
            errors.append("Field 'customerEmail' must be a valid email address")
        if not str(body.get("deliveryAddress", "")).strip():
            errors.append("Field 'deliveryAddress' must not be blank")
        try:
            if float(body.get("totalAmount") or 0) <= 0:
                errors.append("Field 'totalAmount' must be positive")
        except (TypeError, ValueError):
            errors.append("Field 'totalAmount' must be positive")
        if not body.get("orderItems"):
            errors.append("Field 'orderItems' must contain at least one item")
        return errors

    def timestamp(self):
        return time.strftime("%Y-%m-%dT%H:%M:%SZ", time.gmtime())

    def json(self, status, payload):
        self.send_response(status)
        self.send_header("Content-Type", "application/json")
        self.end_headers()
        self.wfile.write(json.dumps(payload).encode())

    def log_message(self, fmt, *args):
        print("%s - %s" % (self.address_string(), fmt % args))


if __name__ == "__main__":
    print(f"ShopEase mock API running at http://{HOST}:{PORT}")
    HTTPServer((HOST, PORT), Handler).serve_forever()
