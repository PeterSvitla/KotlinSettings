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

class FileQualitySettingsFragment : Fragment() {
    private val model: SettingViewModel by viewModels()
    private lateinit var viewFragment: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewFragment = inflater.inflate(R.layout.settings_quality, container, false)
        initUi()
        return viewFragment
    }

    private fun initUi() {

        val model = Gson().fromJson(
            activity?.getPreferences(AppCompatActivity.MODE_PRIVATE)?.getString(
                resources.getString(R.string.key_quality), null
            ), TextDataModel::class.java
        )

        Log.i("Test", "Model: $model")
        if (model == null) {
            Log.i("Test", "Default case ")
            //default first open state
            viewFragment.findViewById<CustomRadioGroup>(R.id.radioGroup)?.check(R.id.option_high)
        } else {
            Log.i("Test", "Stored case: " + model.setting_id)
            when (model.setting_id) {
                0 -> {
                    Log.i("Test", "RadioGroup0: " + view?.findViewById<CustomRadioGroup>(R.id.radioGroup))
                    viewFragment?.findViewById<CustomRadioGroup>(R.id.radioGroup)?.check(R.id.option_full)
                }
                1 -> {
                    Log.i("Test", "RadioGroup1: " + view?.findViewById<CustomRadioGroup>(R.id.radioGroup))
                    viewFragment?.findViewById<CustomRadioGroup>(R.id.radioGroup)?.check(R.id.option_high)
                }
                2 -> {
                    Log.i("Test", "RadioGroup2: " + view?.findViewById<CustomRadioGroup>(R.id.radioGroup))
                    viewFragment?.findViewById<CustomRadioGroup>(R.id.radioGroup)?.check(R.id.option_medium)
                }
            }
        }
    }
}
