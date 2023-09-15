package app.regate.common.compose.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CommonTopBar(modifier:Modifier = Modifier,
onBack:()->Unit) {
    Column (modifier = modifier){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
        ,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { onBack() }) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back_esta")
        }
        IconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Default.Share, contentDescription = "back_esta")
        }
    }
        Divider()
    }
}