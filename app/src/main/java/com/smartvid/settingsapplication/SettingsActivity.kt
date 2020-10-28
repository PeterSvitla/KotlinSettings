package com.smartvid.settingsapplication

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager

class SettingsActivity : AppCompatActivity() {

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

    public fun showFragment(fragment: PreferenceFragmentCompat, titleText: Int) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, fragment)
            .addToBackStack(null)
            .commit()
        supportActionBar!!.setTitle(titleText)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    class MainPreferenceFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }

        override fun onPreferenceTreeClick(preference: Preference?): Boolean {
            Log.d("TEST", "Main settings preference clicked: " + preference.toString())
            if (preference!!.key == "file_upload") {
                (activity as SettingsActivity?)!!.showFragment(FileUploadSettingsFragment(), R.string.file_upload_header)
            } else if (preference.key == "file_quality") {
                (activity as SettingsActivity?)!!.showFragment(FileQualitySettingsFragment(), R.string.file_quality)
            }
            return super.onPreferenceTreeClick(preference)
        }
    }

    class FileUploadSettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.file_upload_preferences, rootKey)
        }

        override fun onPreferenceTreeClick(preference: Preference?): Boolean {
            Log.d("TEST", "Upload Setting preference clicked: " + preference.toString())
            return super.onPreferenceTreeClick(preference)
        }
    }

    class FileQualitySettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.file_quality_preferences, rootKey)
        }

        override fun onPreferenceTreeClick(preference: Preference?): Boolean {
            Log.d("TEST", "File Quality Setting preference clicked: " + preference.toString())
            return super.onPreferenceTreeClick(preference)
        }
    }

    companion object {
        private fun bindPreferenceSummaryToValue(preference: Preference) {
            preference.onPreferenceChangeListener = sBindPreferenceSummaryToValueListener

            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                    .getDefaultSharedPreferences(preference.context)
                    .getString(preference.key, ""))
        }

        private val sBindPreferenceSummaryToValueListener = Preference.OnPreferenceChangeListener { preference, newValue ->
            val stringValue = newValue.toString()

            if (preference is ListPreference) {
                val index = preference.findIndexOfValue(stringValue)
                preference.setSummary(
                    if (index >= 0)
                        preference.entries[index]
                    else
                        null)

            } else if (preference is Preference) {
                if (preference.getKey() == "file_quality") {
                    preference.setSummary(stringValue)
                }
            } else {
                preference.summary = stringValue
            }
            true
        }
    }
}