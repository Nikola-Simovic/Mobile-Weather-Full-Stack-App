package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ClickCounter()
{
    var counter by remember{ mutableIntStateOf(0) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Header("Click counter")
        Spacer(modifier = Modifier.height(200.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.Blue)
                .height(55.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Text(text = "Click count: $counter", fontSize = 40.sp,)
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(onClick = { counter-- }, modifier = Modifier.fillMaxWidth(0.5f).height(60.dp))
            {
                Text("-",fontSize = 35.sp)
            }
            Button(onClick = { counter++ }, modifier = Modifier.fillMaxWidth().height(60.dp))  {
                Text("+",fontSize = 35.sp)
            }
        }
    }
}