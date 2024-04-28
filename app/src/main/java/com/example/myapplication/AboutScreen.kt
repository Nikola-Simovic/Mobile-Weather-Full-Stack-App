package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(navController: NavController) {
    val context= LocalContext.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        text = "About",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            )
        },
        bottomBar = {
            // Define a BottomAppBar
            BottomAppBar {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    Button(
                        onClick = {
                            var recipient="nikola.simovic@tuni.fi"


                            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:$recipient")
                                putExtra(Intent.EXTRA_SUBJECT, "Amazing weather app")
                                putExtra(Intent.EXTRA_TEXT, "Wow,\n\n")
                            }
                            if (emailIntent.resolveActivity(context.packageManager) != null) {
                                context.startActivity(emailIntent)
                            } else {
                                Toast.makeText(context, "No email app found", Toast.LENGTH_SHORT).show()
                            }
                        },

                        modifier = Modifier.padding(16.dp).weight(1f)
                    ) {
                        Text("SEND EMAIL")
                    }
                    Button(
                        onClick = {
                            navController.navigate("home") {

                            }
                        },

                        modifier = Modifier.padding(16.dp).weight(1f)
                    ) {
                        Text("RETURN TO APP")
                    }
                }
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BasicText(
                    text = "Hey!\n\nI'm Nikola, a software engineering student AND the creator of this app!\n\nThis entire app was made by me" +
                            " and I truly hope your enjoying it!\n\nThe app is quite new so if there are any suggestions or bugs you'd like to report (or just want to" +
                            " send me an email talking about how much you like the app) you can do so by pressing the button at the bottom of the screen!" +
                            "\n\nOtherwise, I hope you have a great day!\n\nBR. The creator\n\nP.S. None of the images used were created by me, I'm just a simple programmer not a designer 😅" ,
                    style = MaterialTheme.typography.bodyLarge
                )

            }
        }
    )
}
