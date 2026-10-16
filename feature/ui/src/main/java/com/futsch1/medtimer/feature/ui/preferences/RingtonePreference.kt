package com.futsch1.medtimer.feature.ui.preferences

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.util.AttributeSet
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.preference.Preference

@SuppressLint("ResourceType")
class RingtonePreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.preference.R.attr.preferenceStyle
) : Preference(context, attrs, defStyleAttr) {

    private var launcher: ActivityResultLauncher<Intent>? = null

    init {
        if (context is ComponentActivity) {
            launcher = context.registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                    val uri: Uri? = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                        result.data?.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI, Uri::class.java)
                    } else {
                        @Suppress("DEPRECATION")
                        result.data?.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
                    }
                    val uriString = uri?.toString() ?: ""
                    if (callChangeListener(uriString)) {
                        persistString(uriString)
                        updateSummary(uriString)
                    }
                }
            }
        }
    }

    override fun onClick() {
        val currentUriString = getPersistedString("")
        val currentUri = if (currentUriString.isNotEmpty()) Uri.parse(currentUriString) else RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

        val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER).apply {
            putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM)
            putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)
            putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false)
            putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
            putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, currentUri)
            putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, title)
        }

        launcher?.launch(intent)
    }

    override fun onSetInitialValue(defaultValue: Any?) {
        val uriString = getPersistedString(defaultValue as? String ?: "")
        updateSummary(uriString)
    }

    private fun updateSummary(uriString: String) {
        if (uriString.isEmpty()) {
            summary = "Predeterminado"
            return
        }
        val uri = Uri.parse(uriString)
        val ringtone = RingtoneManager.getRingtone(context, uri)
        summary = ringtone?.getTitle(context) ?: uriString
    }
}
