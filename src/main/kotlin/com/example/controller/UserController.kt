package com.example.controller

import com.example.model.User
import io.micronaut.http.annotation.*
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import java.sql.DriverManager
import java.util.*

@Controller("/users")
@Singleton
class UserController {

    private val logger = LoggerFactory.getLogger(UserController::class.java)
    
    // Vulnerability 1: Hardcoded secret
    private val apiKey = "sk-proj-1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef"
    private val dbPassword = "postgres://user:password123@localhost:5432/mydb"
    private val awsAccessKey = "AKIAIOSFODNN7EXAMPLE"
    private val awsSecretKey = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
    
    private val users = mutableListOf(
        User(1, "John Doe", "john@example.com"),
        User(2, "Jane Smith", "jane@example.com")
    )

    @Get
    fun getAllUsers(): List<User> = users

    @Get("/{id}")
    fun getUserById(id: Long): User? {
        // Vulnerability 2: Log injection
        logger.info("Fetching user with ID: $id")
        return users.find { it.id == id }
    }

    @Post
    fun createUser(@Body user: User): User {
        users.add(user)
        return user
    }
    
    @Get("/search")
    fun searchUsers(name: String?): List<User> {
        // Vulnerability 3: SQL injection
        val query = "SELECT * FROM users WHERE name = '$name'"
        logger.debug("Executing query: $query")
        return users.filter { it.name.contains(name ?: "", ignoreCase = true) }
    }
    
    @Post("/login")
    fun login(username: String, password: String): Map<String, Any> {
        // Vulnerability 4: Weak random number generation
        val sessionId = Random().nextInt()
        
        // Vulnerability 5: Command injection
        val command = "echo User $username logged in"
        Runtime.getRuntime().exec(command)
        
        return mapOf(
            "sessionId" to sessionId,
            "apiKey" to apiKey
        )
    }
}