from http.server import BaseHTTPRequestHandler, HTTPServer
import json
import time
from urllib.parse import parse_qs, urlparse


HOST = "localhost"
PORT = 8080
ALLOWED_ORIGINS = {"http://localhost:5500", "http://127.0.0.1:5500", "null"}

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
    "customer@shopease.com": {"password": "password123", "role": "USER", "fullName": "John Customer"},
    "admin@shopease.com": {"password": "admin123", "role": "ADMIN", "fullName": "Admin User"},
})


class Handler(BaseHTTPRequestHandler):
    def end_headers(self):
        origin = self.headers.get("Origin")
        self.send_header("Access-Control-Allow-Origin", origin if origin in ALLOWED_ORIGINS else "http://localhost:5500")
        self.send_header("Access-Control-Allow-Credentials", "true")
        self.send_header("Access-Control-Allow-Headers", "Authorization, Content-Type, X-CSRF-TOKEN")
        self.send_header("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
        super().end_headers()

    def do_OPTIONS(self):
        self.send_response(204)
        self.end_headers()

    def do_GET(self):
        path = urlparse(self.path).path
        if path == "/api/products":
            return self.json(200, products)
        if path == "/api/products/discounted":
            discounted = [{**item, "originalPrice": round(item["price"] * 1.25, 2)} for item in products]
            return self.json(200, discounted)
        if path == "/api/products/search":
            term = parse_qs(urlparse(self.path).query).get("term", [""])[0].lower()
            matches = [item for item in products if term in item["name"].lower() or term in item.get("description", "").lower()]
            return self.json(200, matches)
        if path.startswith("/api/products/category/"):
            category = path.rsplit("/", 1)[-1].lower()
            matches = [item for item in products if str(item.get("category", "")).lower().rstrip("s") == category.rstrip("s")]
            return self.json(200, matches)
        if path.startswith("/api/products/"):
            try:
                product_id = int(path.rsplit("/", 1)[-1])
            except ValueError:
                return self.json(404, {"message": "Not found"})
            product = next((item for item in products if item["id"] == product_id), None)
            return self.json(200, product) if product else self.json(404, {"message": "Product not found"})
        if path == "/api/auth/me":
            username = self.current_user()
            user = users.get(username, {})
            if not username:
                return self.json(401, {"message": "Not authenticated"})
            return self.json(200, {"email": username, "fullName": user.get("fullName", username), "role": user.get("role")})
        if path == "/api/auth/csrf":
            token = self.csrf_token()
            return self.json(200, {"token": token, "parameterName": "_csrf", "headerName": "X-CSRF-TOKEN"})
        if path == "/login":
            token = self.csrf_token()
            self.send_response(200)
            self.send_header("Content-Type", "text/html")
            self.end_headers()
            self.wfile.write(f'<input type="hidden" name="_csrf" value="{token}">'.encode())
            return
        if path == "/api/orders":
            if not self.current_user():
                return self.json(401, {"message": "Authentication required. Please log in first."})
            return self.json(200, orders)
        if path == "/":
            return self.json(200, {"message": "ShopEase mock API is running", "frontend": sorted(ALLOWED_ORIGINS)})
        self.json(404, {"message": "Not found"})

    def do_POST(self):
        path = urlparse(self.path).path
        body = self.body()

        if path == "/api/auth/register":
            username = str(body.get("email", "")).strip()
            password = str(body.get("password", ""))
            full_name = str(body.get("fullName", username)).strip()
            errors = []
            if "@" not in username:
                errors.append("Field 'email' must be a valid email")
            if len(password) < 8:
                errors.append("Field 'password' must be at least 8 characters")
            if errors:
                return self.json(400, {"timestamp": self.timestamp(), "errors": errors})
            users[username] = {"password": password, "role": "USER", "fullName": full_name}
            token = self.create_session(username)
            return self.json(201, {"message": "Account created successfully", "email": username, "fullName": full_name, "token": token})

        if path in ("/api/auth/login", "/login"):
            username = str(body.get("username", "")).strip()
            password = str(body.get("password", ""))
            if users.get(username, {}).get("password") != password:
                return self.json(401, {"message": "Invalid username or password"})
            session_id = self.create_session(username)
            self.send_response(200)
            self.send_header("Content-Type", "application/json")
            self.send_header("Set-Cookie", f"JSESSIONID={session_id}; Path=/; HttpOnly; SameSite=Lax")
            self.end_headers()
            self.wfile.write(json.dumps({
                "message": "Login successful",
                "email": username,
                "fullName": users[username].get("fullName", username),
                "role": users[username]["role"],
                "token": session_id
            }).encode())
            return

        if path in ("/api/auth/logout", "/logout"):
            session_id = self.session_id()
            if session_id:
                sessions.pop(session_id, None)
            self.send_response(200)
            self.send_header("Content-Type", "application/json")
            self.send_header("Set-Cookie", "JSESSIONID=; Path=/; Max-Age=0; HttpOnly; SameSite=Lax")
            self.end_headers()
            self.wfile.write(json.dumps({"message": "Signed out."}).encode())
            return

        if path == "/api/products":
            if not self.current_user():
                return self.json(401, {"message": "Authentication required. Please log in first."})
            if users.get(self.current_user(), {}).get("role") != "ADMIN":
                return self.json(403, {"message": "ADMIN role is required for product changes"})
            errors = self.validate_product(body)
            if errors:
                return self.json(400, {"timestamp": self.timestamp(), "errors": errors})
            price = float(body.get("price") or 0)
            category = body.get("category", "")
            if isinstance(category, dict):
                category = category.get("name", "")
            product = {**body, "id": len(products) + 1, "price": price, "category": category, "stockQuantity": body.get("stock", 0)}
            products.append(product)
            return self.json(201, product)

        if path == "/api/orders":
            if not self.current_user():
                return self.json(401, {"message": "Authentication required. Please log in first."})
            errors = self.validate_order(body)
            if errors:
                return self.json(400, {"timestamp": self.timestamp(), "errors": errors})
            order = {**body, "id": f"ORD-{len(orders) + 1}", "customerEmail": self.current_user()}
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
        auth = self.headers.get("Authorization", "")
        if auth.startswith("Bearer "):
            return auth.removeprefix("Bearer ").strip()
        cookie = self.headers.get("Cookie", "")
        for part in cookie.split(";"):
            name, _, value = part.strip().partition("=")
            if name == "JSESSIONID":
                return value
        return None

    def create_session(self, username):
        session_id = f"S{int(time.time() * 1000)}"
        sessions[session_id] = username
        return session_id

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
            if int(body.get("stockQuantity") or body.get("stock") or 0) <= 0:
                errors.append("Field 'stock' must be positive")
        except (TypeError, ValueError):
            errors.append("Field 'stock' must be positive")
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
