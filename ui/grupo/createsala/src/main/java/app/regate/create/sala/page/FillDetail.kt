package app.regate.create.sala.page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.regate.common.resources.R
import app.regate.compoundmodels.InstalacionCupos
import kotlinx.datetime.Instant

@Composable
internal  fun Page2 (
    reservarInstalacion:@Composable ()->Unit,
    formatShortTime:(time: Instant)->String,
    formatDate:(date: Instant)->String,
    instalacionCupos: InstalacionCupos? = null,
) {
    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 5.dp)
                .verticalScroll(rememberScrollState()),
//                .padding(bottom = 60.dp),
//            horizontalArrangement = Arrangement.SpaceBetween
        ) {
                Text(text = stringResource(id = R.string.choose_the_time_and_place),style = MaterialTheme.typography.titleMedium,
                    modifier =  Modifier.padding(10.dp))
//            Spacer(modifier = Modifier.height(100.dp))
            Box(modifier = Modifier.height(400.dp)){
            reservarInstalacion()
            }
            Divider(modifier = Modifier.padding(vertical = 5.dp))
           InstalacionSelected(
               instalacionCupos = instalacionCupos,
               formatDate = formatDate,
               formatShortTime = formatShortTime,)


        }

    }
}