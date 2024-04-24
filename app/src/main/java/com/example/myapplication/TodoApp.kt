package com.example.myapplication

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class  TodoItem(
    val userId: Int,
    val id: Int,
    val title: String,
    val completed: Boolean,
)

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
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
    }
    val apiService : ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

@Composable
fun TodoApp()
{
    //var todoItem by remember{ mutableStateOf<TodoItem>(TodoItem(1,100,"Loading",false))}
    var todoItemList by remember{ mutableStateOf<List<TodoItem>>(listOf()) }
    var checked by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit){
        todoItemList = RetrofitInstance.apiService.fetchTodos()
        isLoading=false
    }

    if (isLoading) {
        CircularProgressIndicator()
    }
    else {
        LazyColumn {
            items(items = todoItemList){
                todoItem -> Text(todoItem.title)
            }
        }
    }

}