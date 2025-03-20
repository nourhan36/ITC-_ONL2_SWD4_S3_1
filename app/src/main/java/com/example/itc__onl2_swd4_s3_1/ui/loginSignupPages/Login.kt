package com.example.itc__onl2_swd4_s3_1.ui.loginSignupPages

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.itc__onl2_swd4_s3_1.R
import com.example.itc__onl2_swd4_s3_1.ui.homePage.HomeActivity
import com.example.itc__onl2_swd4_s3_1.ui.loginSignupPages.ui.theme.ITC_ONL2_SWD4_S3_1Theme

class Login : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ITC_ONL2_SWD4_S3_1Theme {
                LoginScreen(
                    onItemClick = {
                        val intent = Intent(this, SignUp    ::class.java)
                        startActivity(intent)
                        finish()
                    },
                    onSignInClick = {
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                    }
                )
                }
            }
        }
    }


@Composable
fun LoginScreen(onItemClick: () -> Unit, onSignInClick: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val isKeyboardVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0

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

        Spacer(modifier = Modifier.height(24.dp))

        SignButton(
            btnName = "Sign In",
            onSignInClick = onSignInClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (!isKeyboardVisible) {
            AdditionalOptions(onItemClick = onItemClick)
        }
    }
}

@Composable
fun LoginHeader(isKeyboardVisible: Boolean) {
    val logoFontSize = if (isKeyboardVisible) 16.sp else 32.sp
    val logoBottomPadding = if (isKeyboardVisible) 8.dp else 16.dp

    Text(
        text = "Ramadan Tracker",
        fontSize = logoFontSize,
        color = Color.Black,
        modifier = Modifier.padding(bottom = logoBottomPadding)
    )

    if (!isKeyboardVisible) {
        Text(
            text = "Welcome!",
            fontSize = 24.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Please enter your data",
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun EmailField(
    email: String,
    onEmailChange: (String) -> Unit
) {
    OutlinedTextField(
        value = email,
        onValueChange = onEmailChange,
        label = { Text(text = "E-mail") },
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
    )
}

@Composable
fun PasswordField(
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibilityChange: () -> Unit,
    label: String = "Password"
) {
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = { Text(text = label) },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = onPasswordVisibilityChange) {
                Icon(
                    painter = painterResource(
                        id = if (passwordVisible) R.drawable.eye_not_visibile else R.drawable.eye_visible
                    ),
                    contentDescription = if (passwordVisible) "Hide password" else "Show password"
                )
            }
        },
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password)
    )
}

@Composable
fun SignButton(btnName: String, onSignInClick: () -> Unit) {
    Button(
        onClick = onSignInClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(

            contentColor = Color.White
        ),
        shape = RoundedCornerShape(size = 30.dp)
    ) {
        Text(
            text = btnName,
            fontSize = 16.sp,
            color = Color.White
        )
    }
}

@Composable
fun AdditionalOptions(onItemClick: () -> Unit) {
    Text(
        text = "New to App? Create account",
        fontSize = 14.sp,
        color = Color.Gray,
        modifier = Modifier.clickable { onItemClick() }
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = "or login with",
        fontSize = 14.sp,
        color = Color.Gray
    )

    Spacer(modifier = Modifier.height(16.dp))

    SocialLoginButtons()
}

@Composable
fun SocialLoginButtons() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(onClick = { /* Handle X Login */ }) {
            Icon(
                painter = painterResource(id = R.drawable.x),
                contentDescription = "X Login",
                modifier = Modifier.size(48.dp),
                tint = Color.Unspecified
            )
        }

        Spacer(modifier = Modifier.width(24.dp))

        IconButton(onClick = { /* Handle Facebook Login */ }) {
            Icon(
                painter = painterResource(id = R.drawable.facebook),
                contentDescription = "Facebook Login",
                modifier = Modifier.size(48.dp),
                tint = Color.Unspecified
            )
        }

        Spacer(modifier = Modifier.width(24.dp))

        IconButton(onClick = { /* Handle Google Login */ }) {
            Icon(
                painter = painterResource(id = R.drawable.google),
                contentDescription = "Google Login",
                modifier = Modifier.size(48.dp),
                tint = Color.Unspecified
            )
        }
    }
}