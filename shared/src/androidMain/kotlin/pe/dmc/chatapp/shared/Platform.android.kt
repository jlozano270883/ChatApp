package pe.dmc.chatapp.shared

import android.provider.Settings
import android.annotation.SuppressLint

@SuppressLint("HardwareIds")
actual fun idDeDispositivo(): String =
    Settings.Secure.ANDROID_ID

actual fun nombrePlataforma(): String = "Android"

