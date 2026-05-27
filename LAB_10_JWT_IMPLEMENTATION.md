# JWT Authentication Implementation - Lab 10 Complete Guide

## Overview
This document describes the complete implementation of JWT (JSON Web Token) authentication in the ShopEase backend Spring Boot application, as per Lab 10 requirements.

## What Has Been Implemented

### 1. **Dependencies Added** (`pom.xml`)
- `spring-boot-starter-security`: Spring Security framework for authentication/authorization
- `jjwt-api (0.11.5)`: JWT token generation and parsing API
- `jjwt-impl (0.11.5)`: Implementation runtime dependency
- `jjwt-jackson (0.11.5)`: Jackson support for JWT

### 2. **Configuration Files Updated**

#### `application.yaml`
Added JWT configuration properties:
```yaml
jwt:
  secret: "your-very-strong-secret-key-that-is-at-least-32-characters-long"
  expiration: 86400000  # 24 hours in milliseconds
```

### 3. **Core JWT Classes Created**

#### a) **JwtUtil** (`src/main/java/com/shopease/util/JwtUtil.java`)
Utility service for token management with methods:
- `generateToken(UserDetails)`: Creates JWT tokens
- `extractUsername(String token)`: Extracts username from token
- `isTokenValid(String token, UserDetails)`: Validates token
- `extractClaim()`: Extracts specific claims
- `extractAllClaims()`: Parses all token claims
- Uses HS256 (HMAC SHA-256) for signing

#### b) **JwtAuthenticationFilter** (`src/main/java/com/shopease/security/JwtAuthenticationFilter.java`)
Filter that:
- Intercepts all HTTP requests
- Extracts JWT from Authorization header (Bearer token)
- Validates token signature and expiration
- Sets user authentication in Spring Security context
- Runs before standard authentication filter

#### c) **CustomUserDetailsService** (`src/main/java/com/shopease/security/CustomUserDetailsService.java`)
UserDetailsService implementation that:
- Implements Spring's UserDetailsService interface
- Loads user details by username
- Currently uses in-memory storage (can be replaced with database)
- Includes test users:
  - `user@example.com` / `password123` (USER role)
  - `admin@example.com` / `admin123` (ADMIN role)

#### d) **SecurityConfig** (`src/main/java/com/shopease/config/SecurityConfig.java`)
Spring Security configuration that:
- Disables CSRF (not needed for stateless JWT APIs)
- Sets session management to STATELESS
- Defines authorization rules:
  - `/api/auth/**` and `/api/public/**`: Public access
  - All other endpoints: Require authentication
- Registers JWT filter before UsernamePasswordAuthenticationFilter
- Configures exception handling for unauthorized requests
- Provides beans for:
  - `AuthenticationManager`: For authentication processing
  - `AuthenticationProvider`: DaoAuthenticationProvider with UserDetailsService
  - `PasswordEncoder`: BCryptPasswordEncoder for secure password hashing

#### e) **AuthController** (`src/main/java/com/shopease/controller/AuthController.java`)
REST endpoints for authentication:
- `POST /api/auth/login`: Authenticate and get JWT token
- `POST /api/auth/register`: Register new user and get JWT token

Returns:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "user@example.com",
  "roles": ["ROLE_USER"]
}
```

### 4. **CORS Configuration** (Already in `WebConfig.java`)
- Allows requests from multiple origins
- Includes "Authorization" header in allowed headers
- Supports credentials for token-based authentication

## Authentication Flow

### Step 1: Login Request
```
POST /api/auth/login
Content-Type: application/json

{
  "username": "user@example.com",
  "password": "password123"
}
```

### Step 2: Server Validation
- AuthenticationManager validates credentials
- Checks against UserDetailsService (in-memory or database)
- Verifies password using BCryptPasswordEncoder

### Step 3: Token Generation
- JwtUtil creates JWT with:
  - Subject (username)
  - Issue time (iat)
  - Expiration time (exp)
  - Signature (HS256 with secret key)

### Step 4: Return Token
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjEiLCJlbWFpbCI6InVzZXJAZXhhbXBsZS5jb20iLCJpYXQiOjE2MjMwNDI4MDB9.xxxxx",
  "username": "user@example.com",
  "roles": ["ROLE_USER"]
}
```

