package com.laioffer

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

@Serializable
data class Playlist (
    val id: Long,
    val songs: List<Song>
)

@Serializable
data class Song(
    val name: String,
    val lyric: String,
    val src: String,
    val length: String
)

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)

}

fun Application.module() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
        })
    }
    // TODO: adding the routing configuration here
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        get("/feed") {
            val jsonString = this::class.java.classLoader.getResource("feed.json")?.readText()
            // last line is return
            call.respondText(jsonString ?: "", ContentType.Application.Json)
        }

        get("/playlists") {
            val jsonString = this::class.java.classLoader.getResource("playlists.json")?.readText()
            call.respondText(jsonString ?: "", ContentType.Application.Json)
        }

        get("/playlist/{id}") {
            // read to string so it can be conver to object
            this::class.java.classLoader.getResource("playlists.json")?.readText()?.let { jsonString ->
                // convert string to array of playlist
                val playlists = Json.decodeFromString(ListSerializer(Playlist.serializer()), jsonString)
                val id = call.parameters["id"]
                // if itereables item's id is same as id
                val item = playlists.firstOrNull { it.id.toString() == id }
                call.respondNullable(item)
            } ?: call.respond("null")
        }

        // serving static files
        // http://0.0.0.0:8080/songs/solo.mp3
        static("/") {
            staticBasePackage = "static"
            static("songs") {
                resources("songs")
            }
        }
    }
}

