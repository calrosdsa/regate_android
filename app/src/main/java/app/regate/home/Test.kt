package app.regate.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import app.regate.common.composes.theme.RegateTheme
import app.regate.common.composes.util.DarkLightPreviews


@Composable
fun Test(modifier: Modifier = Modifier) {
//    val complejoDetail = ComplejoDetail()
    val scrollState = rememberScrollState()
    LaunchedEffect(key1 = scrollState.value, block = {
        Log.d("DEBUG_APP",scrollState.value.toString())
    })
    Column(modifier = modifier
        .fillMaxSize()
        .verticalScroll(scrollState)) {
//        Text(text = complejoDetail.title)
        Box(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.4f)
            .graphicsLayer {
                alpha = 1f - (scrollState.value.toFloat() / 100f)
//                translationY = 0.3f * scrollState.value
            }) {
            Image(
                painterResource(id = app.regate.R.drawable.image),
                modifier = Modifier.fillMaxSize(),
                contentDescription = "Heelo",
                contentScale = ContentScale.Crop
            )
        }
        repeat(20){
            Text(text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
        }
    }
}


@DarkLightPreviews
@Composable
fun TextPreview(){
    RegateTheme() {
    Test()
    }
}