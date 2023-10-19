package app.regate.constant

const val  AppUrl = "https://regate-p.s3.sa-east-1.amazonaws.com"
//private const val base = "api.regate.site"

object Host {
//   private const val base = "192.168.0.9"
    private const val base = "api.regate.site"
    const val host = base
    const val port = 9000
    const val url = "http://$base:$port"
    }

object HostAdmin {
//       private const val base = "192.168.0.9"
    private const val base = "api.regate.site"
    const val host = base
    const val port = 9000
    const val url = "http://$base:$port"
}

object HostMessage {
//    private const val base = "192.168.0.9"
    private const val base = "message.regate.site"
    const val host = base
    const val port = 9000
    const val url = "http://$base:$port"
}