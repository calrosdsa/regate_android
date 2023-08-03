package app.regate.welcome

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.regate.common.composes.viewModel
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import app.regate.common.resources.R
import app.regate.welcome.categories.Categories
import app.regate.welcome.started.Started
import kotlinx.coroutines.launch

typealias Welcome = @Composable (
    navigateToHome:() -> Unit,
) -> Unit
@Inject
@Composable
fun Welcome(
    viewModelFactory : () ->WelcomeViewmModel,
    @Assisted navigateToHome: () -> Unit,
) {
    Welcome(
        viewModel = viewModel(factory = viewModelFactory),
        navigateToHome = navigateToHome
    )
}

@Composable
fun Welcome(
    viewModel: WelcomeViewmModel,
    navigateToHome: () -> Unit,
){
    val state by viewModel.state.collectAsState()
    Welcome(
        viewState = state,
        navigateToHome = {
            navigateToHome()
            viewModel.saveCategories()
        },
        addCategory = viewModel::addCategory
    )
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun Welcome(
    viewState:WelcomeState,
    addCategory:(Long)->Unit,
    navigateToHome: () -> Unit
){
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    BackHandler(true) {
        when(pagerState.currentPage){
            1 -> coroutineScope.launch { pagerState.animateScrollToPage(0) }
        }
    }
    HorizontalPager(
        state = pagerState,
        userScrollEnabled = false,
        pageCount = 2) {page->
        when(page){
            0 -> Started(navigateToPage = { coroutineScope.launch { pagerState.animateScrollToPage(it) } })
            1 -> Categories(
                navigateToHome = navigateToHome,
                categories = viewState.categories,
                selectedCategories = viewState.selectedCategories,
                addCategory = addCategory
            )
        }
    }
}
