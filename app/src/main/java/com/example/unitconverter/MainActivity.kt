package com.example.unitconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableDoubleState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.unitconverter.ui.theme.UnitConverterTheme
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UnitConverterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UnitConverter()

                }
            }
        }
    }
}

@Composable
fun UnitConverter() {

    var inputValue by remember { mutableStateOf("") }
    var outputValue by remember { mutableStateOf("") }
    var inConversionFactor = remember { mutableStateOf(1.00) }
    var outConversionFactor = remember { mutableStateOf(1.00) }

    fun convertUnit() {
        val inputAsDouble = inputValue.toDoubleOrNull() ?: 0.0
        val result = (inputAsDouble * inConversionFactor.value * 100.0 / outConversionFactor.value).roundToInt() / 100
        outputValue = result.toString()
    }

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Unit Converter",
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = inputValue, onValueChange = {
            inputValue = it
            convertUnit()
        },
            label = { Text("Enter value") }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row {
            inConversionFactor = dropdownButton { convertUnit() }
            Spacer(modifier = Modifier.width(16.dp))
            outConversionFactor = dropdownButton { convertUnit() }
        }

        val unitTemp by remember {
            mutableStateOf(
                when (outConversionFactor.value) {
                    1.00 -> "Meters"
                    0.01 -> "Centimeters"
                    0.3048 -> "Feet"
                    1609.34 -> "Miles"
                    else -> ""
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Result: $outputValue $unitTemp",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Composable
fun dropdownButton(myLambda: () -> Unit): MutableState<Double> {

    var isExpanded by remember { mutableStateOf(false) }
    var inputUnit by remember { mutableStateOf("Meters") }
    val temp = remember { mutableStateOf(0.01) }

    Box {
        Button(onClick = { isExpanded = true }) {
            Text(inputUnit)
            Spacer(modifier = Modifier.width(5.dp))
            Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown Arrow")
        }
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = {
                isExpanded = false
        }) {
            DropdownMenuItem(text = { Text("Centimeters") }, onClick = {
                isExpanded = false
                inputUnit = "Centimeters"
                temp.value = 0.01
                myLambda.invoke()
            })
            DropdownMenuItem(text = { Text("Meters") }, onClick = {
                isExpanded = false
                inputUnit = "Meters"
                temp.value = 1.00
                myLambda.invoke()
            })
            DropdownMenuItem(text = { Text("Feet") }, onClick = {
                isExpanded = false
                inputUnit = "Feet"
                temp.value = 0.3048
                myLambda.invoke()
            })
            DropdownMenuItem(text = { Text("Miles") }, onClick = {
                isExpanded = false
                inputUnit = "Miles"
                temp.value = 1609.34
                myLambda.invoke()
            })
        }
    }

    return temp
}

interface ButtonClick {
    fun click()
}


//@Preview(showBackground = true)
//@Composable
//fun UnitConverterPreview() {
//    UnitConverter()
//}

//@Composable
//fun CaptainGame() {
//    var treasuresFound by remember { mutableIntStateOf(0) }
//    val directions = remember { mutableStateOf("North") }
//
//    Column {
//        Text("Treasures Found: $treasuresFound")
//        Text("Current Direction: ${directions.value}")
//        Button(onClick = {
//            directions.value = "East"
//            if (Random.nextBoolean()) treasuresFound += 1
//        }) {
//            Text("Sail East")
//        }
//        Button(onClick = {
//            directions.value = "West"
//            if (Random.nextBoolean()) treasuresFound += 1
//        }) {
//            Text("Sail West")
//        }
//        Button(onClick = {
//            directions.value = "North"
//            if (Random.nextBoolean()) treasuresFound += 1
//        }) {
//            Text("Sail North")
//        }
//        Button(onClick = {
//            directions.value = "South"
//            if (Random.nextBoolean()) treasuresFound += 1
//        }) {
//            Text("Sail South")
//        }
//    }
//}
