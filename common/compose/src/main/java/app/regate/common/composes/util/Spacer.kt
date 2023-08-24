package app.regate.common.composes.util

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Divider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

fun LazyListScope.dividerLazyList() {
    item{
    Divider(modifier = Modifier.padding(vertical = 10.dp))
    }
}


fun LazyListScope.spacerLazyList() {
    item{
        Spacer(modifier = Modifier.height(10.dp))
    }
}
