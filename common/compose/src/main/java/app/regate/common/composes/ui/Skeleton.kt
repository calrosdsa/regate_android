package app.regate.common.composes.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.regate.common.composes.util.shimmerBackground

@Composable
fun Skeleton(modifier: Modifier = Modifier){
    Box(modifier = modifier.shimmerBackground(CardDefaults.shape))
}