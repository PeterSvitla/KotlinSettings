package com.smartvid.settingsapplication.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceGroup
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.smartvid.settingsapplication.R
import com.smartvid.settingsapplication.menu.menu_activity.SettingsActivity
import com.smartvid.settingsapplication.model.SETTING_KIND
import com.smartvid.settingsapplication.model.TextDataModel
import com.smartvid.settingsapplication.viewmodel.SettingViewModel

class MainPreferenceFragment(private var handleClicks: Boolean) : PreferenceFragmentCompat() {

    private val model: SettingViewModel by viewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        PreferenceManager.setDefaultValues(
            requireContext(), R.xml.root_preferences,
            false
        )
    }

    private fun updateUI(updatedSetting: TextDataModel?) {
        when (updatedSetting?.settingKind) {
            SETTING_KIND.QUALITY -> {
                Log.i("Update", "Quality UI setting should be updated $updatedSetting")
            }
            SETTING_KIND.UPLOAD_WAY -> {
                Log.i("Update", "Upload UI setting should be updated $updatedSetting")
            }
            null -> Log.e("error", "Nothing to update")
        }
    }

    override fun onResume() {
        super.onResume()
        initSummary(preferenceScreen)
    }

    private fun initSummary(p: Preference) {
        if (p is PreferenceGroup) {
            val pGrp: PreferenceGroup = p as PreferenceGroup
            for (i in 0 until pGrp.getPreferenceCount()) {
                initSummary(pGrp.getPreference(i))
            }
        } else {
            updatePrefSummary(p)
        }
    }

    private fun updatePrefSummary(p: Preference) {

        val modelQuality = Gson().fromJson(
            activity?.getPreferences(AppCompatActivity.MODE_PRIVATE)?.getString(
                resources.getString(R.string.key_quality), null
            ), TextDataModel::class.java
        )

        val modelUpload = Gson().fromJson(
            activity?.getPreferences(AppCompatActivity.MODE_PRIVATE)?.getString(
                resources.getString(R.string.key_upload), null
            ), TextDataModel::class.java
        )

        when (p.key) {
            resources.getString(R.string.key_upload) -> {
                p.title = modelUpload.title
                p.summary = modelUpload.summary
            }
            resources.getString(R.string.key_quality) -> {
                p.title = modelQuality.title
                p.summary = modelQuality.summary
            }
        }
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        if (handleClicks) {
            Log.d("TEST", "Main settings preference clicked: " + preference.toString())
            if (preference!!.key == resources.getString(R.string.key_upload)) {
                (activity as SettingsActivity?)!!.showFragment(
                    FileUploadSettingsFragment(),
                    R.string.file_upload_header
                )
            } else if (preference.key == resources.getString(R.string.key_quality)) {
                (activity as SettingsActivity?)!!.showFragment(
                    FileQualitySettingsFragment(),
                    R.string.file_quality
                )
            }
        }
        return super.onPreferenceTreeClick(preference)
    }

    private fun getFieldState(settingType: SETTING_KIND): TextDataModel {
        val dataQuality = resources.getStringArray(R.array.pref_upload_quality_entries)
        val dataQualitySummary = resources.getStringArray(R.array.pref_upload_quality_summary)

        val dataType = resources.getStringArray(R.array.pref_upload_type_entries)
        val dataTypeSummary = resources.getStringArray(R.array.pref_upload_type_summary)
        return TextDataModel(0, "", "", settingType)
    }
}