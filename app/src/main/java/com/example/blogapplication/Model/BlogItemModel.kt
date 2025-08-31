package com.example.blogapplication.Model

data class BlogItemModel(
    val heading : String,
    val userName : String,
    val date : String,
    val post : String,
    val likecounts : Int,
    val imageUrl : String,
)
