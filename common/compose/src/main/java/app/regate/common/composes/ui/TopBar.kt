package app.regate.common.composes.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import app.regate.common.resources.R
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    modifier:Modifier = Modifier,
    openDrawer:()->Unit = {},
){
    TopAppBar(
        modifier = modifier,
        title = {
            Text(text = "Regate", style = MaterialTheme.typography.titleLarge)
        },
        navigationIcon = {
            Box() {
                Image(
                    painter = painterResource(id = R.drawable.logo_app),
                    contentDescription = "logo_home", modifier = Modifier
                        .size(25.dp)
                        .align(
                            Alignment.Center
                        )
                )
            }
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search_icon"
                )
            }
            IconButton(onClick = { openDrawer() }) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "person_icon"
                )
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTopBar(
    navigateUp:()->Unit,
    modifier: Modifier = Modifier,
    title:String? = null,
    iconBack:ImageVector = Icons.Default.ArrowBack,
    scrollBehavior: TopAppBarScrollBehavior? = null ,
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = { navigateUp() }) {
                Icon(imageVector = iconBack, contentDescription = null)
            }
        },
        modifier = modifier,
        title = {
            if (title != null) {
                Text(text = title, maxLines = 1,)
            }
        },
        scrollBehavior = scrollBehavior,
        actions = actions
    )

}