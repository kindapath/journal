package com.kindaboii.journal

import com.kindaboii.journal.mock.FakeEntryRepository // DELETE LATER
import com.kindaboii.journal.mock.EntryResponse
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.defaultheaders.DefaultHeaders
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(Netty, port = 10000, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(DefaultHeaders)
    install(CORS) {
        allowMethod(io.ktor.http.HttpMethod.Get)
        allowMethod(io.ktor.http.HttpMethod.Put)
        allowMethod(io.ktor.http.HttpMethod.Delete)
        allowHeader(io.ktor.http.HttpHeaders.ContentType)
        allowHeader(io.ktor.http.HttpHeaders.Accept)
        allowHeader(io.ktor.http.HttpHeaders.AcceptCharset)
        allowHost("localhost:8081", schemes = listOf("http"))
        allowHost("127.0.0.1:8081", schemes = listOf("http"))
        allowHost("192.168.0.182:8081", schemes = listOf("http"))
        allowHost("localhost:8080", schemes = listOf("http"))
    }
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
        put("/api/entries/{id}") {
            val id = call.parameters["id"]
            if (id == null) {
                call.respondText("Missing id", status = io.ktor.http.HttpStatusCode.BadRequest)
                return@put
            }
            val entry = call.receive<EntryResponse>()
            if (entry.id != id) {
                call.respondText("Path id doesn't match body id", status = io.ktor.http.HttpStatusCode.BadRequest)
                return@put
            }
            val saved = FakeEntryRepository.upsertEntry(entry)
            call.respond(saved)
        }
        delete("/api/entries/{id}") {
            val id = call.parameters["id"]
            if (id == null) {
                call.respondText("Missing id", status = io.ktor.http.HttpStatusCode.BadRequest)
                return@delete
            }
            val deleted = FakeEntryRepository.deleteEntryById(id)
            if (!deleted) {
                call.respondText("Not found", status = io.ktor.http.HttpStatusCode.NotFound)
            } else {
                call.respondText("Deleted")
            }
        }
    }
}
