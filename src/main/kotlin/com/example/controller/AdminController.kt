package com.example.controller

import io.micronaut.http.annotation.*
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import java.sql.Connection
import java.sql.DriverManager
import java.security.MessageDigest

@Controller("/admin")
@Singleton
class AdminController {

    private val logger = LoggerFactory.getLogger(AdminController::class.java)
    
    // Hardcoded credentials
    private val adminToken = "admin_token_12345_secret"
    private val encryptionKey = "AES256_SECRET_KEY_HARDCODED_123"
    
    @Get("/logs")
    fun getLogs(filter: String?): String {
        // Log injection vulnerability
        logger.info("Admin accessing logs with filter: $filter")
        
        // Command injection vulnerability
        val command = "grep '$filter' /var/log/app.log"
        val process = Runtime.getRuntime().exec(command)
        return process.inputStream.bufferedReader().readText()
    }
    
    @Post("/execute")
    fun executeCommand(cmd: String): String {
        // Direct command execution without validation
        logger.warn("Executing admin command: $cmd")
        return Runtime.getRuntime().exec(cmd).inputStream.bufferedReader().readText()
    }
    
    @Get("/config")
    fun getConfig(file: String): String {
        // Path traversal vulnerability
        val configPath = "/app/config/$file"
        return java.io.File(configPath).readText()
    }
    
    @Post("/hash")
    fun hashPassword(password: String): String {
        // Weak cryptography - MD5
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(password.toByteArray())
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
}