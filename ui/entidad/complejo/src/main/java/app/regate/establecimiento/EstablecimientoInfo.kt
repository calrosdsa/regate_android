package app.regate.establecimiento

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import app.regate.common.composes.components.input.AmenityItem
import app.regate.common.composes.components.item.ReviewItem
import app.regate.common.composes.ui.PosterCardImage
import app.regate.common.resources.R
import app.regate.data.dto.empresa.establecimiento.EstablecimientoReviews
import app.regate.establecimiento.component.Detail
import app.regate.establecimiento.component.InstalacionCategoryItem

@OptIn(ExperimentalLayoutApi::class)
@SuppressLint("QueryPermissionsNeeded")
@Composable
fun EstablecimientoInfo(
    state: EstablecimientoState ,
    openLocationSheet:()->Unit,
    navigateToReserva:(category:Long)->Unit,
    createReview:(Long)->Unit,
    navigateToProfile:(Long)->Unit,
    navigateToReviews: (Long) -> Unit,
    modifier: Modifier = Modifier,
    openMap:(String?,String?,String?)->Unit
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

        establecimiento?.address_photo?.let{addressPhoto->
        Divider(modifier = Modifier.padding(5.dp))
        Text(
            text = "Donde estamos ubicados",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 5.dp)
        )
        PosterCardImage(model = addressPhoto,
            modifier = Modifier
                .clickable {
                    openMap(
                        establecimiento?.longitud,
                        establecimiento?.latidud,
                        establecimiento?.name
                    )
                }
                .fillMaxWidth()
                .height(200.dp)
                .padding(10.dp))
        }

        state.reviews?.let { data ->
            ReviewBlock(
                data = data,
                navigateToReviews = { state.establecimiento?.let { navigateToReviews(it.id) } },
                navigateToProfile = navigateToProfile,
                createReview = { state.establecimiento?.let { createReview(it.id) } }
            )
//            }
        }

        Spacer(modifier = Modifier.height(10.dp))

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

@Composable
internal fun ReviewBlock(
    data:EstablecimientoReviews,
    navigateToReviews:()->Unit,
    navigateToProfile:(Long)->Unit,
    createReview:()->Unit
    ){
    Column {
    Divider(modifier = Modifier.padding(5.dp))
    Spacer(modifier = Modifier.height(10.dp))
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = Icons.Default.Star, contentDescription = "star")
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "${data.average} - ${data.count} ${stringResource(id = R.string.reviews)}",
            style = MaterialTheme.typography.titleLarge
        )

    }
    Spacer(modifier = Modifier.height(10.dp))
    data.results.map {
        ReviewItem(review = it,navigateToProfile = navigateToProfile)
    }
        Divider()
        Row(
            modifier = Modifier
                .clickable {
                   createReview()
                }
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(id = R.string.rate_this_place))
            Row() {
                repeat(5) {
                    Icon(
                        imageVector = Icons.Outlined.StarOutline, contentDescription = null,
                        modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        Divider()
        if(data.count > 0) {

                OutlinedButton(
                    onClick = { navigateToReviews() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = stringResource(id = R.string.show_reviews, data.count),
                        textAlign = TextAlign.Center
                    )
                }
            }
    }
}


