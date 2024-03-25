package com.example.cafe.ui.screens.owner

import com.example.cafe.R
import com.example.cafe.ui.theme.CafeTheme
import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.cafe.ui.components.EditTextField
import com.example.cafe.ui.components.LoginClickableTextComponent
import com.example.cafe.ui.components.PasswordTextField
import com.example.cafe.viewmodel.AuthViewModel
import com.example.cafe.viewmodel.OwnerAuthViewModel

@Composable
fun OwnerRegister (
    navigateToOwnerLogin: () -> Unit,
    navigateToOwnerBottomNav: () -> Unit,
    //viewModel: RegisterViewModel = viewModel()
    ownerAuthViewModel: OwnerAuthViewModel = viewModel()
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {

            val firebaseUser by ownerAuthViewModel.firebaseUser.observeAsState(null)

            var imageUri by remember { mutableStateOf<Uri?>(null) }
            var name by remember{ mutableStateOf("") }
            var cafeName by remember{ mutableStateOf("") }
            var location by remember { mutableStateOf("") }
            var licenceNumber by remember { mutableStateOf("") }
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }

            val context = LocalContext.current




            val error by ownerAuthViewModel.error.observeAsState()

            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }

            val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
                    uri: Uri? ->
                imageUri = uri
            }

            val permissionLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {
                    isGranted: Boolean ->
                if (isGranted) {

                } else {

                }
            }

            val permissionToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.READ_MEDIA_IMAGES
            } else {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }

            LaunchedEffect(firebaseUser) {
                if (firebaseUser != null) {
                    navigateToOwnerBottomNav()
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
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
                        text = "Create an Account",
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
                    Box {
                        Image(
                            painter =
                            if (imageUri == null) { painterResource(id = R.drawable.boy) }
                            else { rememberAsyncImagePainter(model = imageUri)},
                            contentDescription = null,
                            modifier = Modifier
                                .size(96.dp)
                                .clip(shape = RoundedCornerShape(48.dp))
                                .clickable {
                                    val isGranted = ContextCompat.checkSelfPermission(
                                        context, permissionToRequest
                                    ) == PackageManager.PERMISSION_GRANTED

                                    if (isGranted) {
                                        launcher.launch("image/*")
                                    } else {
                                        permissionLauncher.launch(permissionToRequest)
                                    }
                                },
                            contentScale = ContentScale.Crop
                        )
                        Column(
                            modifier = Modifier
                                .size(96.dp),
                            //.clip(shape = MaterialTheme.shapes.large),
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.camera),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(24.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                    EditTextField(
                        labelText = "Name",
                        leadingIcon = R.drawable.baseline_person_24,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        value = name, // viewModel.registrationDetailsUiState.fName,
                        onValueChange = { name = it  /* viewModel.updateDetails(viewModel.registrationDetailsUiState.copy(fName = it)) */},
                        errorMessage = "", // viewModel.registrationUiState.validationResult.errors?.get("fName") ?: ""
                    )
                    EditTextField(
                        labelText = "Cafe Name",
                        leadingIcon = R.drawable.baseline_short_text_24,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        value = cafeName, // viewModel.registrationDetailsUiState.fName,
                        onValueChange = { cafeName = it  /* viewModel.updateDetails(viewModel.registrationDetailsUiState.copy(fName = it)) */},
                        errorMessage = "", // viewModel.registrationUiState.validationResult.errors?.get("fName") ?: ""
                    )
                    EditTextField(
                        labelText = "Location",
                        leadingIcon = R.drawable.baseline_my_location_24,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        value = location, // viewModel.registrationDetailsUiState.fName,
                        onValueChange = { location = it  /* viewModel.updateDetails(viewModel.registrationDetailsUiState.copy(fName = it)) */},
                        errorMessage = "", // viewModel.registrationUiState.validationResult.errors?.get("fName") ?: ""
                    )
                    EditTextField(
                        labelText = "Licence Number",
                        leadingIcon = R.drawable.baseline_numbers_24,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        value = licenceNumber, // viewModel.registrationDetailsUiState.fName,
                        onValueChange = { licenceNumber = it  /* viewModel.updateDetails(viewModel.registrationDetailsUiState.copy(fName = it)) */},
                        errorMessage = "", // viewModel.registrationUiState.validationResult.errors?.get("fName") ?: ""
                    )
                    EditTextField(
                        labelText = "email",
                        leadingIcon = R.drawable.baseline_email_24,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        value = email, // viewModel.registrationDetailsUiState.email,
                        onValueChange = { email = it  /* viewModel.updateDetails(viewModel.registrationDetailsUiState.copy(email = it)) */},
                        errorMessage = "", // viewModel.registrationUiState.validationResult.errors?.get("email") ?: ""
                    )
                    PasswordTextField(
                        labelText = "password",
                        leadingIcon = R.drawable.baseline_lock_24,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        value = password, // viewModel.registrationDetailsUiState.password,
                        onValueChange = { password = it  /* viewModel.updateDetails(viewModel.registrationDetailsUiState.copy(password = it)) */},
                        errorMessage = "", // viewModel.registrationUiState.validationResult.errors?.get("password") ?: ""
                    )
//                    PrivacyPolicyComposable(
//                        checked = false,//viewModel.registrationDetailsUiState.isPrivacyPolicyChecked,
//                        onCheckedChange = {/*viewModel.updateDetails(viewModel.registrationDetailsUiState.copy(isPrivacyPolicyChecked = it))*/}
//                    )
                    Button(
                        onClick = {
                            //viewModel.updateRegistrationUiState(navigateToBottomNav)
                            if (name.isEmpty() ||  email.isEmpty() || password.isEmpty() || imageUri == null || cafeName.isEmpty() ||  location.isEmpty() || licenceNumber.isEmpty()) {
                                Toast.makeText(context, "please fill all details", Toast.LENGTH_SHORT).show()
                            } else {
                                ownerAuthViewModel.ownerRegister(
                                    imgUrl = imageUri!!,
                                    name = name,
                                    cafeName = cafeName,
                                    location = location,
                                    licenceNumber = licenceNumber,
                                    email = email,
                                    password = password,
                                    context = context,
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Register")
                    }
                    LoginClickableTextComponent(
                        navigateToLogin = navigateToOwnerLogin,
                        value = "Already have an account? Login"
                    )
                }
            }
        }
        if (ownerAuthViewModel.signUpInProgress.value) {
            CircularProgressIndicator()
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun RegisterPagePreview() {
    CafeTheme {

    }
}
