package app.regate.welcome.notifications

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.regate.common.resources.R

@Composable
internal fun EnabledNotification(
//    navigateToHome:()->Unit,
    requestPermission:()->Unit,
    ){

        Box(modifier = Modifier
            .padding(horizontal = 25.dp)
            .fillMaxSize()
            .fillMaxWidth()){
            Column(modifier = Modifier
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally) {
                
            Image(painter = painterResource(id = R.drawable.notification_image), contentDescription = null,
            modifier = Modifier
                .size(200.dp), contentScale = ContentScale.Fit)
                Spacer(modifier = Modifier.height(60.dp))
                
//                Text(text = stringResource(id = R.string.do_you_want_receive_notifications),
//                textAlign = TextAlign.Center, style = MaterialTheme.typography.headlineLarge)

                Spacer(modifier = Modifier.height(40.dp))

                Button(onClick = { requestPermission() },modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(15.dp)
                ) {
                    Text(text = stringResource(id = R.string.continuar))
                }

                Spacer(modifier = Modifier.height(10.dp))

//                Button(onClick = { navigateToHome() },modifier = Modifier.fillMaxWidth(),
//                    contentPadding = PaddingValues(15.dp),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = MaterialTheme.colorScheme.inverseOnSurface
//                    ),
//                ) {
//                    Text(text = stringResource(id = R.string.later),color = MaterialTheme.colorScheme.inverseSurface)
//                }
            }
        }


}