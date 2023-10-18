package app.regate.util

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import app.regate.common.resources.R
import app.regate.inject.ApplicationScope
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import me.tatarka.inject.annotations.Inject
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.Locale


@ApplicationScope
@Inject
class AppMedia {

    suspend fun saveImageFromUrl(context: Context,url:String){
        try{
            val loader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(url)
                .allowHardware(false) // Disable hardware bitmaps.
                .build()

            val result = (loader.execute(request) as SuccessResult).drawable
            val bitmap = (result as BitmapDrawable).bitmap
            saveImage(bitmap,context)
        }catch (e:Exception){
            //TODO
        }
    }
    fun saveImage(bitmap: Bitmap, context: Context,folderName:String = "Regate") {
        if (Build.VERSION.SDK_INT >= 29) {
            val values = contentValues()
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/$folderName")
            values.put(MediaStore.Images.Media.IS_PENDING, true)
            // RELATIVE_PATH and IS_PENDING are introduced in API 29.
            val uri: Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (uri != null) {
                try{
                    saveImageToStream(bitmap, context.contentResolver.openOutputStream(uri))
                    values.put(MediaStore.Images.Media.IS_PENDING, false)
                    context.contentResolver.update(uri, values, null, null)
                    Toast.makeText(context,context.getString(R.string.saved_correctly), Toast.LENGTH_SHORT).show()
                }catch(e:Exception){
                    Toast.makeText(context,context.getString(R.string.unexpected_error), Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            try{
                val directory = File(Environment.getExternalStorageDirectory().toString() + File.separator + folderName)
                // getExternalStorageDirectory is deprecated in API 29

                if (!directory.exists()) {
                    directory.mkdirs()
                }
                val fileName = System.currentTimeMillis().toString() + ".png"
                val file = File(directory, fileName)
                saveImageToStream(bitmap, FileOutputStream(file))
                val values = contentValues()
                values.put(MediaStore.Images.Media.DATA, file.absolutePath)
                // .DATA is deprecated in API 29
                context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                Toast.makeText(context,context.getString(R.string.saved_correctly), Toast.LENGTH_SHORT).show()
            }catch (e:Exception){
                Toast.makeText(context,context.getString(R.string.unexpected_error), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun contentValues() : ContentValues {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        return values
    }

    private fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

//    @Suppress("DEPRECATION")
//     fun getAddress(lat: Double, long: Double):Address?{
//        Log.d("DEBUG_APP","INITTT6")
//        val geocoder = Geocoder(activity.applicationContext, Locale.getDefault())
//        val addresses = geocoder.getFromLocation(lat, long, 1)
//        return addresses?.get(0)
//    }
//
//    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
//    private fun getAddressApi33(lat: Double, long: Double):Address?{
//        var address:Address? = null
//        val geoCoder = Geocoder(activity.applicationContext, Locale.getDefault())
//        geoCoder.getFromLocation(lat,long,1,
//            object: Geocoder.GeocodeListener{
//                override fun onGeocode(addresses: MutableList<Address>) {
//                    address = addresses.get(0)
//                }
//                override fun onError(errorMessage: String?) {
//                    super.onError(errorMessage)
//                    address = null
//                }
//            })
//        return address
//    }

}