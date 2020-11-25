package com.example.juskask2.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class QuestionsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "No question has been selected"
    }
    val text: LiveData<String> = _text
}