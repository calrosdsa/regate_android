package app.regate.constant

const val  AppUrl1 = "https://regate-p.s3.sa-east-1.amazonaws.com"
//private const val base = "api.regate.site"
//private const val baseHost = "192.168.0.5"
private const val baseHost = "172.20.20.76"


const val commonPort = 9090
object Host {
//    private const val base = "api.regate.site"
//    private const val base = "172.20.20.76"
    const val host_1 = baseHost
    const val port = commonPort
    const val url = "http://$baseHost:$port"
    }

object HostAdmin {
//    private const val base = "api.regate.site"
//    private const val base = "172.20.20.76"
    const val host = baseHost
    const val port = commonPort
    const val url = "http://$baseHost:$port"
}   

object HostMessage {
//    private const val base = "message.regate.site"
//    private const val base = "172.20.20.76"
    const val host = baseHost
    const val port = 9091
    const val url = "http://$baseHost:$port"
}