# Sistema de Autenticación y Autorización

Este proyecto implementa un sistema completo de autenticación y autorización de usuarios utilizando tecnologías como Java, Spring Boot, Spring Security y JWT. El sistema permite gestionar usuarios con diferentes roles y permisos, asegurando que solo los usuarios autorizados puedan acceder a recursos específicos de la aplicación.

## Características

- **Registro y Autenticación de Usuarios:** Permite a los usuarios registrarse y autenticarse utilizando credenciales seguras.
- **Roles y Permisos:** Soporta múltiples roles (por ejemplo, ADMIN, USER, EMPLOYEE) y permisos granulares para controlar el acceso a diferentes partes de la aplicación.
- **JWT (JSON Web Tokens):** Utiliza tokens JWT para manejar la autenticación sin estado, mejorando la escalabilidad y seguridad.
- **Integración con PostgreSQL:** Almacenamiento robusto de datos utilizando PostgreSQL y Hibernate como ORM.

## Estructura del Proyecto

```bash
    com
    └── authapp
        ├── AuthAppApplication.java
        ├── config
        │   ├── CustomAccessDeniedHandler.java
        │   ├── filter
        │   │   └── JwtTokenValidatorFilter.java
        │   └── SecurityConfig.java
        ├── jwt
        │   └── JwtUtils.java
        ├── persistance
        │   ├── entity
        │   │   ├── BaseEntity.java
        │   │   ├── enums
        │   │   │   ├── PermissionEnum.java
        │   │   │   └── RoleEnum.java
        │   │   ├── PermissionEntity.java
        │   │   ├── RoleEntity.java
        │   │   └── UserEntity.java
        │   └── repository
        │       ├── RoleRepository.java
        │       └── UserRepository.java
        ├── presentation
        │   ├── advice
        │   │   └── GlobalExceptionsHandlers.java
        │   ├── controller
        │   │   └── AuthController.java
        │   └── dto
        │       ├── AuthCreateUserDTO.java
        │       ├── AuthRoleRequestDTO.java
        │       ├── AuthUserLoginDTO.java
        │       ├── AuthUserProfileResponseDTO.java
        │       └── AuthUserResponseDTO.java
        └── service
            └── UserDetailsServiceImpl.java
```

## Uso

1. Registro de usuario

    ```bash
    [POST] /api/auth/sign-up
    ```
    ```json
    {
        "fullNames": "Juan C.",
        "email": "juan@mail.com",
        "password": "1234",
        "roleRequest": {
            "roles": ["USER",...] // ADMIN, USER, EMPLOYEE 
        }
    }
    ```
    Respuesta
    ```json
    {
        "email": "juan@mail.com",
        "message": "User created successfully",
        "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
        "status": true
    }
    ```

    
2. Autenticación, Login

    ```bash
    [POST] /api/auth/login
    ```
    ```json
    {
        "email": "juan@mail.com",
        "password": "1234"
    }
    ```
    Respuesta
    ```json
    {
        "email": "juan@mail.com",
        "message": "User login successfully",
        "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
        "status": true
    }
    ```
3. Acceso al sistema, Profile

    ```bash
    [GET] /api/auth/profile-user
    ```
    ```bash
    # Headers

    "Authorization": "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    ```
    Respuesta
    ```json
    {
        "fullNames": "Juan C.",
        "email": "juan@mail.com"
    }
    ```

