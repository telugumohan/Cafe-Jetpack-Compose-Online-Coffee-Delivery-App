package com.example.cafe.ui.screens.customer

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cafe.R
import com.example.cafe.ui.components.EditTextField
import com.example.cafe.ui.components.PasswordTextField
import com.example.cafe.ui.components.RegisterClickableTextComponent
import com.example.cafe.ui.theme.CafeTheme
import com.example.cafe.viewmodel.AuthViewModel


@Composable
fun Login(
    //viewModel: LoginViewModel = viewModel(),
    navigateToRegister: () -> Unit,
    navigateToBottomNav: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    val error by authViewModel.error.observeAsState()

    error?.let {
        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
    }

    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)

    LaunchedEffect(firebaseUser) {
        if (firebaseUser != null) {
            navigateToBottomNav()
        }
    }


    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = "Hey there,")
                    Text(
                        text = "Welcome Back",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    EditTextField(
                        labelText = "email",
                        leadingIcon = R.drawable.baseline_email_24,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        value = email,//viewModel.loginDetailsUiState.email,
                        onValueChange = { email = it  /*viewModel.updateDetails(viewModel.loginDetailsUiState.copy(email = it))*/},
                        errorMessage = ""
                    )
                    PasswordTextField(
                        labelText = "password",
                        leadingIcon = R.drawable.baseline_lock_24,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        value = password,//viewModel.loginDetailsUiState.password,
                        onValueChange = { password = it  /*viewModel.updateDetails(viewModel.loginDetailsUiState.copy(password = it))*/},
                        errorMessage = ""
                    )
                    Button(
                        onClick = {
                            //viewModel.updateLoginUiState(navigateToBottomNav)
                            if (email.isEmpty() || password.isEmpty()) {
                                Toast.makeText(context, "please fill all details", Toast.LENGTH_SHORT).show()
                            } else {
                                authViewModel.login(
                                    email = email,
                                    password = password,
                                    context = context
                                )
                            }

                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Login")
                    }
                    RegisterClickableTextComponent (
                        navigateToRegister = navigateToRegister,
                        value = "Don't have an account? Register"
                    )

                }
            }
        }
        if ((authViewModel.signUpInProgress.value)) {
            CircularProgressIndicator()
        }
    }
}




@Preview(showSystemUi = true, showBackground = true)
@Composable
fun LoginPagePreview() {
    CafeTheme {
        Login(navigateToRegister = { /*TODO*/ }, navigateToBottomNav = {})
    }
}