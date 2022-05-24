package com.example.jettip


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource


import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jettip.ui.theme.JetTipTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetTipTheme {

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val total = remember {
                            mutableStateOf(0.00)
                        }
                        TopHeader(total = total.value)
                        MainContent() { newValue ->
                            total.value = newValue
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun TopHeader(total: Double) {
    Card(
        modifier = Modifier
            .height(150.dp)
            .width(350.dp)
            .padding(12.dp),
        shape = RoundedCornerShape(
            corner = CornerSize(15.dp),
        ), backgroundColor = androidx.compose.ui.graphics.Color.Magenta
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Total Per Person", fontSize = 20.sp)
            Text(
                text = "$" + Math.round(total * 100.0) / 100.0,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun MainContent(result: Double = 0.0, Exe: (Double) -> Unit) {
    val total = remember {
        mutableStateOf(0.0)
    }
    var sliderPosition by remember { mutableStateOf(0f) }
    Card(
        modifier = Modifier
            .wrapContentHeight()
            .width(350.dp)
            .padding(10.dp),
        shape = RoundedCornerShape(corner = CornerSize(15.dp)),
        border = BorderStroke(0.5.dp, androidx.compose.ui.graphics.Color.LightGray)
    ) {
        Column(
            verticalArrangement = Arrangement.Top
        ) {
            val status = remember {
                mutableStateOf(false)
            }
            var text by rememberSaveable { mutableStateOf("") }
            Card(
                modifier = Modifier
                    .height(100.dp)
                    .width(330.dp)
                    .padding(10.dp),
                shape = RoundedCornerShape(corner = CornerSize(15.dp)),
                elevation = 4.dp,
                border = BorderStroke(0.5.dp, androidx.compose.ui.graphics.Color.Magenta)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(text = "$", fontSize = 25.sp)

                    BasicTextField(
                        value = text,
                        onValueChange = { value ->
                            text = value.filter { it.isDigit() }
                            if (text != "")
                                status.value = true
                            else {
                                status.value = false
                                Exe(0.0)
                                total.value = 0.0
                            }

                        },
                        textStyle = LocalTextStyle.current.copy(
                            color = MaterialTheme.colors.onSurface,
                            fontSize = 25.sp
                        ),
                        modifier = Modifier.padding(20.dp, 0.dp, 0.dp, 0.dp),
                        decorationBox = { innerTextField ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(Modifier.weight(1f)) {
                                    if (text.isEmpty()) Text(
                                        "Enter Bill",
                                        style = LocalTextStyle.current.copy(
                                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f),
                                            fontSize = 25.sp
                                        )
                                    )
                                    innerTextField()
                                }
                            }
                        }
                    )
                }
            }
            if (status.value) {

                Column(
                    modifier = Modifier.padding(5.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val counter = remember {
                            mutableStateOf(0)
                        }
                        Text(text = "Split", fontSize = 25.sp)
                        Spacer(modifier = Modifier.padding(50.dp))
                        Card(
                            modifier = Modifier
                                .size(50.dp)
                                .clickable {
                                    if (counter.value > 0) {
                                        counter.value -= 1
                                        if (counter.value != 0) {
                                            Exe(
                                                text.toDouble() / counter.value + Math.round(
                                                    sliderPosition.toInt() * text.toInt() / 100 * 100.0
                                                ) / 100.0
                                            )
                                            total.value =
                                                text.toDouble() / counter.value
                                        } else {
                                            Exe(result)
                                            total.value = 0.0
                                        }
                                    }
                                },
                            shape = CircleShape,
                            elevation = 4.dp
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = "-", fontSize = 25.sp)
                            }
                        }
                        Spacer(modifier = Modifier.padding(10.dp))
                        Text(text = "${counter.value}", fontSize = 25.sp)
                        Spacer(modifier = Modifier.padding(10.dp))
                        Card(
                            modifier = Modifier
                                .size(50.dp)
                                .clickable {
                                    counter.value += 1
                                    Exe(
                                        result + text.toDouble() / counter.value + Math.round(
                                            sliderPosition.toInt() * text.toInt() / 100 * 100.0
                                        ) / 100.0
                                    )
                                    total.value =
                                        text.toDouble() / counter.value
                                },
                            shape = CircleShape,
                            elevation = 7.dp
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = "+", fontSize = 25.sp)
                            }
                        }

                    }

                    Row(
                        modifier = Modifier.padding(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(text = "Tip", fontSize = 25.sp)
                        Spacer(modifier = Modifier.padding(50.dp))
                        Text(
                            text = "$" + Math.round(sliderPosition.toInt() * total.value / 100 * 100.0) / 100.0,
                            fontSize = 25.sp
                        )
                    }

                    Column(
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = sliderPosition.toInt().toString() + "%")
                    }

                    //SliderComposable

                    Column() {
                        Slider(
                            value = sliderPosition,
                            onValueChange = {
                                sliderPosition = it
                                Exe(total.value + Math.round(sliderPosition.toInt() * total.value / 100 * 100.0) / 100.0)
                                            },
                            valueRange = 0f..100f,
                            onValueChangeFinished = {

                            },
                            steps = 4,
                            colors = SliderDefaults.colors(
                                thumbColor = MaterialTheme.colors.secondary,
                                activeTrackColor = MaterialTheme.colors.secondary
                            )
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                    }

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetTipTheme {
        Greeting("Android")
    }
}