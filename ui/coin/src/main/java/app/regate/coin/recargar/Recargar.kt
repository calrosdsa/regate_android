package app.regate.coin.recargar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.regate.common.composes.ui.SimpleTopBar
import app.regate.common.composes.viewModel
import app.regate.common.resources.R
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject


typealias Recargar = @Composable (
    navigateUp:()->Unit,
) -> Unit


@Inject
@Composable
fun Recargar(
    viewModelFactory:()->RecargarViewModel,
    @Assisted navigateUp: () -> Unit,
){
    Recargar(navigateUp = navigateUp,viewModel = viewModel(factory = viewModelFactory))
}


@Composable
internal fun Recargar(
    viewModel: RecargarViewModel,
    navigateUp:()->Unit,
) {
    val state by viewModel.state.collectAsState()
    Recargar(viewState = state,navigateUp = navigateUp)
}

@Composable
internal fun Recargar(
    viewState: RecargarState,
    navigateUp: () -> Unit
){
    Scaffold(
        topBar = {
         SimpleTopBar(navigateUp = navigateUp,title =  stringResource(id = R.string.purchase))
        },
    ) {paddingValues ->
        LazyColumn(modifier = Modifier
            .padding(paddingValues)
            .padding(10.dp)
            .fillMaxSize()){
            item{
                Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom) {
                Text(text = stringResource(id = R.string.purchase),style = MaterialTheme.typography.titleMedium)
                    Text(text = "Unidad: BOB",style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Light
                    ))
                }
            }
            items(
                items = viewState.coins,
                key = {it.id}
            ){coin->
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                    modifier = Modifier.padding(vertical =  10.dp)
                ) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically){
                    Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = coin.amount.toString(), style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ))
                        Spacer(modifier = Modifier.width(5.dp))
                        Image(painter = painterResource(id = R.drawable.coin), contentDescription = null,
                            modifier = Modifier.size(25.dp))
                    }
                    Surface(shape = MaterialTheme.shapes.large,
                    color = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.width(70.dp),
                    onClick = {}) {
                        Text(text = coin.price.toString(),style = MaterialTheme.typography.labelMedium,
                            modifier =Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                        textAlign = TextAlign.Center)
                    }
                }
                }
            }
        }
    }
}

