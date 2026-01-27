package com.example.config

import jakarta.inject.Singleton
import java.security.MessageDigest

@Singleton
class SecurityConfig {
    
    // Additional hardcoded secrets
    val jwtSecret = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ"
    val encryptionKey = "-----BEGIN PRIVATE KEY-----\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC7VJTUt9Us8cKB\n-----END PRIVATE KEY-----"
    val githubToken = "ghp_1234567890abcdef1234567890abcdef123456"
    
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