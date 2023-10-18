package app.regate.constant

const val  AppUrl = "https://regate-p.s3.sa-east-1.amazonaws.com"

object Host {
//    private const val base = "172.20.20.76"
   private const val base = "192.168.0.9"
    const val host = base
    const val port = 9090
    const val url = "http://$base:9090"
    }

object HostAdmin {
//    private const val base = "172.20.20.76"
       private const val base = "192.168.0.9"
    const val host = base
    const val port = 9090
    const val url = "http://$base:9090"
}

object HostMessage {
//    private const val base = "172.20.20.76"
    private const val base = "192.168.0.9"
    const val host = base
    const val port = 9091
    const val url = "http://$base:9091"
}