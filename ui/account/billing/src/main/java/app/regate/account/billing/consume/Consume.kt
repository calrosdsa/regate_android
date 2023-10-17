package app.regate.account.billing.consume

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import app.regate.common.composes.LocalAppDateFormatter
import app.regate.common.composes.ui.Loader
import app.regate.common.composes.util.itemsCustom
import app.regate.common.composes.viewModel
import me.tatarka.inject.annotations.Inject
import app.regate.common.resources.R
import app.regate.data.dto.account.billing.ConsumeDto
import app.regate.data.dto.account.billing.TypeEntity
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Assisted

typealias Consume= @Composable (
    navigateToSala: (Long) -> Unit,
    navigateToReserva: (Long) ->Unit
 ) ->Unit


@Inject
@Composable
fun Consume(
    viewModelFactory:()->ConsumeViewModel,
    @Assisted navigateToSala: (Long) -> Unit,
    @Assisted navigateToReserva: (Long) -> Unit
){
    Consume(viewModel = viewModel(factory = viewModelFactory) ,
    navigateToReserva = navigateToReserva,
        navigateToSala= navigateToSala
    )
}


@Composable
internal fun Consume(
    viewModel:ConsumeViewModel,
    navigateToReserva: (Long) -> Unit,
    navigateToSala: (Long) -> Unit
){
    val lazyPagingItems = viewModel.pagedList.collectAsLazyPagingItems()
    val formatter = LocalAppDateFormatter.current
    LazyColumn(modifier = Modifier.fillMaxSize(),content = {
        itemsCustom(
            items = lazyPagingItems
        ){item->
            if(item != null){
                ConsumeItem(item = item,
                navigate = {
                    when(item.type_entity){
                        TypeEntity.RESERVA.ordinal -> navigateToReserva(item.id_entity)
                        TypeEntity.SALA.ordinal -> navigateToSala(item.id_entity)
                        else -> {}
                    }
                },
                    formatterDateTime = formatter::formatMediumDateTime
                )
                Divider()
            }
        }
        item{
            if(lazyPagingItems.loadState.append == LoadState.Loading){
                Loader()
            }
        }
    })
}


@Composable
internal fun ConsumeItem(
    item:ConsumeDto,
    navigate:()->Unit,
    formatterDateTime:(Instant)->String
){
    Column(modifier = Modifier
        .clickable { navigate() }
        .fillMaxWidth()
        .padding(vertical = 10.dp)) {
        Text(text = item.message,modifier = Modifier,style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary)
        Text(text = stringResource(id = R.string.amount , item.amount.toString()),modifier = Modifier,style = MaterialTheme.typography.labelMedium)
        Text(text = formatterDateTime(item.created_at),modifier = Modifier,style = MaterialTheme.typography.labelMedium)
    }
}