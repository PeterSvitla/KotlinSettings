package com.smartvid.settingsapplication.menu.menu_activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.smartvid.settingsapplication.R
import com.smartvid.settingsapplication.model.SETTING_KIND
import com.smartvid.settingsapplication.model.TextDataModel
import com.smartvid.settingsapplication.view.MainPreferenceFragment
import com.smartvid.settingsapplication.viewmodel.SettingViewModel

class SettingsActivity : AppCompatActivity(), View.OnClickListener {
    private val model: SettingViewModel by viewModels()
    val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(false)
            supportActionBar!!.setTitle(R.string.title_activity_settings)
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.settings, MainPreferenceFragment(handleClicks = true))
                    .commit()

            model.currentUploadKind.value = gson.fromJson(getPreferences(MODE_PRIVATE).getString(
                resources.getString(R.string.key_upload), null), TextDataModel::class.java)
            model.currentUploadQuality.value = gson.fromJson(getPreferences(MODE_PRIVATE).getString(
                resources.getString(R.string.key_quality), null), TextDataModel::class.java)

            Log.i("Test", "Upload model: " + (model.currentUploadKind.getValue()?.title ?: ""))
            Log.i("Test", "Quality model: " + (model.currentUploadQuality.getValue()?.title ?: ""))

            // Create the observer which updates the UI.
            val settingObserver = Observer<TextDataModel> { updatedSetting ->
                // Updates for the UI is ready
                Log.i("Test", "Updated model: $updatedSetting")
                //Store in shared preferences
                storeSetting(updatedSetting)
            }

            // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
            model.currentUploadKind.observe(this, settingObserver)
            model.currentUploadQuality.observe(this, settingObserver)
        }
    }

    private fun storeSetting(updatedSetting: TextDataModel?) {
        val prefsEditor = getPreferences(MODE_PRIVATE).edit()
        val json = gson.toJson(updatedSetting)

        when (updatedSetting?.settingKind) {
            SETTING_KIND.QUALITY -> {
                prefsEditor.putString(resources.getString(R.string.key_quality), json)
                prefsEditor.apply()

                Log.i("Test", "Stored quality updates")
            }
            SETTING_KIND.UPLOAD_WAY -> {
                prefsEditor.putString(resources.getString(R.string.key_upload), json)
                prefsEditor.apply()

                Log.i("Test", "Stored upload way updates")
            }
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

    override fun onClick(v: View?) {
        Log.i("Test", "Selected a field " + v!!.id);
        when(v.id) {
            R.id.option_full -> {
                val textDataModel = TextDataModel(
                    0, getString(R.string.resolution_full), getString(
                        R.string.resolution_full_detail
                    ), SETTING_KIND.QUALITY
                )
                model.currentUploadQuality.setValue(textDataModel)
            }
            R.id.option_high -> {
                val textDataModel = TextDataModel(
                    1, getString(R.string.resolution_high), getString(
                        R.string.resolution_high_details
                    ), SETTING_KIND.QUALITY
                )
                model.currentUploadQuality.setValue(textDataModel)
            }
            R.id.option_medium -> {
                val textDataModel = TextDataModel(
                    2, getString(R.string.resolution_medium), getString(
                        R.string.resolution_medium_details
                    ), SETTING_KIND.QUALITY
                )
                model.currentUploadQuality.setValue(textDataModel)
            }
            R.id.option_wifi -> {
                val textDataModel = TextDataModel(
                    0,
                    getString(R.string.wifi_only),
                    " ",
                    SETTING_KIND.UPLOAD_WAY
                )
                model.currentUploadKind.setValue(textDataModel)
            }
            R.id.option_wifi_cellular -> {
                val textDataModel = TextDataModel(
                    1, getString(R.string.wifi_or_cellular), getString(
                        R.string.optimal_performance
                    ), SETTING_KIND.UPLOAD_WAY
                )
                model.currentUploadKind.setValue(textDataModel)
            }
        }
    }

    fun showFragment(fragment: Fragment, titleText: Int) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, fragment)
            .addToBackStack(null)
            .commit()
        supportActionBar!!.setTitle(titleText)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}