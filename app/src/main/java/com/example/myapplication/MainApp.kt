package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.content.Context
import android.widget.Toast

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi


@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalPagerApi::class
)
@Composable
fun MainApp(navController: NavController) {

    // Use the correct overload of rememberPagerState
    // Pass a lambda that returns the number of pages for the pager
    val pagerState = rememberPagerState {
        2 // Number of pages in your pager (screen1 and screen2)
    }

    val context=LocalContext.current

    val currentPage = remember { mutableStateOf(0) }
    currentPage.value = pagerState.currentPage




    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        "Tampere",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.Black,
                        fontSize = 26.sp
                    )
                },
                navigationIcon = {
                    // Conditionally include the navigation icon only when on the second page
                    if (currentPage.value == 1) {
                        IconButton(onClick = {
                            // Animate the pager to scroll to page 0
                            navController.navigate("home")
                        }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                },
                actions = {
                    // State variable to manage the expanded state of the dropdown menu
                    var expanded by remember { mutableStateOf(false) }

                    // Icon button that opens the dropdown menu
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Main Menu"
                        )
                    }

                    // Dropdown menu
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            onClick = {

                                expanded = false
                            },
                            text = { Text("Open Map") }
                        )
                        DropdownMenuItem(
                            onClick = {
                                val url = "https://openweathermap.org/api"
                                val intent = Intent(Intent.ACTION_VIEW)
                                intent.data = Uri.parse(url)
                                if (intent.resolveActivity(context.packageManager) != null) {
                                    context.startActivity(intent)
                                }
                                else {
                                    Toast.makeText(context, "No application found to handle the intent", Toast.LENGTH_SHORT).show()
                                }
                                expanded = false

                            },
                            text = { Text("Our source") }
                        )
                        DropdownMenuItem(
                            onClick = {

                                navController.navigate("about")
                                expanded = false
                            },
                            text = { Text("About") }
                        )
                    }
                }



            )
        }
        ,
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left-side icon
                    IconButton(onClick = { /* Handle left icon action */ }) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = "Left icon"
                        )
                    }

                    // Center: Indicator Dots
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(2) { index ->
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(
                                        color = if (currentPage.value == index) Color.Black else Color.Gray,
                                        shape = CircleShape
                                    )
                            )
                            if (index == 0) {
                                Spacer(modifier = Modifier.width(10.dp))
                            }
                        }
                    }

                    // Right-side Floating Action Button
                    FloatingActionButton(
                        onClick = {
                            val url = "https://openweathermap.org/api"
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse(url)
                            if (intent.resolveActivity(context.packageManager) != null) {
                                context.startActivity(intent)
                             }
                            else {
                                 Toast.makeText(context, "No application found to handle the intent", Toast.LENGTH_SHORT).show()
                            }
                                  },
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.chain_link_icon),
                            contentDescription = "FAB icon",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

        },
        content = { innerPadding ->
            val pagerState = rememberPagerState { 2 }
            // Use HorizontalPager to handle swipe navigation
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) { page ->
                // Determine the current page and display content accordingly
                currentPage.value = page
                when (page) {
                    0 -> CurrentWeatherScreenTEST(innerPadding)
                    1 -> WeatherForecastScreenTEST(innerPadding)
                }
            }
        }
    )
}


