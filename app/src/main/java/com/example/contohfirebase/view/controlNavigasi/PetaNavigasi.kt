package com.example.contohfirebase.view.controlNavigasi

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.contohfirebase.view.EntrySiswaScreen
import com.example.contohfirebase.view.HomeScreen
import com.example.contohfirebase.view.route.DestinasiDetail
import com.example.contohfirebase.view.route.DestinasiEntry
import com.example.contohfirebase.view.route.DestinasiHome

@Composable
fun DataSiswaApp(navController: NavHostController = rememberNavController(),
                 modifier: Modifier){
    HostNavigasi(navController = navController)
}

@Composable
fun HostNavigasi(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    NavHost(navController = navController, startDestination = DestinasiHome.route,
        modifier = Modifier) {
        composable(DestinasiHome.route) {
            HomeScreen(navigateToItemEntry = { navController.navigate(DestinasiEntry.route)},
                navigateToItemUpdate = {
                    navController.navigate(("${DestinasiDetail.route}/${it}"))
                })
        }
        composable (DestinasiEntry.route){
            EntrySiswaScreen(navigateBack = {navController.navigate(DestinasiHome
                .route)})
        }
    }
}