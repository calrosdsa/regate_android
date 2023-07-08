package app.regate.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.regate.common.composes.ui.CommonTopBar
import app.regate.common.composes.viewModel
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias Setting = @Composable (
    navigateUp:()->Unit
        ) -> Unit

@Inject
@Composable
fun Setting(
    viewModelFactory:()-> SettingViewModel,
    @Assisted navigateUp: () -> Unit
){
    Setting(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navigateUp
    )
}

@Composable
internal fun Setting(
    viewModel: SettingViewModel,
    navigateUp: () -> Unit
){
    val viewState by viewModel.state.collectAsState()
    Setting(
        viewState = viewState,
        navigateUp = navigateUp
    )
}

@Composable
internal fun Setting(
    viewState: SettingState,
    navigateUp: () -> Unit
){
   Scaffold(modifier = Modifier.fillMaxSize()
       .padding(10.dp),
       topBar = {
           CommonTopBar(onBack = navigateUp)
       }
   ) {paddingValues->
       Box(modifier = Modifier
           .padding(paddingValues)
           .fillMaxSize()){
           Text(text = "Setting Screen ${viewState.user?.nombre}")
       }
   }
}