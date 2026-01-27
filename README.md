# code-scanning-test-repo

A Kotlin Micronaut service with REST API for testing code scanning tools.

**⚠️ WARNING: This repository contains intentional security vulnerabilities for testing purposes only. Do not use in production.**

## Security Vulnerabilities Included

1. **Hardcoded Secrets** - API keys and passwords in source code
2. **Log Injection** - Unsanitized user input in log statements
3. **SQL Injection** - Dynamic SQL query construction
4. **Command Injection** - Unsafe command execution
5. **Weak Cryptography** - MD5 hashing and weak random generation
6. **ReDoS** - Regular expression denial of service

## API Endpoints

- `GET /users` - Get all users
- `GET /users/{id}` - Get user by ID
- `POST /users` - Create a new user
- `GET /users/search?name=<name>` - Search users by name
- `POST /users/login` - User login

## Running the Application

```bash
./gradlew run
```

The service will start on port 8080.
