package com.smartvid.settingsapplication.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.preference.*
import com.smartvid.settingsapplication.R
import com.smartvid.settingsapplication.model.SETTING_KIND
import com.smartvid.settingsapplication.menu.menu_activity.SettingsActivity
import com.smartvid.settingsapplication.model.TextDataModel
import com.smartvid.settingsapplication.viewmodel.SettingViewModel
import java.util.*

class MainPreferenceFragment(private var handleClicks: Boolean) : PreferenceFragmentCompat() {

    private val model: SettingViewModel by viewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        PreferenceManager.setDefaultValues(
            requireContext(), R.xml.root_preferences_simplified,
            false
        )
    }

    private fun updateUI(updatedSetting: TextDataModel?) {
        when(updatedSetting?.settingKind) {
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

        if (p is ListPreference) {

            val dataQuality = resources.getStringArray(R.array.pref_upload_quality_entries)
            val dataQualitySummary = resources.getStringArray(R.array.pref_upload_quality_summary)

            val dataType = resources.getStringArray(R.array.pref_upload_type_entries)
            val dataTypeSummary = resources.getStringArray(R.array.pref_upload_type_summary)

            val foundQuality = Arrays.stream(dataQuality).anyMatch { t -> t == p.entry }
            val foundType = Arrays.stream(dataType).anyMatch { t -> t == p.entry }

            Log.d("Test", "Found quality: $foundQuality Found type: $foundType");

            when(p.entry) {
                dataQuality[0] -> {
                    p.setTitle(dataQuality[0])
                    p.setSummary(dataQualitySummary[0])
                }
                dataQuality[1] -> {
                    p.setTitle(dataQuality[1])
                    p.setSummary(dataQualitySummary[1])
                }
                dataQuality[2] -> {
                    p.setTitle(dataQuality[2])
                    p.setSummary(dataQualitySummary[2])
                }
                dataType[0] -> {
                    p.setTitle(dataType[0])
                    p.setSummary(dataTypeSummary[0])
                }
                dataType[1] -> {
                    p.setTitle(dataType[1])
                    p.setSummary(dataTypeSummary[1])
                }
                else -> {
                    p.setTitle("")
                    p.setSummary("")
                }
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

    private fun getFieldState(settingType : SETTING_KIND) : TextDataModel {
        val dataQuality = resources.getStringArray(R.array.pref_upload_quality_entries)
        val dataQualitySummary = resources.getStringArray(R.array.pref_upload_quality_summary)

        val dataType = resources.getStringArray(R.array.pref_upload_type_entries)
        val dataTypeSummary = resources.getStringArray(R.array.pref_upload_type_summary)
        return TextDataModel(0,"", "", settingType)
    }
}