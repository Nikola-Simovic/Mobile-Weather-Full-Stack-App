package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(navController: NavController) {
    val context= LocalContext.current

    val isDarkTheme = isSystemInDarkTheme()      //in order to style the app better, a check of theme was done
                                                 //after which the colors are adjusted to match the desired mode
    val titleColor = if (isDarkTheme) Color.White else Color.Black
    val textColor = if (isDarkTheme) Color.White else MaterialTheme.colorScheme.onSurface
    var backgroundColor = Color(230, 239, 244, 100)


    val headerColor = if (isDarkTheme) Color(202, 231, 254, 150) else { MaterialTheme.colorScheme.primaryContainer}

    var buttonColorMode = if (isDarkTheme) MaterialTheme.colorScheme.secondaryContainer else { MaterialTheme.colorScheme.primaryContainer}


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = headerColor,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Box(
                        modifier = Modifier.fillMaxSize(),   //fills the entire space of the top app bar
                        contentAlignment = Alignment.Center  //centers the title both vertically and horizontally
                    ) {
                        Text(
                            text = stringResource(R.string.about),  //like in a lot of places, string resources are used for localization
                            fontSize = 35.sp,
                            fontWeight = FontWeight.Bold,
                            color = titleColor
                        )
                    }
                },
                modifier = Modifier.height(70.dp)
            )
        },
        bottomBar = {
            // Define a BottomAppBar
            BottomAppBar (containerColor =Color(202, 231, 254, 150)
            ){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    Button(
                        onClick = {
                            val recipient="nikola.simovic@tuni.fi"


                            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {  //code that runs an intent if the user wants to send an email
                                data = Uri.parse("mailto:$recipient")
                                putExtra(Intent.EXTRA_SUBJECT, "Amazing weather app")
                                putExtra(Intent.EXTRA_TEXT, "Wow,\n\n")
                            }
                            if (emailIntent.resolveActivity(context.packageManager) != null) {
                                context.startActivity(emailIntent)
                            } else {
                                Toast.makeText(context, "No email app found", Toast.LENGTH_SHORT).show()  //an error message if no email app is found
                            }
                        },
                        modifier = Modifier.padding(16.dp).weight(1f),
                        colors = ButtonDefaults.buttonColors(buttonColorMode),

                        ) {
                        Text(stringResource(R.string.send_email),color=titleColor)
                    }
                    Button(
                        onClick = {
                            navController.navigate("home") {  //jetpack navigation into the home page --- the navigation from about to
                                //the main page restarts the app , pulling up the data for Tampere again, so that testing can be done and we can easily compare
                                //location data without a need to manually restart the app (a choice made for testing and demonstration purposes only from the about section)
                            }
                        },

                        modifier = Modifier.padding(16.dp).weight(1f),
                        colors = ButtonDefaults.buttonColors(buttonColorMode),

                        ) {
                        Text(stringResource(R.string.return_to_app),color=titleColor)
                    }
                }
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Hey!\n\nI'm Nikola, a software engineering student AND the creator of this app!\n\nThis entire app was made by me" +
                            " and I truly hope your enjoying it!\n\nThe app is quite new so if there are any suggestions or bugs you'd like to report (or just want to" +
                            " send me an email talking about how much you like the app) you can do so by pressing the button at the bottom of the screen!" +
                            "\n\nOtherwise, I hope you have a great day!\n\nBR. The creator\n\nP.S. None of the images used were created by me, I'm just a simple programmer not a designer 😅" ,
                    style = MaterialTheme.typography.bodyLarge,
                    color = textColor
                )

            }
        }
    )
}
