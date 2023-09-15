package app.regate.common.compose.components.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.regate.common.compose.components.images.ProfileImage
import app.regate.data.dto.account.user.ProfileDto
import app.regate.data.dto.empresa.establecimiento.EstablecimientoReview

@Composable
fun ReviewItem(
    review:EstablecimientoReview,
    navigateToProfile:(Long) ->Unit,
    modifier:Modifier = Modifier,
){
    Column(
        modifier = modifier.padding(10.dp)
    ) {
            review.profile?.let {profile->
        Row (verticalAlignment = Alignment.CenterVertically){

            ProfileImage(
                profileImage = profile.profile_photo,
                modifier = Modifier.size(40.dp)
                    .clickable{
                       navigateToProfile(profile.profile_id)
                    },
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(text = "${profile.nombre} ${profile.apellido ?:""}",
                style = MaterialTheme.typography.labelLarge)
                Row() {
                repeat(review.score){
                    Icon(
                        imageVector = Icons.Default.Star,
                        modifier = Modifier.size(16.dp),
                        tint = Color(252,181,3),
                        contentDescription = null
                    )
                }
                }
            }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
                Text(text = review.review,
                    style = MaterialTheme.typography.bodySmall)

    }
}

@Preview
@Composable
fun ReviewItemPreview(){
    ReviewItem(
        review = EstablecimientoReview(
            establecimiento_id = 1,
            profile = ProfileDto(
                nombre = "John",
                apellido = "Doe",
                profile_id = 1,
                profile_photo = null
            ),
            review = "Es un muy buen lugar para reservar y jugar",
            score = 4
        ),
        navigateToProfile = {}
    )
}