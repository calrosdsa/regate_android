package app.regate.servicios

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import app.regate.common.composes.ui.BottomBar
import app.regate.common.composes.ui.TopBar
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import moe.tlaster.nestedscrollview.VerticalNestedScrollView
import moe.tlaster.nestedscrollview.rememberNestedScrollViewState


typealias Servicios = @Composable (
    navController: NavController,
//    navigateToReserva:(id:Long)->Unit,
//    navigateToSignUpScreen:() -> Unit,
) -> Unit

@OptIn(ExperimentalFoundationApi::class)
@Inject
@Composable
fun Servicios (
    @Assisted navController: NavController,
//    @Assisted navigateToReserva: (id:Long) -> Unit,
//    viewModelFactory:()->ReservasViewModel
){
    Scaffold(
//        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar()
        },
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) {paddingValue->
        Box(modifier = Modifier.padding(paddingValue)){
            val nestedScrollViewState = rememberNestedScrollViewState()
            VerticalNestedScrollView(
                state = nestedScrollViewState,
                header = {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth(),
                        color = MaterialTheme.colorScheme.background,
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(text = "This is some awesome title")
                        }
                    }
                },
                content = {
                    val pagerState = rememberPagerState()
                    val pages = (0..4).map { it }
                    Column {
                        TabRow(
                            selectedTabIndex = pagerState.currentPage,
                            indicator = { tabPositions ->
                                TabRowDefaults.Indicator(
                                    Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage])
                                )
                            }
                        ) {
                            pages.forEachIndexed { index, title ->
                                Tab(
                                    text = { Text(text = "tab $title") },
                                    selected = pagerState.currentPage == index,
                                    onClick = {}
                                )
                            }
                        }
                        HorizontalPager(
                            modifier = Modifier.weight(1f),
                            state = pagerState,
                            pageCount = 4
                        ) {
                            LazyColumn {
                                items(100) {
                                        Text(text = "item $it")
                                }
                            }
                        }
                    }
                }
            )

        }
    }
}