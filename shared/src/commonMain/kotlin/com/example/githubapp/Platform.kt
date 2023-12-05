package com.example.githubapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform