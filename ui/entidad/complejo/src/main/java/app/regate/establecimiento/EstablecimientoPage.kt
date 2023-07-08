package app.regate.establecimiento

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import app.regate.common.composes.components.input.AmenityItem
import app.regate.common.composes.ui.PosterCardImage
import app.regate.common.composes.ui.Skeleton
import app.regate.common.resources.R

@OptIn(ExperimentalLayoutApi::class)
@SuppressLint("QueryPermissionsNeeded")
@Composable
fun EstablecimientoPage(
    state: EstablecimientoState ,
    openLocationSheet:()->Unit,
    navigateToReserva:(category:Long)->Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val establecimiento by remember(state.establecimiento) {
        derivedStateOf {
            state.establecimiento
        }
    }
    val categoryCount by remember(state.instalacionCategoryCount) {
        derivedStateOf {
            state.instalacionCategoryCount
        }
    }
    val showMore = remember{ mutableStateOf(false) }
    val rules = remember(state.rules) {
        if(state.rules.size >= 5){
        mutableStateOf(state.rules.slice(0 .. 3))
        }else{
            mutableStateOf(emptyList())
        }
    }
    LaunchedEffect(key1 = rules.value, block = {
        if(showMore.value){
            scrollState.animateScrollTo(scrollState.maxValue)
        }
    })

    LaunchedEffect(key1 = showMore.value, block = {
        if(state.rules.size >= 5){
        if(showMore.value){
            rules.value = state.rules
        }else{
            rules.value = state.rules.slice(0..3)
        }
        }
    })

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
            .verticalScroll(scrollState)
    ) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(vertical = 8.dp)
        ) {
            categoryCount.map { category ->
                InstalacionCategoryItem(
                    item = category,
                    navigateToReserva = navigateToReserva
                )
            }
        }
        Divider(modifier = Modifier.padding(5.dp))
        establecimiento?.address?.let {
            Detail(
                it = it,
                label = "Ubicacion",
                icon = Icons.Default.Place,
                intent = {
                    openLocationSheet()
                }
            )
        }
        establecimiento?.phone_number?.let {
            Detail(
                it = it,
                label = "Telefono",
                icon = Icons.Default.Phone,
                intent = {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:${it}")
                    }
                    context.startActivity(intent)
                }
            )
        }
        establecimiento?.email?.let {
            Detail(
                it = it,
                label = "Email",
                icon = Icons.Default.Email,
                intent = {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "*/*"
                        putExtra(Intent.EXTRA_EMAIL, arrayOf(it))
                        putExtra(Intent.EXTRA_SUBJECT, "")
                        putExtra(Intent.EXTRA_STREAM, "")
                    }
                    if (intent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(intent)
                    }
                }
            )
        }
        Divider(modifier = Modifier.padding(5.dp))
        Text(text = establecimiento?.description ?: "", style = MaterialTheme.typography.bodySmall)
        Divider(modifier = Modifier.padding(5.dp))
        Text(
            text = "Lo que ofrecemos",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 5.dp)
        )
        FlowRow(modifier = Modifier.padding(5.dp)) {
            state.amenities.map { amenity ->
                AmenityItem(amenity = amenity)
                Spacer(modifier = Modifier.width(8.dp))
            }
        }

        Divider(modifier = Modifier.padding(5.dp))
        Text(
            text = "Donde estamos ubicados",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 5.dp)
        )
        PosterCardImage(model = stringResource(id = R.string.location_static_url),
            modifier = Modifier
                .clickable {
                }
                .fillMaxWidth()
                .height(200.dp)
                .padding(10.dp))

        Divider(modifier = Modifier.padding(5.dp))
        Text(
            text = stringResource(id = R.string.rules_of_the_place),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 5.dp)
        )
        rules.value.map {
            Text(
                text = "_ ${it.name}", style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.W400
                )
            )
        }
        if(state.rules.size >= 5){
            Text(
                text =if(showMore.value) stringResource(R.string.show_less) else stringResource(R.string.show_more),
                style = MaterialTheme.typography.labelLarge,
                textDecoration = TextDecoration.Underline,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    showMore.value = !showMore.value
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

    }
}