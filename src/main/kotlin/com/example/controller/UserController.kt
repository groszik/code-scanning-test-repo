package com.example.controller

import com.example.model.User
import io.micronaut.http.annotation.*
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import java.sql.Connection
import java.sql.DriverManager
import java.util.*
import jakarta.annotation.PostConstruct

@Controller("/users")
@Singleton
class UserController {

    private val logger = LoggerFactory.getLogger(UserController::class.java)
    
    // Vulnerability 1: Hardcoded secret
    private val apiKey = "sk-proj-1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef"
    private val dbPassword = "postgres://user:password123@localhost:5432/mydb"
    private val awsAccessKey = "AKIAIOSFODNN7EXAMPLE"
    private val awsSecretKey = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
    private val jwtSecret = "mySecretJWTKey123456789"
    
    private lateinit var connection: Connection
    
    @PostConstruct
    fun initDatabase() {
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1")
        
        // Create users table
        connection.createStatement().execute("""
            CREATE TABLE users (
                id BIGINT PRIMARY KEY,
                name VARCHAR(255),
                email VARCHAR(255)
            )
        """)
        
        // Insert sample data
        connection.createStatement().execute(
            "INSERT INTO users (id, name, email) VALUES (1, 'John Doe', 'john@example.com')"
        )
        connection.createStatement().execute(
            "INSERT INTO users (id, name, email) VALUES (2, 'Jane Smith', 'jane@example.com')"
        )
    }

    @Get
    fun getAllUsers(): List<User> {
        val users = mutableListOf<User>()
        val stmt = connection.createStatement()
        val rs = stmt.executeQuery("SELECT * FROM users")
        
        while (rs.next()) {
            users.add(User(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email")
            ))
        }
        return users
    }

    @Get("/{id}")
    fun getUserById(id: Long): User? {
        // Vulnerability 2: Log injection
        logger.info("Fetching user with ID: $id")
        
        val stmt = connection.createStatement()
        val rs = stmt.executeQuery("SELECT * FROM users WHERE id = $id")
        
        return if (rs.next()) {
            User(rs.getLong("id"), rs.getString("name"), rs.getString("email"))
        } else null
    }

    @Post
    fun createUser(@Body user: User): User {
        val stmt = connection.prepareStatement("INSERT INTO users (id, name, email) VALUES (?, ?, ?)")
        stmt.setLong(1, user.id)
        stmt.setString(2, user.name)
        stmt.setString(3, user.email)
        stmt.executeUpdate()
        return user
    }
    
    @Get("/search")
    fun searchUsers(name: String?): List<User> {
        // Vulnerability 3: SQL injection - directly concatenating user input
        val query = "SELECT * FROM users WHERE name LIKE '%$name%'"
        logger.debug("Executing query: $query")
        
        val users = mutableListOf<User>()
        val stmt = connection.createStatement()
        val rs = stmt.executeQuery(query)
        
        while (rs.next()) {
            users.add(User(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email")
            ))
        }
        return users
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
    
    @Get("/download")
    fun downloadFile(filename: String): String {
        // Vulnerability 6: Path traversal - allows access to any file on the system
        val filePath = "/app/files/$filename"
        return try {
            java.io.File(filePath).readText()
        } catch (e: Exception) {
            "File not found: ${e.message}"
        }
    }
}