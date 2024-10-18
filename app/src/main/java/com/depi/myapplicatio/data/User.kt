package com.depi.myapplicatio.data

data class User(
    val firstName: String,
    val lastName: String,
    val email: String,
    val imagePath: String = " "   // It is used when user upload image profile
){
    constructor(): this("" , "" , "" , "")
}
