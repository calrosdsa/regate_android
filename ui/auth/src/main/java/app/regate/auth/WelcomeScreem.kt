package app.regate.auth

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias Welcome = @Composable (
    navigateToLogin:() -> Unit,
) -> Unit
@Inject
@Composable
fun Welcome(
    @Assisted navigateToLogin:()->Unit,
    modifier: Modifier = Modifier,
){
    Column(modifier = modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally) {
//        Image(painter = painterResource(id = Res.mipmap.ic_launcher), contentDescription = "Logo",
//        modifier = Modifier.size(40.dp))
        val backgroundColor = listOf(Color(0xFF2078EE), Color(0xFF74E6FE))
        val sunColor = listOf(Color(0xFFFFC200), Color(0xFFFFE100))
        Canvas(
            modifier = Modifier
//                .size(100.dp)
                .fillMaxWidth()
                .height(400.dp)
//                .padding(16.dp)
        ) {
            val width = size.width
            val height = size.height
            val path = Path().apply {
                moveTo(width.times(0f), height.times(0f))
                lineTo(size.width * 0f, size.height * 0.4f)
//                drawOval(color = Color.Black)
//                lineTo(size.width * 1f, size.height * 0.5f)
//                lineTo(size.width * 1f, size.height * 0f)

                cubicTo(
                    width.times(0f),
                    height.times(0.7f),
                    width.times(1f),
                    height.times(0.7f),
                    width.times(1f),
                    height.times(1.4f)
                )
                lineTo(size.width * 1f, size.height * 0f)
                close()
            }


            drawPath(path = path, color = Color.Blue.copy(alpha = .90f))
        }
        Canvas(
            modifier = Modifier
                .size(100.dp)
                .offset(y = (-150).dp)
//                .padding(16.dp)
        ) {
            val width = size.width
            val height = size.height
            val path = Path().apply {
                moveTo(width.times(.76f), height.times(.72f))
                cubicTo(
                    width.times(.93f),
                    height.times(.72f),
                    width.times(.98f),
                    height.times(.41f),
                    width.times(.76f),
                    height.times(.40f)
                )
                cubicTo(
                    width.times(.75f),
                    height.times(.21f),
                    width.times(.35f),
                    height.times(.21f),
                    width.times(.38f),
                    height.times(.50f)
                )
                cubicTo(
                    width.times(.25f),
                    height.times(.50f),
                    width.times(.20f),
                    height.times(.69f),
                    width.times(.41f),
                    height.times(.72f)
                )
                close()
            }
            drawRoundRect(
                brush = Brush.verticalGradient(backgroundColor),
                cornerRadius = CornerRadius(50f, 50f),

                )
            drawCircle(
                brush = Brush.verticalGradient(sunColor),
                radius = width.times(.17f),
                center = Offset(width.times(.35f), height.times(.35f))
            )
            drawPath(path = path, color = Color.White.copy(alpha = .90f))
        }
        Column(modifier = Modifier.offset(y=(-60).dp), horizontalAlignment = Alignment.CenterHorizontally) {

//        Spacer(modifier = Modifier.height(60.dp))
        Text(text = "LeaftBoard", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "A platform built for  a new way of work")
        Spacer(modifier = Modifier.height(70.dp))
        Button(onClick = { navigateToLogin() },
            modifier = Modifier
                .clip(shape = CircleShape)
                .height(45.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
               Text(text = "Get Started for free")
                Icon(imageVector =Icons.Default.KeyboardArrowRight, contentDescription = "GetStartedArrow")
            }
        }
    }
        }
    }
