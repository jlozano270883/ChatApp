package pe.dmc.chatapp.shared

import platform.UIKit.UIDevice

actual fun idDeDispositivo(): String =
    UIDevice.currentDevice.identifierForVendor?.UUIDString() ?: "desconocido"

actual fun nombrePlataforma(): String = "iOS"

