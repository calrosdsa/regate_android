package app.regate.auth.signup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.regate.common.composes.components.select.SelectComponent

@Composable
fun SelectGenero(
    genero: Genero?,
    selectGenero:(genero: Genero?)->Unit,
    navigateTab:(tab:Int)->Unit,
    goHome:()->Unit,
    modifier: Modifier = Modifier
    ) {
//    val selected = remember { mutableStateOf(genero) }
    Column(modifier ) {
    SelectComponent(selected = Genero.MALE == genero,
        onSelect = {if(it)selectGenero(Genero.MALE) else selectGenero(null) }, text = "Home")
    Spacer(modifier = Modifier.height(15.dp))
    SelectComponent(selected = genero == Genero.FAMELE,
        onSelect = {if(it)selectGenero(Genero.FAMELE) else selectGenero(null) },text = "Mujer")
    Spacer(modifier = Modifier.height(50.dp))
    Button(
        onClick = { goHome() }, modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        enabled = genero != null,
        shape = CircleShape
    ) {
        Text(text = "Siguiente")
    }
        Spacer(modifier = Modifier.height(15.dp))
        Button(
            onClick = { navigateTab(1) }, modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = CircleShape
        ) {
            Text(text = "Atras")
        }
    }
}