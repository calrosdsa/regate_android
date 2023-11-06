/*
 * Copyright 2021 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package app.regate.common.composes.ui

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import app.regate.common.composes.component.images.AsyncImage
import app.regate.common.composes.component.images.CardImage
import app.regate.data.dto.empresa.establecimiento.EstablecimientoDto

@Composable
fun PosterCardImageDark (
    model:String?,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier) {
            AsyncImage(
                model = model,
                requestBuilder = {crossfade(true) },
                contentDescription = model,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply{setToScale(0.8f,0.8f,0.8f,1f)}),
        )
    }
}


@Composable
fun  PosterCardImage(
    model:String?,
    modifier: Modifier = Modifier,
    shape:Shape = CardDefaults.shape,
    isUser:Boolean = false,
    enabled: Boolean = true,
    darkerImage:Boolean = false,
    contentScale:ContentScale = ContentScale.Crop,
    onClick:()->Unit = {},
) {

    CardImage(
        src = model,
        shape = shape,
        modifier = modifier,
        contentScale = contentScale,
        isUser = isUser,
        onClick = onClick,
        enabled = enabled,
        colorFilter = if(darkerImage) ColorFilter.colorMatrix(ColorMatrix().apply{setToScale(0.8f,0.8f,0.8f,1f)}) else null,
    )
//    Card(modifier = modifier,
//    shape = shape) {
//        AsyncImage(
//            model = model,
//            requestBuilder = { crossfade(true) },
//            contentDescription = model,
//            modifier = Modifier.fillMaxSize(),
//            contentScale = contentScale,
//        )
//    }
}



@Composable
fun UploadImageBitmap(
    bitmap: Bitmap?,
//    uploadImage:()->Unit,
    modifier: Modifier = Modifier,
    shape:Shape = CircleShape,
) {
    Card(
        modifier = modifier,
        shape = shape
    ) {
        Box(modifier = Modifier.fillMaxSize()) {


            bitmap?.let { btm ->
                Image(
                    bitmap = btm.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }


        }

//            IconButton(onClick = { uploadImage() },modifier = Modifier.align(Alignment.Center)) {
//                Icon(imageVector = Icons.Default.UploadFile, contentDescription = "upload")
//        }

    }
//        Image(
//            bitmap = it,
//            contentDescription = null,
//            modifier = Modifier.fillMaxSize(),
////            contentScale = ContentScale.Crop,
//        )
}


