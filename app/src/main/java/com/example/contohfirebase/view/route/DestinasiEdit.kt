package com.example.contohfirebase.view.route

import com.example.contohfirebase.R
import com.example.contohfirebase.view.route.DestinasiNavigasi

object DestinasiEdit : DestinasiNavigasi {
    override val route = "item_edit"
    override val titleRes = R.string.edit_siswa
    const val itemIdArg = "idSiswa"
    val routeWithArgs = "$route/{$itemIdArg}"
}