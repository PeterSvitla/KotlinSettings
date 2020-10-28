package com.smartvid.settingsapplication.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.gson.Gson
import com.smartvid.settingsapplication.R
import com.smartvid.settingsapplication.model.TextDataModel
import com.smartvid.settingsapplication.viewmodel.SettingViewModel

class FileUploadSettingsFragment : Fragment() {
    private val model: SettingViewModel by viewModels()
    private lateinit var viewFragment: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewFragment = inflater.inflate(R.layout.settings_type, container, false)
        initUi()
        return view
    }

    private fun initUi() {

        val model = Gson().fromJson(
            activity?.getPreferences(AppCompatActivity.MODE_PRIVATE)?.getString(
                resources.getString(R.string.key_upload), null
            ), TextDataModel::class.java
        )

        Log.i("Test", "Model: $model")
        if (model == null) {
            Log.i("Test", "Default case ")
            //default first open state
            view?.findViewById<CustomRadioGroup>(R.id.type_group)?.check(R.id.option_wifi)
        } else {
            Log.i("Test", "Stored case: " + model.setting_id)
            when(model.setting_id){
                0 -> {
                    viewFragment?.findViewById<CustomRadioGroup>(R.id.type_group)?.check(R.id.option_wifi)
                }
                1 -> {
                    viewFragment?.findViewById<CustomRadioGroup>(R.id.type_group)?.check(R.id.option_wifi_cellular)
                }
            }
        }
    }
}
