package app.regate.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import app.regate.inject.ActivityScope
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import me.tatarka.inject.annotations.Inject

@ActivityScope
@Inject
class AppUtil(
    private val activity: Activity
) {
    fun openInBrowser(context: Context,url:String){
        try{
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(browserIntent)
        }catch (e:Exception){
            throw e
        }
    }
    fun shareTextIntent(context: Context,data:String){
        try{

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, data)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
        }catch(e:Exception){
            //TODO()
        }
    }
    suspend fun convertUrlImgToBitamp(context:Context,photo:String):Bitmap{
        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(photo)
            .allowHardware(false) // Disable hardware bitmaps.
            .build()

        val result = (loader.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }
    fun openMap(lng:String?,lat:String?,label:String?){
        try{
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("geo:$lat,$lng?q=$lat,$lng(${label})")
            )
            activity.startActivity(intent)
//            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=$lat,$lng"))
//            context.startActivity(browserIntent)
        }catch(e:Exception){
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=$lat,$lng"))
            activity.startActivity(browserIntent)
            Log.d("DEBUG_APP",e.localizedMessage?:"")
        }catch (e:Exception){
            Log.d("DEBUG_APP",e.localizedMessage?:"")

        }
    }

    fun updateKeboardHeight(
        activity: Activity,
        setBottomLayoutHeight:(Int)->Unit
    ){
        val insets = ViewCompat.getRootWindowInsets(activity.window.decorView)
        val keyboardHeight =
            insets?.getInsets(WindowInsetsCompat.Type.ime())?.bottom
        val bottomBarHeight = insets?.getInsets(WindowInsetsCompat.Type.navigationBars())?.bottom
        Log.d("DEBUG_APP_INSET",keyboardHeight.toString())
        Log.d("DEBUG_APP_INSET",bottomBarHeight.toString())

        setBottomLayoutHeight(keyboardHeight!! - bottomBarHeight!!)
    }

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.TIRAMISU)
    fun isRequiredAskForNotificationPermission():Boolean{
        return Build.VERSION.SDK_INT >= 33
    }

}