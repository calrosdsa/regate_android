package app.regate.data.common

import kotlinx.serialization.Serializable

@Serializable
data class AddressDevice(
    val city:String?,
    val country:String,
    val locale:String,
    val country_code: String,
    val admin_area:String?,
    val sub_admin_area:String,
    val longitud:Double,
    val latitud:Double,
)