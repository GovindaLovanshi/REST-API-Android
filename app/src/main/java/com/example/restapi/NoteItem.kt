package com.example.restapi

data class NoteItem(val title : String, val description : String, val noteId :String){
    constructor():this("","","")
}

