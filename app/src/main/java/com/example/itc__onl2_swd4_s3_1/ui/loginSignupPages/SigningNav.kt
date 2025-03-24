package com.example.itc__onl2_swd4_s3_1.ui.loginSignupPages

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun MyAppNavigation(modifier: Modifier = Modifier,authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login", builder = {
        composable("login"){
            LoginPage (modifier,navController,authViewModel)
        }
        composable("signup"){
            SignupPage (modifier,navController,authViewModel)
        }
        composable("home") {
            HomePage(modifier, navController, authViewModel) // Change MockAuthViewModel to AuthViewModel
        }
    })
}