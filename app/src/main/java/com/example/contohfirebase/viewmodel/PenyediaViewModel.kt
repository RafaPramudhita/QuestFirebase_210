package com.example.contohfirebase.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.contohfirebase.repositori.AplikasiDataSiswa
import com.example.contohfirebase.viewmodel.EntryViewModel
import com.example.contohfirebase.viewmodel.HomeViewModel


fun CreationExtras.aplikasiDataSiswa(): AplikasiDataSiswa =
    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
            as AplikasiDataSiswa

object PenyediaViewModel {

    val Factory = viewModelFactory {

        initializer {
            HomeViewModel(
                aplikasiDataSiswa().container.repositorySiswa
            )
        }

        initializer {
            EntryViewModel(
                aplikasiDataSiswa().container.repositorySiswa
            )
        }
    }
}