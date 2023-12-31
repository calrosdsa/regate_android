package app.regate.notifications

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.transform.CircleCropTransformation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.ensureActive

class Util {
    companion object {
        @SuppressLint("SuspiciousIndentation")
        suspend fun getBitmap(
            url: String, context: Context,
            default: String = "https://cdn-icons-png.flaticon.com/128/847/847969.png"
        ): Bitmap {
            val bitmap = CoroutineScope(Dispatchers.IO).async {
                val loader = ImageLoader(context)
                val request = ImageRequest.Builder(context)
                    .data(url.ifBlank { default })
                    .allowHardware(false) // Disable hardware bitmaps.
                    .transformations(CircleCropTransformation())
                    .build()

                val result = (loader.execute(request) as SuccessResult).drawable
                val bitmap = (result as BitmapDrawable).bitmap
                return@async bitmap
            }
            return bitmap.await()
        }
    }
}
