package app.regate.util

import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale


fun getDayName(day:Int):String{
   return when(day){
        1 -> "Lunes"
        2 -> "Martes"
        3 -> "Miercoles"
        4 -> "Jueves"
        5 -> "Viernes"
        6 -> "Sabado"
        0 -> "Domingo"
       else -> {""}
   }
}

fun roundOffDecimal(number: Double): Double? {
    return try{
        val df = DecimalFormat("#.##", DecimalFormatSymbols(Locale.ENGLISH))
        df.roundingMode = RoundingMode.CEILING
        df.format(number).toDouble()
    }catch (e:Exception){
        null
    }
}





