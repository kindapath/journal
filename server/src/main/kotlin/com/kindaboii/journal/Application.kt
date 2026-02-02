package com.kindaboii.journal

import com.kindaboii.journal.mock.FakeEntryRepository // DELETE LATER
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(Netty, port = 10000, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    routing {
        get("/") {
            call.respondText("Ktor: hello!")
        }
        get("/api/entries") {
            call.respond(FakeEntryRepository.getEntries())
        }
        get("/api/entries/{id}") {
            val id = call.parameters["id"]
            val entry = id?.let { FakeEntryRepository.getEntryById(it) }
            if (entry == null) {
                call.respondText("Not found", status = io.ktor.http.HttpStatusCode.NotFound)
            } else {
                call.respond(entry)
            }
        }
    }
}