### Step 5: Subsequent Requests
Client sends token in Authorization header:
```
GET /api/products
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### Step 6: Server Validation
- JwtAuthenticationFilter extracts token
- Verifies signature using secret key
- Checks expiration time
- Extracts username and loads user details
- Sets authentication in SecurityContext
- Request is processed

## Frontend Integration (JavaScript/Fetch API)

### Login and Store Token
```javascript
async function login(username, password) {
  const response = await fetch('http://localhost:8080/api/auth/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username, password })
  });
  
  const data = await response.json();
  localStorage.setItem('jwt_token', data.token);
  return data.token;
}
```

### Make Authenticated Request
```javascript
async function fetchProtectedData(endpoint) {
  const token = localStorage.getItem('jwt_token');
  
  const response = await fetch(`http://localhost:8080${endpoint}`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    }
  });
  
  if (response.status === 401) {
    // Token expired, redirect to login
    localStorage.removeItem('jwt_token');
    window.location.href = '/login';
    return;
  }
  
  return await response.json();
}
```

## Testing

### Test Users (In-Memory)
```
Username: user@example.com
Password: password123
Role: USER

Username: admin@example.com
Password: admin123
Role: ADMIN
```

### Using jwt-auth-example.html
1. Open `jwt-auth-example.html` in browser
2. Use test credentials to login
3. Copy the generated JWT token
4. Decode at [jwt.io](https://jwt.io)
   - Secret: `ws101`
5. Fetch protected resources (products, categories)

### Using cURL
```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user@example.com","password":"password123"}'

# Fetch protected resource
curl -X GET http://localhost:8080/api/products \
  -H "Authorization: Bearer <JWT_TOKEN_HERE>"
```

## Token Structure

### JWT Format: `header.payload.signature`

#### Example Decoded:
```
Header: 
{
  "alg": "HS256",
  "typ": "JWT"
}

Payload:
{
  "sub": "user@example.com",
  "iat": 1623042800,
  "exp": 1623129200
}

Signature: HMACSHA256(header.payload, secret)
```

## Security Considerations

### ✅ What's Implemented
- **Stateless Authentication**: No server-side sessions needed
- **Secure Signature**: HS256 with strong secret key
- **Expiration**: Tokens expire after 24 hours
- **Password Hashing**: BCrypt for secure password storage
- **CORS**: Properly configured for frontend requests
- **Authorization Header**: Standard Bearer token format

### ⚠️ Additional Recommendations for Production
1. **Token Storage**: Use HttpOnly cookies instead of localStorage (prevents XSS)
2. **Refresh Tokens**: Implement separate long-lived refresh tokens
3. **Token Revocation**: Maintain token blacklist for logout
4. **HTTPS Only**: Always use HTTPS in production
5. **Secret Key Management**: Use environment variables, never hardcode
6. **Rate Limiting**: Limit login attempts to prevent brute force
7. **Database Integration**: Replace in-memory CustomUserDetailsService with database queries

## Troubleshooting

### Issue: "Unauthorized" on protected endpoints
- **Solution**: Verify token is in Authorization header as `Bearer <token>`
- Check token expiration using jwt.io

### Issue: "Invalid signature"
- **Solution**: Verify the secret key in application.yaml
- Ensure jwt.secret matches the signing key in JwtUtil

### Issue: CORS errors from frontend
- **Solution**: Check WebConfig allows your frontend origin
- Verify Authorization header is in allowedHeaders

### Issue: User not found
- **Solution**: Use correct test credentials
- For database: Ensure user exists in database

## File Structure
```
shopease-backend/
├── pom.xml (Updated with JWT dependencies)
├── src/main/java/com/shopease/
│   ├── util/
│   │   └── JwtUtil.java (NEW)
│   ├── security/
│   │   ├── JwtAuthenticationFilter.java (NEW)
│   │   └── CustomUserDetailsService.java (NEW)
│   ├── config/
│   │   ├── SecurityConfig.java (NEW)
│   │   └── WebConfig.java (Updated)
│   └── controller/
│       └── AuthController.java (NEW)
├── src/main/resources/
│   └── application.yaml (Updated with JWT config)
└── E-commerce Interface Lab/
    └── jwt-auth-example.html (NEW - Frontend example)
```

## Next Steps

1. **Update CustomUserDetailsService**:
   - Replace in-memory users with database queries
   - Query User entity from database instead

2. **Protect Endpoints**:
   - Add `@PreAuthorize("hasRole('USER')")` annotations to controller methods
   - Control access based on user roles

3. **Implement Refresh Tokens**:
   - Create separate refresh token endpoint
   - Allow token refresh without re-login

4. **Production Deployment**:
   - Update jwt.secret to strong random string
   - Move secrets to environment variables
   - Enable HTTPS
   - Configure production database

## References
- [Spring Security Documentation](https://spring.io/projects/spring-security)
- [JWT.io](https://jwt.io) - JWT Debugger and Documentation
- [JJWT Library](https://github.com/jwtk/jjwt) - Java JWT Implementation
- [RFC 7519](https://tools.ietf.org/html/rfc7519) - JWT Specification
