package com.example.itc__onl2_swd4_s3_1.ui.ui.loginSignupPages


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun LoginPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {

    var email by remember {mutableStateOf("")}
    var password by remember {mutableStateOf("")}
    var passwordVisible by remember { mutableStateOf(false) }
    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    val isKeyboardVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0

    LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthState.Authenticated ->
                navController.navigate("home")
            is AuthState.Error ->
                Toast.makeText(context,
                (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(start = 24.dp, end = 24.dp, top = 82.dp)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        LoginHeader(isKeyboardVisible)
        Spacer(modifier = Modifier.height(24.dp))

        EmailField(
            email = email,
            onEmailChange = { email = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        PasswordField(
            password = password,
            onPasswordChange = { password = it },
            passwordVisible = passwordVisible,
            onPasswordVisibilityChange = { passwordVisible = !passwordVisible }
        )


        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            authViewModel.login(email,password)
        },
            enabled = authState.value != AuthState.Loading,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(

                contentColor = Color.White
            ),
            shape = RoundedCornerShape(size = 30.dp)

        ) {
            Text(text = "Login",
                    fontSize = 16.sp,
                color = Color.White)
        }


        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = {
            navController.navigate("signup")
        }) {
            Text(text = "Don't have an account, Signup")
        }

    }

}


