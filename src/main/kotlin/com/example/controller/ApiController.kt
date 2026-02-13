package com.example.controller

import io.micronaut.http.annotation.*
import io.micronaut.http.HttpResponse
import java.sql.DriverManager
import java.security.MessageDigest
import org.slf4j.LoggerFactory

@Controller("/api")
class ApiController {

    private val logger = LoggerFactory.getLogger(ApiController::class.java)
    
    // Vulnerability 1: Hardcoded Secret
    private val secretKey = "prod-secret-key-12345"
    private val awsAccessKey = "AKIAIOSFODNN7EXAMPLE"
    private val dbPassword = "P@ssw0rd123"

    @Get("/query")
    fun query(@QueryValue sql: String): HttpResponse<String> {
        // Vulnerability 2: SQL Injection
        val conn = DriverManager.getConnection("jdbc:h2:mem:db", "sa", "")
        val stmt = conn.createStatement()
        stmt.executeQuery("SELECT * FROM data WHERE " + sql)
        return HttpResponse.ok("Query executed")
    }

    @Post("/run")
    fun run(@Body cmd: String): HttpResponse<String> {
        // Vulnerability 3: Command Injection
        Runtime.getRuntime().exec(cmd)
        return HttpResponse.ok("Executed")
    }

    @Get("/track/{input}")
    fun track(@PathVariable input: String): HttpResponse<String> {
        // Vulnerability 4: Log Injection
        logger.info("Tracking: $input")
        return HttpResponse.ok("Tracked")
    }

    @Post("/encrypt")
    fun encrypt(@Body data: String): HttpResponse<String> {
        // Vulnerability 5: Weak Cryptography
        val md5 = MessageDigest.getInstance("MD5")
        val hash = md5.digest(data.toByteArray())
        return HttpResponse.ok(hash.toString())
    }
}