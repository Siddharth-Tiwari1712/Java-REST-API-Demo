# JWT and API Documentation Guide

## 1. What is JWT?
JWT stands for JSON Web Token.
It is a compact, URL-safe way to securely transmit information between parties as a JSON object.

A JWT has three parts:
- Header: metadata about the token, usually algorithm and type
- Payload: claims or data such as user ID, roles, expiration
- Signature: verifies the token was issued by a trusted source and not changed

Example JWT format:
```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsImV4cCI6MTcwMDUwMzYwMH0.abc123...
```

## 2. Why use JWT in a REST API?
### When JWT is useful
- Stateless authentication: the server does not keep session state
- Mobile or single-page applications need compact tokens
- APIs want a standard way to authorize requests
- Tokens can contain user identity and roles

### Benefits
- Scalable across multiple instances because authentication is in the token
- Easy to pass in HTTP headers: `Authorization: Bearer <token>`
- Works across domains and clients

### When not to use JWT yet
- If the project is only a small demo
- If you do not need protected endpoints
- If security is not yet required
- If you want to keep the API simpler for learning

## 3. What was added for JWT in this project?
### Dependencies
Added Spring Security and JWT libraries to `pom.xml`:
- `spring-boot-starter-security`
- `jjwt-api`, `jjwt-impl`, `jjwt-jackson`

### Security configuration
Implemented a Spring Security configuration class:
- `SecurityConfig.java`
- Configures stateless JWT authentication
- Allows public access to `/api/auth/**` and the H2 console
- Protects all other endpoints such as `/api/employees/**`
- Inserts a JWT filter before Spring Security's authentication filter

### Login endpoint
Added a login endpoint:
- `POST /api/auth/login`

Request example:
```json
{
  "username": "admin",
  "password": "admin123"
}
```

Response example:
```json
{
  "token": "eyJhbGciOiJI...",
  "type": "Bearer",
  "expiresIn": 3600000,
  "username": "admin"
}
```

### JWT generation and validation
Implemented token creation and validation in `JwtTokenProvider.java`:
- Uses a secret from `application.properties`
- Adds expiration time
- Signs tokens with HS256
- Validates incoming tokens on each request

### Authentication filter
Added `JwtAuthenticationFilter.java`:
- Reads the `Authorization` header
- Verifies the JWT token
- Loads the user details and sets the Spring Security context

### Configuration values
Configured in `application.properties`:
- `jwt.secret`
- `jwt.expiration-ms`
- `app.security.username`
- `app.security.password`

### Example JWT flow now implemented
1. Client sends `POST /api/auth/login` with user credentials.
2. Server authenticates the user using in-memory user details.
3. Server returns a signed JWT.
4. Client sends `Authorization: Bearer <token>` on protected requests.
5. JWT filter validates the token and authorizes the request.

## 4. Why JWT was added now
JWT was added because your API core was stable enough to support security.

### Why this was a good next step
- The project already had DTOs, validation, and exception handling.
- Adding JWT now makes your API realistic and ready for protected endpoints.
- It is still a manageable scope: one auth endpoint, one security configuration, one filter, and a token provider.

### Why hold off until now?
- Your current API improvements are more important first: DTOs, validation, exception handling, and logging.
- JWT adds complexity around authentication and security configuration.
- It is best to implement it after the core API behavior is stable.

## 5. How to document your API
Documentation is important so other developers can understand and use your API.

### Option 1: Manual Markdown documentation
Create files like `REST_API_FLOW.md` and `ADDITIONAL_IMPORTANT_CONCEPTS.md` with:
- endpoint descriptions
- request/response examples
- error codes
- sample requests and responses

### Option 2: Automatic API documentation with Springdoc OpenAPI
Add dependency:
```xml
<dependency>
  <groupId>org.springdoc</groupId>
  <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
  <version>2.1.0</version>
</dependency>
```

Then Springdoc generates documentation automatically at:
- `/swagger-ui.html`
- `/swagger-ui/index.html`
- `/v3/api-docs`

### What to document
- Endpoints and HTTP methods
- Request body structures
- Response body structures
- Path variables and query parameters
- HTTP status codes
- Error responses

## 6. Recommended next steps for your project
1. Keep the current improvements: DTOs, validation, exception handling, and logging.
2. Add OpenAPI/Swagger for automatic documentation.
3. If you want security, add JWT next.
4. Document new endpoints and any protected routes.

## 7. What would JWT look like in this repo
If implemented, the repo would gain:
- `SecurityConfig.java`
- `JwtTokenProvider.java`
- `JwtAuthenticationFilter.java`
- `AuthController.java`
- `User` entity or in-memory user storage
- updated `pom.xml` with Spring Security and JWT dependencies

## 8. Summary
- JWT is a good real-world security feature.
- It is feasible for your project but should be added after the core API features.
- Documentation can be created manually in Markdown and automated with Springdoc OpenAPI.
- I have not yet added JWT to the project; I recommended it as the next major enhancement.
