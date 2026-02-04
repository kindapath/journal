package com.kindaboii.journal.network

interface NetworkLogger {
    fun debug(message: String)
    fun info(message: String)
    fun warn(message: String)
    fun error(message: String, throwable: Throwable? = null)
}

class ConsoleNetworkLogger : NetworkLogger {
    override fun debug(message: String) {
        println("Network: $message")
    }

    override fun info(message: String) {
        println("Network: $message")
    }

    override fun warn(message: String) {
        println("Network: $message")
    }

    override fun error(message: String, throwable: Throwable?) {
        println("Network: $message")
        if (throwable != null) {
            println("Network: ${throwable::class.simpleName}: ${throwable.message}")
        }
    }
}
