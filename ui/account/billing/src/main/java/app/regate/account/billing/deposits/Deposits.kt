package app.regate.account.billing.deposits

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import app.regate.common.composes.ui.Loader
import app.regate.common.composes.util.itemsCustom
import app.regate.common.composes.viewModel
import app.regate.data.dto.account.billing.DepositDto
import me.tatarka.inject.annotations.Inject
import app.regate.common.resources.R

typealias Deposits = @Composable (
 ) ->Unit


@Inject
@Composable
fun Deposits(
    viewModelFactory:()->DepositsViewModel
){
    Deposits(viewModel = viewModel(factory = viewModelFactory) )
}


@Composable
internal fun Deposits(
    viewModel:DepositsViewModel
){

    val lazyPagingItems = viewModel.pagedList.collectAsLazyPagingItems()
    LazyColumn(modifier = Modifier.fillMaxSize(),content = {
        itemsCustom(
            items = lazyPagingItems
        ){item->
            if(item != null){
                DepositItem(item = item)
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
internal fun DepositItem(
    item:DepositDto
){
    Column(modifier = Modifier.padding(vertical = 10.dp)) {
        Text(text = item.gloss,modifier = Modifier,style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary)
        Text(text = stringResource(id = R.string.amount , item.amount.toString()),modifier = Modifier,style = MaterialTheme.typography.labelMedium)
        Text(text = "Fecha: ${item.transaction_datetime}",modifier = Modifier,style = MaterialTheme.typography.labelMedium)
    }
}