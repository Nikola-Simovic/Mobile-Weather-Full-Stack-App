package com.example.myapplication

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument


@Composable
fun Navigation() {
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



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagingTest(navController: NavController) {
    val pagerState = rememberPagerState {
        2  //  number of pages
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            when (page) {
                0 -> Text("PagingTest1")//CenterAlignedTopAppBarExample(currentPage = pagerState.currentPage)
                1 -> Text("PagingTest2")//CenterAlignedTopAppBarExample(currentPage = pagerState.currentPage)
            }
        }

        //  indicator dots at the bottom
        //IndicatorDots(currentPage = pagerState.currentPage)
    }
}

@Composable
fun IndicatorDots(currentPage: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),  //  vertical padding for space from content
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(2) { index ->
            Box(
                modifier = Modifier
                    .size(12.dp)  //
                    .background(
                        color = if (currentPage == index) Color.Black else Color.Gray,
                        shape = CircleShape
                    )
            )
            if (index == 0) {
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
    }
}

