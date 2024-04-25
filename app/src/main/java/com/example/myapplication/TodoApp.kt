package com.example.myapplication

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class  TodoItem(
    val userId: Int,
    val id: Int,
    val title: String,
    val completed: Boolean,
)


@Composable
fun TodoApp()
{
    var todoItemList by remember{ mutableStateOf<List<TodoItem>>(listOf()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit){
        todoItemList = RetrofitInstance.apiService.fetchTodos()
        isLoading=false
    }

    if (isLoading) {
        CircularProgressIndicator()
    }
    else {
        Column {
            Header("Todo List")
            Spacer(Modifier.height(50.dp))
            LazyColumn {
                items(items = todoItemList) { todoItem ->
                    Text(todoItem.title)
                }
            }
        }
    }
}