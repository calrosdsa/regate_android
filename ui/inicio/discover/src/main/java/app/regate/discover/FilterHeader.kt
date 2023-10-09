package app.regate.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import app.regate.common.composes.component.dialog.CategoryDialog
import app.regate.common.composes.component.images.AsyncImage
import app.regate.data.common.AddressDevice
import app.regate.common.resources.R
import app.regate.models.Labels
import java.time.LocalTime

@Composable
internal fun HeaderFilter(
    place: AddressDevice?,
    showTimeDialog:()->Unit,
    showDateDialog:()->Unit,
    showDialogInterval:()->Unit,
    date:String,
    navigateToFilter:()->Unit,
    categories:List<Labels>,
//    currentCategoryId:Long,
    currentTime:LocalTime,
    currentInterval:Long,
    setCategory: (Long) -> Unit,
    selectedCategory:Labels?,
    modifier:Modifier = Modifier
){
    val showCategoryDialog = remember{ mutableStateOf(false) }
    CategoryDialog(
        showDialog = showCategoryDialog.value,
        selectedCategory = selectedCategory,
        categories = categories,
        closeDialog = { showCategoryDialog.value = false },
        setCategory = setCategory
    )
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 10.dp),
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 10.dp,
        color = MaterialTheme.colorScheme.inverseOnSurface
//                    tonalElevation = 5.dp
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                place?.let {
                Surface(
                    modifier = Modifier, onClick = {},
                    shadowElevation = 5.dp,
                    shape = CircleShape
                ) {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 8.dp,horizontal = 12.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = "placeIcon",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = place.city ?: place.admin_area ?: place.country,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
                }
                Row() {
//                IconButton(onClick = { navigateToFilter() }) {
//                    Icon(
//                        imageVector = Icons.Default.FilterAlt,
//                        contentDescription = "filteralt",
//                        tint = MaterialTheme.colorScheme.primary
//                    )
//                }
                    IconButton(onClick = { navigateToFilter() }) {
                        Icon(
                            imageVector = Icons.Default.Sort,
                            contentDescription = "sorticon",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }


            Divider()
            LazyRow(modifier = Modifier.fillMaxWidth().height(50.dp)) {

                item {
                    Surface(
                        onClick = { showCategoryDialog.value = true },
                        modifier = Modifier,
                        color = MaterialTheme.colorScheme.inverseOnSurface
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(5.dp)
                                .size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "add-sport",
                                modifier = Modifier
                                    .zIndex(1f)
                                    .size(8.dp)
                                    .align(Alignment.TopEnd)
                            )
                            if (selectedCategory != null) {
                                AsyncImage(
                                    model = selectedCategory.thumbnail ?: "",
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(30.dp)
                                        .align(Alignment.Center),
                                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                                )
                            }
                        }
                    }
                }
                item {
                    Divider(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(1.dp)
                    )
                }

                item{
                Surface(onClick = { showDateDialog() }, modifier = Modifier,
                    color = MaterialTheme.colorScheme.inverseOnSurface
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Text(
                            text = "Date",
                            style = MaterialTheme.typography.labelSmall
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = date,
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                }
                }
                item {
                    Divider(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(1.dp)
                    )
                }
                item {
                    Surface(onClick = { showTimeDialog() }, modifier = Modifier,
                        color = MaterialTheme.colorScheme.inverseOnSurface
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp)
                        ) {
                            Text(
                                text = "Hora",
                                style = MaterialTheme.typography.labelSmall
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "$currentTime",
                                style = MaterialTheme.typography.labelMedium,
                            )
                        }
                    }
                }
                item {
                    Divider(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(1.dp)
                    )
                }
                item{
                    Surface(onClick = { showDialogInterval() }, modifier = Modifier,
                        color = MaterialTheme.colorScheme.inverseOnSurface
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.time_interval),
                                style = MaterialTheme.typography.labelSmall
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "$currentInterval min",
                                style = MaterialTheme.typography.labelMedium,
                            )
                        }
                    }
                }

            }
//            Divider()
//
//            LazyRow{
//                items(
//                    items = categories,
//                    key= {it.id}
//                ) { item ->
//                    InstalacionCategoryItem(
//                        item = item,
//                        isSelected  = currentCategoryId == item.id,
//                        setCategory = { setCategory(item.id) }
//                    )
//                }
//            }
        }
    }
}


@Composable
fun InstalacionCategoryItem(
    item: Labels,
    isSelected: Boolean,
    setCategory:()->Unit,
    modifier:Modifier = Modifier
) {
    Surface(
        onClick = {setCategory() },
//        shape = MaterialTheme.shapes.small,
//        color = MaterialTheme.colorScheme.background,
//        tonalElevation = 10.dp,
        modifier = modifier,
//        border = BorderStroke(width = 1.dp,color = MaterialTheme.colorScheme.inverseSurface)
//        contentColor = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = item.thumbnail,
                contentDescription = item.thumbnail,
                modifier = Modifier.size(30.dp),
                colorFilter = ColorFilter.tint(if(isSelected)MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground)
            )
            Text(text = item.name, style = MaterialTheme.typography.labelMedium,
            color = if(isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground)
           
        }
    }
}