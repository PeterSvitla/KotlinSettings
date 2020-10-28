package com.smartvid.settingsapplication.menu.menu_activity

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.*
import com.smartvid.settingsapplication.R
import java.util.*


class SettingsActivitySimplifiedUI : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(false)
            supportActionBar!!.setTitle(R.string.title_activity_settings)
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.settings, MainPreferenceFragment())
                    .commit()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        supportActionBar!!.setTitle(R.string.title_activity_settings)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        super.onBackPressed()
    }

    class MainPreferenceFragment : PreferenceFragmentCompat(),
        SharedPreferences.OnSharedPreferenceChangeListener {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences_simplified, rootKey)
            PreferenceManager.setDefaultValues(
                requireContext(), R.xml.root_preferences_simplified,
                false
            )
            initSummary(preferenceScreen)
        }

        override fun onResume() {
            super.onResume()
            preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        }

        override fun onPause() {
            preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
            super.onPause()
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

        override fun onSharedPreferenceChanged(
            sharedPreferences: SharedPreferences?,
            key: String?
        ) {
            Log.d("SP", "onSharedPreferenceChanged $key")
            updatePrefSummary(findPreference(key!!)!!)
        }
    }

}