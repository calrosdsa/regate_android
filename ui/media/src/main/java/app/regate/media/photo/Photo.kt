package app.regate.media.photo

import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Report
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.regate.common.composes.ui.PosterCardImage
import app.regate.common.composes.ui.SimpleTopBar
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import app.regate.common.resources.R
import app.regate.data.dto.system.ReportData
import app.regate.data.dto.system.ReportType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


typealias Photo = @Composable (
    navigateUp:()->Unit,
    url:String,
    navigateToReport:(String)->Unit
) -> Unit


@Inject
@Composable
fun Photo(
    @Assisted navigateUp: () -> Unit,
    @Assisted url :String,
    @Assisted navigateToReport:(String)->Unit
){
    Photo(navigateUp = navigateUp, url_ = url,
    navigateToReport = navigateToReport)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Photo(
    url_:String,
    navigateUp:()->Unit,
    navigateToReport:(String)->Unit
) {
    Scaffold(
        topBar = {
//            IconButton(onClick = { navigateUp() }) {
//                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
//            }
            SimpleTopBar(navigateUp = navigateUp,
            actions = {
                IconButton(onClick = {
                    val report = ReportData(
                        report_type = ReportType.ESTABLECIMIENTO.ordinal,
                        entity_id = 1,
                    )
                    navigateToReport(Json.encodeToString(report)) }) {
                    Icon(imageVector = Icons.Outlined.Flag,
                        contentDescription = stringResource(id = R.string.report))
                }
            })
        }
    ) {paddingValues ->
        Box(modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(paddingValues)
            .fillMaxSize()){
            Text(text = url_)
            PosterCardImage(model = url_,
            shape = RoundedCornerShape(0.dp),
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            contentScale = ContentScale.Fit
            )
        }
    }
}