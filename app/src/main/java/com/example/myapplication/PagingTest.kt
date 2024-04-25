package com.example.myapplication

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/*
interface ApiService

{
    @GET("todos/1")
    suspend fun fetchTodo1(): TodoItem

    @GET("todos")
    suspend fun fetchTodos(): List<TodoItem>
}

object RetrofitInstance{
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    private val retrofit by lazy{
        Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
    val apiService : ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

--THE JSONPLACEHOLDER API SETTINGS--
*/
@Composable
fun TheApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home",  // start destination
        modifier = Modifier.fillMaxSize()
    ) {
        // Define composable destinations
        composable("home") {
            PagingTest(navController = navController)
        }
        composable("firstPage") {
            FirstPage()
        }
        composable("secondPage") {
            SecondPage()
        }
        //  more destinations if needed
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
                0 -> FirstPage()
                1 -> SecondPage()
            }
        }

        //  indicator dots at the bottom
        IndicatorDots(currentPage = pagerState.currentPage)
    }
}


@Composable
fun FirstPage() {
    //  the first page
    BasicText(text = "First Page Content", style = MaterialTheme.typography.bodyLarge)
}

@Composable
fun SecondPage() {
    //  the second page
    BasicText(text = "Second Page Content", style = MaterialTheme.typography.bodyLarge)
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
            }        }
    }
}

