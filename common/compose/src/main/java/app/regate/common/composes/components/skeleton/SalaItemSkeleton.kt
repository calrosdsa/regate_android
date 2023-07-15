package app.regate.common.composes.components.skeleton

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.regate.common.composes.ui.Skeleton

@Composable
fun SalaItemSkeleton(modifier: Modifier = Modifier){
    Column(modifier = modifier.padding(10.dp)) {
    Skeleton(modifier = Modifier
        .height(20.dp)
        .width(145.dp)
        .padding(5.dp))
        Skeleton(modifier = Modifier
            .height(18.dp)
            .width(205.dp)
            .padding(5.dp))
        Skeleton(modifier = Modifier
            .height(17.dp)
            .width(55.dp)
            .padding(5.dp))

    }
}