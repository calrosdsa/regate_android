package app.regate.welcome.location

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.regate.common.composes.components.images.AsyncImage
import app.regate.common.resources.R

@Composable
internal fun EnabledLocation(
    navigateToHome:()->Unit,
    requestLocationPermission:()->Unit
    ){
    Box(modifier = Modifier
        .padding(horizontal = 25.dp)
        .fillMaxSize()
        .fillMaxWidth()){
        Column(modifier = Modifier
            .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally) {

            Image(painter = painterResource(id = R.drawable.enabled_location), contentDescription = null,
                modifier = Modifier
                    .size(200.dp), contentScale = ContentScale.Fit)
            Spacer(modifier = Modifier.height(20.dp))

            Text(text = stringResource(id = R.string.what_is_your_location),
                textAlign = TextAlign.Center, style = MaterialTheme.typography.headlineLarge)

            Spacer(modifier = Modifier.height(40.dp))

            Button(onClick = { requestLocationPermission() },modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(15.dp)
            ) {
                Text(text = stringResource(id = R.string.continuar))
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = { navigateToHome() },modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.inverseOnSurface
                ),
            ) {
                Text(text = stringResource(id = R.string.later),color = MaterialTheme.colorScheme.inverseSurface)
            }
        }
    }
}