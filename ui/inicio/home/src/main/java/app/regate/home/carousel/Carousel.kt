package app.regate.home.carousel

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import app.regate.common.compose.util.carouselTransition
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import app.regate.common.resources.R

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeMediaCarousel (
    list: List<String>,
//    totalItemsToShow: Int = 10,
//    carouselLabel: String = "Carousel Label",
    pagerState: PagerState = rememberPagerState(),
    autoScrollDuration: Long = Constants.CAROUSEL_AUTO_SCROLL_TIMER,
    onItemClicked: (String) -> Unit
) {
//    val pageCount = list.itemCount.coerceAtMost(totalItemsToShow)
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()
    if (isDragged.not()) {
        with(pagerState) {
            if (list.isNotEmpty()) {
                var currentPageKey by remember { mutableStateOf(0) }
                LaunchedEffect(key1 = currentPageKey) {
                    launch {
                        delay(timeMillis = autoScrollDuration)
                        if(currentPageKey == list.size){
                            scrollToPage(0)
                            currentPageKey = 0
                            return@launch
                        }
                        val nextPage = (currentPage + 1)
                        animateScrollToPage(page = nextPage)
                        currentPageKey = nextPage
                    }
                }
            }
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box {
            HorizontalPager(
                pageCount =list.size,
                state = pagerState,
                contentPadding = PaddingValues(
                    horizontal = 10.dp
                ),
                pageSpacing = 10.dp
            ) { page ->
                val item: String = list[page]
                Card(
                    onClick = { onItemClicked(item) },
                    modifier = Modifier
                        .carouselTransition(page, pagerState)
                        .height(80.dp)
                        .fillMaxWidth()
                ) {
                    CarouselItem(item)
                }
            }
            DotIndicators(
                pageCount = list.size,
                pagerState = pagerState,
                modifier = Modifier.align(Alignment.BottomCenter).padding(5.dp)
            )
        }
    }
}

@Composable
fun CarouselItem(item:String) {
    Box {
        AsyncImage(
            model = item,
            contentDescription = null,
//            placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
            error = painterResource(id = R.drawable.map_background),
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxSize()
        )
//        val gradient =
//            Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)))

//        Text(
//            text = item,
//            color = Color.White,
//            maxLines = 2,
//            overflow = TextOverflow.Ellipsis,
//            modifier = Modifier
//                .fillMaxWidth()
//                .align(Alignment.BottomCenter)
//                .background(gradient)
//                .padding(
//                    horizontal = 10.dp,
//                ),
//            style = MaterialTheme.typography.labelMedium
//        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DotIndicators(
    pagerState: PagerState,
    pageCount:Int,
    modifier: Modifier,
) {
    Row(modifier = modifier) {
        repeat(pageCount) { iteration ->
            val color = if (pagerState.currentPage == iteration) Color.White else Color.LightGray
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(10.dp)
                    .background(color)
            )
            Spacer(modifier = Modifier.width(6.dp))
        }
    }
}


object Constants {
    const val NONE: String = "none"
    const val TMDB_BASE_URL = "https://api.themoviedb.org/3/"
    const val TMDB_POSTER_PATH_URL = "https://image.tmdb.org/t/p/w500/"
    const val ITEM_LOAD_PER_PAGE: Int = 10
    const val MEDIA_TYPE_MOVIE = "MOVIES"
    const val MEDIA_TYPE_TV_SHOW = "TV"
    const val POINT_SEPARATOR: String = "•"
    const val CAROUSEL_AUTO_SCROLL_TIMER: Long = 2000L
}

data class HomeMediaItemUI(
    val id: Int,
    val name: String,
    val posterPath: String,
    val backdropPath: String,
    val overview: String
)