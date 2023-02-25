package br.com.joaovitorqueiroz.firebasefirestore.ui.preferences

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import br.com.joaovitorqueiroz.firebasefirestore.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}