package com.example.myapplication


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavigationLauncher() {  //Launches the navigation, nav controller and creates the screen routes
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = Modifier.fillMaxSize()
    ) {
        composable("home") {
            MainApp(navController)
        }
        composable("about") {
            AboutScreen(navController)
        }


    }
}
