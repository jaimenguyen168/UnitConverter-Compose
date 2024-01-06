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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.unitconverter.ui.theme.UnitConverterTheme
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import kotlin.math.round

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
    val inConversionFactor = remember { mutableDoubleStateOf(1.00) }
    val outConversionFactor = remember { mutableDoubleStateOf(1.00) }
    var outUnit by remember { mutableStateOf("Meters") }

    val outputValue = remember(
        inputValue,
        inConversionFactor,
        outConversionFactor
    ) {
        mutableDoubleStateOf(0.0)
    }.run {
        convertUnit(inputValue = inputValue, conversion1 = inConversionFactor, conversion2 = outConversionFactor)
    }

    val customHeadline = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontSize = 25.sp,
        color = Color.Magenta
    )

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Unit Converter",
            style = customHeadline
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = inputValue, onValueChange = {
            inputValue = it
        },
            label = { Text("Enter value") }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row {
            DropdownButton {
                inConversionFactor.doubleValue = it.conversionFactor
            }
            Spacer(modifier = Modifier.width(16.dp))
            DropdownButton {
                outConversionFactor.doubleValue = it.conversionFactor
                outUnit = it.unit
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Result: ${"%.2f".format(outputValue)} $outUnit",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Composable
private fun convertUnit(inputValue: String, conversion1: MutableState<Double>, conversion2: MutableState<Double>) : Double {
    val inputValueAsDouble = inputValue.toDoubleOrNull() ?: 0.0
    return round(inputValueAsDouble * conversion1.value * 100 / conversion2.value) / 100
}

@Composable
fun DropdownButton(onClick: (LengthUnit) -> Unit) {

    var isExpanded by remember { mutableStateOf(false) }
    var selectedUnit by remember { mutableStateOf(LengthUnit.METER) }

    Box {
        Button(onClick = { isExpanded = true }) {
            Text(selectedUnit.unit)
            Spacer(modifier = Modifier.width(5.dp))
            Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown Arrow")
        }
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            LengthUnit.entries.forEach { dropdownItem ->
                DropdownMenuItem(
                    text = { Text(dropdownItem.unit) },
                    onClick = {
                        isExpanded = false
                        selectedUnit = dropdownItem
                        onClick(dropdownItem)
                    }
                )
            }
        }
    }
}

enum class LengthUnit(val conversionFactor: Double, val unit: String) {
    METER(1.0, "Meters"),
    CENTIMETER(0.01, "Centimeters"),
    FEET(0.3048, "Feet"),
    MILE(1609.34, "Miles")
}
