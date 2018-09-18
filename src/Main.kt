import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.userAgent
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    val server = embeddedServer(Netty, port = 8080, host = "192.168.0.10") {
        routing {
            get("/") {
                val ua = call.request.userAgent() ?: "No user agent"
                val type = determineDevice(ua)
                val query = call.request.queryParameters["origin"]
                println(ua)
                println(query)
                when (type) {
                    ClientDevice.IOS -> call.respondRedirect("/iOS/deeplink?origin=$query")
                    ClientDevice.ANDROID -> call.respondRedirect("/android/deeplink?origin=$query")
                    ClientDevice.OTHER -> call.respondText("$ua\n$type", ContentType.Text.Plain)
                }
            }
            get("/android/deeplink"){
                val query = call.request.queryParameters["origin"]
                call.respondText("Hello Android, from $query", ContentType.Text.Plain)
            }
            get("/iOS/deeplink"){
                val query = call.request.queryParameters["origin"]
                call.respondText("Hello iOS, from $query", ContentType.Text.Plain)
            }
        }
    }
    server.start(wait = true)
}

fun determineDevice(userAgent: String): ClientDevice =
        if (arrayOf("iPhone", "iPad", "iPod").any { userAgent.contains(it) })
            ClientDevice.IOS
        else if (userAgent.contains("Android"))
            ClientDevice.ANDROID
        else
            ClientDevice.OTHER

enum class ClientDevice {
    IOS, ANDROID, OTHER
}