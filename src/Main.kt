import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.userAgent
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    val server = embeddedServer(Netty, port = 8080) {
        routing {
            get("/") {
                val ua = call.request.userAgent() ?: "No user agent"
                call.respondText(ua, ContentType.Text.Plain)
            }
        }
    }
    server.start(wait = true)
}