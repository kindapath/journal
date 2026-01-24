package com.kindaboii.journal

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform