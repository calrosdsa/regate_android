package app.regate.common.composes.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Loader (
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
    ) {
        CircularProgressIndicator(Modifier.align(Alignment.Center))
    }
}