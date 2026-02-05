package com.example.controller

import io.micronaut.http.annotation.*
import io.micronaut.http.HttpResponse
import java.sql.DriverManager

@Controller("/vulnerable")
class VulnerableController {

    // Vulnerability 1: Hardcoded Secret
    private val apiKey = "sk-1234567890abcdef"
    private val dbPassword = "admin123"

    @Get("/data/{userId}")
    fun getUserData(@PathVariable userId: String): HttpResponse<String> {
        // Vulnerability 2: SQL Injection
        val connection = DriverManager.getConnection(
            "jdbc:h2:mem:testdb", 
            "sa", 
            dbPassword
        )
        
        val query = "SELECT * FROM users WHERE id = '$userId'"
        val statement = connection.createStatement()
        val result = statement.executeQuery(query)
        
        return HttpResponse.ok("User data retrieved")
    }
}