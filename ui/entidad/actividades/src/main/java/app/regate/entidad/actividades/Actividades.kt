package app.regate.entidad.actividades

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import me.tatarka.inject.annotations.Inject

typealias ActividadesEstablecimiento = @Composable (
//    viewModel:ActividadEstablecimientoViewModel
) -> Unit

//@Inject
//@Composable
//fun ActividadesEstablecimiento(
//    viewModelFactory:(SavedStateHandle)-> ActividadEstablecimientoViewModel,
//){
//    ActividadesEstablecimiento(
//        viewModel = viewModel(factory = viewModelFactory)
//    )
//}

@Inject
@Composable
fun ActividadesEstablecimiento(
//    viewModel: ActividadEstablecimientoViewModel
){
    LaunchedEffect(key1 = true, block = {
//        viewModel.hello()
    })
    Box(){
        Text(text = "daksdmaks")
    }
}
