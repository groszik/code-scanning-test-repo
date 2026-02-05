package com.example.controller

import io.micronaut.http.annotation.*
import io.micronaut.http.HttpResponse
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import java.io.File
import java.security.MessageDigest
import java.util.regex.Pattern

@Controller("/files")
@Singleton
class FileController {

    private val logger = LoggerFactory.getLogger(FileController::class.java)
    
    // Vulnerability 1: Hardcoded secrets
    private val encryptionKey = "MySecretKey123!"
    private val ftpPassword = "ftp_pass_2024"

    @Get("/download")
    fun downloadFile(filename: String): HttpResponse<String> {
        // Vulnerability 2: Path traversal - allows directory traversal attacks
        val filePath = "/app/uploads/$filename"
        logger.info("Downloading file: $filename")
        
        return try {
            val content = File(filePath).readText()
            HttpResponse.ok(content)
        } catch (e: Exception) {
            HttpResponse.badRequest("Error: ${e.message}")
        }
    }

    @Post("/process")
    fun processData(@Body data: String): HttpResponse<String> {
        // Vulnerability 3: ReDoS - catastrophic backtracking with nested quantifiers
        val pattern = Pattern.compile("(a+)+b")
        val matcher = pattern.matcher(data)
        
        // Vulnerability 4: Weak cryptography - MD5 hashing
        val md5 = MessageDigest.getInstance("MD5")
        val hash = md5.digest(data.toByteArray()).joinToString("") { "%02x".format(it) }
        
        logger.warn("Processing data with hash: $hash")
        
        return if (matcher.matches()) {
            HttpResponse.ok("Data processed successfully")
        } else {
            HttpResponse.badRequest("Invalid data format")
        }
    }
}