package com.tufelmalik.tourapp

data class Users(
    var userID : String?,
    var name : String?,
    var email : String?,
    var pass : String?,

){

    fun userAuth(userID: String?,name: String?,email: String?, pass: String?){
        this.userID = userID
        this.name = name
        this.email = email
        this.pass = pass
    }

    constructor(): this("","","","")
}