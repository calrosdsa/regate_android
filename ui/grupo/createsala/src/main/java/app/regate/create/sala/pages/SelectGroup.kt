package app.regate.create.sala.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.regate.common.composes.ui.Loader
import app.regate.common.composes.ui.PosterCardImage
import app.regate.data.dto.empresa.grupo.GrupoDto
import app.regate.common.resources.R

@Composable
internal fun SelectGroup(
    grupos:List<GrupoDto>,
    selectedGroupId:Long?,
    loading:Boolean,
    selectGroup:(Long)->Unit,
    navigateToCreateGroup:() -> Unit,
    modifier :Modifier = Modifier,
){
    
    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            Text(text = stringResource(id = R.string.select_group_text),style = MaterialTheme.typography.titleMedium,
            modifier =  Modifier.padding(10.dp))
        }
        items(
            items = grupos
        ){item->
            SelectGroupItem(
                item = item,
                selectedGroupId = selectedGroupId,
                selectGroup = selectGroup)
        }
        if(loading){
        item{
            Box(modifier = Modifier.fillMaxSize()){
                Loader(modifier = Modifier.align(Alignment.Center))
            }
        }
        }else{
            if(grupos.isEmpty()){
            item{
                Box(modifier = Modifier.fillMaxSize()){
                    Button(onClick = { navigateToCreateGroup() },modifier = Modifier.align(Alignment.Center)) {
                        Text(text = stringResource(id = R.string.create_group))
                    }
//                    Text(text = "No hay grupos",modifier = Modifier.align(Alignment.Center))
                }
            }
            }
        }

    }
}


@Composable
internal fun SelectGroupItem(
    item:GrupoDto,
    selectedGroupId:Long?,
    selectGroup:(Long)->Unit
){
    Row(modifier = Modifier
        .clickable {
            selectGroup(item.id)
        }
        .fillMaxWidth()
        .padding(8.dp)
        ,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            PosterCardImage(model = item.photo,modifier = Modifier.size(50.dp),
            shape = CircleShape)
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = item.name,style = MaterialTheme.typography.labelLarge)
        }

        Checkbox(checked = item.id == selectedGroupId, onCheckedChange = {selectGroup(item.id)})
    }
}