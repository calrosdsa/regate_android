package app.regate.constant

object Route {
    const val REPORT = "report"
    const val NOTIFICATIONS = "notifications"

    const val WELCOME_PAGE = "welcome"
    const val LOGIN_PAGE = "login"
    const val SIGNUP_SCREEN = "signup"
    const val EMAIL_VERIFICATION = "email_verification"

    const val MAIN = "main"
    const val HOME = "home"
    const val GRUPOS = "grupos"
    const val ACCOUNT = "account"
    const val ACTIVITIES = "activities"
    const val DISCOVER = "discover"
    const val SERVICIOS = "servicios"

    const val RESERVA_DETAIL = "reserva_detail"

    const val ESTABLECIMIENTO = "complejo_detail"

    const val INSTALACION = "instalacion"

    const val SALA = "establecimiento_salas"
    const val SALA_COMPLETE = "sala_complete"
    const val FILTER_SALAS = "filter_salas"
    const val CREAR_SALA = "crear_sala"

    const val RESERVAR = "reservar"

    const val AUTH_DIALOG = "auth_dialog"
    const val MAP = "map"
    const val CHAT_GRUPO = "chat_grupo"
    const val MY_GROUPS = "my_groups"
    const val CHAT_SALA = "chat_sala"

    const val SETTING = "setting"
    const val RESERVAS = "reservas"
    const val FAVORITES = "favorites"

    const val PROFILE = "profile"
    const val EDIT_PROFILE = "edit_profile"

    const val FILTER = "filter"

    const val GRUPO = "grupo"
    const val CREATE_GROUP = "create_group"
    const val GRUPO_SALAS = "grupo_salas"

    const val ESTABLECIMIENTO_FILTER = "establecimiento_filter"
    const val PHOTO = "media_photo"

    const val CONVERSATION = "conversation"
    const val INBOX = "inbox"
    const val BILLING = "billing"
    const val RECARGAR = "recargar"
    const val PAY = "pay"

    const val REVIEWS = "review"
    const val CREATE_REVIEW = "create_review"


}


infix fun String.arg(arg: String) = "$this/{$arg}"

infix fun String.query(query: String) = "$this?$query={$query}"

infix fun String.id(id: Long) = "$this/$id"

infix fun String.id(id: String) = "$this/$id"



infix fun String.bearer(token:String) = "this $token"