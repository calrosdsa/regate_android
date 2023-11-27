package app.regate.account.billing.montoretenido

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
import app.regate.account.billing.consume.ConsumeItem
import app.regate.common.composes.LocalAppDateFormatter
import app.regate.common.composes.ui.Loader
import app.regate.common.composes.util.itemsCustom
import app.regate.common.composes.viewModel
import app.regate.data.dto.account.billing.DepositDto
import me.tatarka.inject.annotations.Inject
import app.regate.common.resources.R
import app.regate.data.dto.account.billing.ConsumeDto
import app.regate.data.dto.account.billing.MontoRetenidoDto
import app.regate.models.TypeEntity
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Assisted

typealias MontoRetenido = @Composable (
    navigateToSala:(Long)->Unit,
    navigateToSalaComplete:(Long)->Unit,
 ) ->Unit


@Inject
@Composable
fun MontoRetenido(
    viewModelFactory:()->MontoRetenidoViewModel,
    @Assisted navigateToSala: (Long) -> Unit,
    @Assisted navigateToSalaComplete: (Long) -> Unit,
    ){
    MontoRetenido(
        viewModel = viewModel(factory = viewModelFactory),
        navigateToSala = navigateToSala,
        navigateToSalaComplete = navigateToSalaComplete
    )
}


@Composable
internal fun MontoRetenido(
    viewModel:MontoRetenidoViewModel,
    navigateToSala: (Long) -> Unit,
    navigateToSalaComplete: (Long) -> Unit
){
    val formatter = LocalAppDateFormatter.current
    val lazyPagingItems = viewModel.pagedList.collectAsLazyPagingItems()
    LazyColumn(modifier = Modifier.fillMaxSize(),content = {
        itemsCustom(
            items = lazyPagingItems
        ){item->
            if(item != null){
                MontoRetenidoItem(item = item,
                    navigate = {
                        when(item.type_entity){
//                            TypeEntity.RESERVA.ordinal -> navigateToReserva(item.id_entity)
                            TypeEntity.SALA.ordinal -> navigateToSala(item.parent_id.toLong())
                            TypeEntity.SALA_COMPLETE.ordinal -> navigateToSalaComplete(item.parent_id.toLong())
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
internal fun MontoRetenidoItem(
    item: MontoRetenidoDto,
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