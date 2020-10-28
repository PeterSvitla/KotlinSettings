package com.smartvid.settingsapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smartvid.settingsapplication.model.TextDataModel

class SettingViewModel : ViewModel() {

    val currentUploadKind: MutableLiveData<TextDataModel> by lazy {
        MutableLiveData<TextDataModel>()
    }

    val currentUploadQuality: MutableLiveData<TextDataModel> by lazy {
        MutableLiveData<TextDataModel>()
    }
}