# Notes Backend API

Base URL: http://localhost:8080

- Swagger UI: /swagger-ui.html
- OpenAPI JSON: /api-docs

Authentication:
1. Register: POST /api/auth/register
   Body: { "username": "jdoe", "email": "jdoe@example.com", "password": "StrongPass123" }
   Response: { "token": "<JWT>", "username": "jdoe" }

2. Login: POST /api/auth/login
   Body: { "username": "jdoe", "password": "StrongPass123" }
   Response: { "token": "<JWT>", "username": "jdoe" }

Use the token:
Authorization: Bearer <JWT>

Notes:
- Create: POST /api/notes  { "title": "My Note", "content": "..." }
- List: GET /api/notes?page=0&size=10&sortBy=updatedAt&direction=DESC
- Get: GET /api/notes/{id}
- Update: PUT /api/notes/{id}  { "title": "New", "content": "..." }
- Delete: DELETE /api/notes/{id}
