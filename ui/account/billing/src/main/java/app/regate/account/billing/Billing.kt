package app.regate.account.billing

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.regate.common.composes.ui.Loader
import app.regate.common.composes.ui.SimpleTopBar
import app.regate.common.composes.viewModel
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import app.regate.common.resources.R

typealias Billing= @Composable (
    navigateUp: () -> Unit,
    navigateToRecargaCoins:()->Unit,
    deposits: @Composable () -> Unit,
    consume: @Composable () -> Unit
) -> Unit

@Inject
@Composable
fun Billing(
    viewModelFactory:()->BillingViewModel,
    @Assisted navigateUp: () -> Unit,
    @Assisted navigateToRecargaCoins: () -> Unit,
    @Assisted deposits:@Composable () -> Unit,
    @Assisted consume:@Composable () -> Unit
){
    Billing(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navigateUp,
        navigateToRecargaCoins = navigateToRecargaCoins,
        deposits = deposits,
        consume = consume
    )
}

@Composable
internal fun Billing(
    viewModel: BillingViewModel,
    navigateUp: () -> Unit,
    navigateToRecargaCoins: () -> Unit,
    deposits:@Composable () -> Unit,
    consume:@Composable () -> Unit
){
    val state by viewModel.state.collectAsState()
    Billing(
        viewState = state,
        navigateUp = navigateUp,
        navigateToRecargaCoins = navigateToRecargaCoins,
        deposits = deposits,
        consume  = consume
    )

}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun Billing(
    viewState:BillingState,
    navigateUp: () -> Unit,
    navigateToRecargaCoins: () -> Unit,
    deposits:@Composable () -> Unit,
    consume:@Composable () ->Unit,
) {
    val pagerState = rememberPagerState()
    val coroutine = rememberCoroutineScope()
    Scaffold(topBar = {
        SimpleTopBar(navigateUp = navigateUp)
    }) { paddingValues ->
        Box(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()) {
            if (viewState.loading) {
                Loader()
            } else {
                Column(
                    modifier = Modifier
                        .padding(10.dp),
                ) {
                    viewState.balance?.let { balance ->
                        Surface(
                            color = MaterialTheme.colorScheme.inverseOnSurface,
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Image(
                                        painter = painterResource(id = R.drawable.coin),
                                        contentDescription = null,
                                        modifier = Modifier.size(25.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column() {
                                        Text(
                                            text = stringResource(id = R.string.balance_coins),
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                        Text(
                                            text = balance.coins.toString(),
                                            style = MaterialTheme.typography.labelLarge
                                        )
                                    }
                                }
                                Button(onClick = { navigateToRecargaCoins() }) {
                                    Text(text = stringResource(id = R.string.purchase))
                                }
                            }
                        }
                    }
                    Text(
                        text = stringResource(id = R.string.history),
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(top = 10.dp, bottom =2.dp)
                    )
                    Indicators(navToTab = { coroutine.launch { pagerState.animateScrollToPage(it) } },
                        currentTab = pagerState.currentPage)
                    HorizontalPager(
                        pageCount = 2,
                        state = pagerState
                    ) { page ->
                        when (page) {
                            0 -> deposits()
                            1 -> consume()
                        }
                    }
                }
            }
        }
    }
}


@Composable
internal fun Indicators(
    navToTab:(tab:Int) ->Unit,
    currentTab:Int,
){
    ScrollableTabRow(selectedTabIndex = currentTab, edgePadding = 1.dp) {
        Tab(
            text = { Text(text = stringResource(id = R.string.deposits), style = MaterialTheme.typography.labelMedium) },
            selected = currentTab == 0,
            onClick = {
                navToTab(0)
            },
        )
        Tab(
            text = { Text(text = stringResource(id = R.string.consume),style = MaterialTheme.typography.labelMedium) },
            selected = currentTab ==1,
            onClick = {
                // Animate to the selected page when clicked
                navToTab(1)
            },
        )
    }
}