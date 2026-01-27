package com.example.config

import jakarta.inject.Singleton
import java.security.MessageDigest

@Singleton
class SecurityConfig {
    
    // Additional hardcoded secrets
    val jwtSecret = "mySecretKey123"
    val encryptionKey = "AES256Key1234567"
    
    fun hashPassword(password: String): String {
        // Vulnerability: Using weak MD5 hashing
        val md = MessageDigest.getInstance("MD5")
        return md.digest(password.toByteArray()).joinToString("") { "%02x".format(it) }
    }
    
    fun validateInput(input: String): Boolean {
        // Vulnerability: Regex DoS
        val pattern = Regex("(a+)+b")
        return pattern.matches(input)
    }
}