package com.exsite.meapmeap

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val _topBarTitle = mutableStateOf("Home")
    val topBarTitle: State<String> = _topBarTitle

    fun updateTopBarTitle(title: String) {
        _topBarTitle.value = title
    }
}