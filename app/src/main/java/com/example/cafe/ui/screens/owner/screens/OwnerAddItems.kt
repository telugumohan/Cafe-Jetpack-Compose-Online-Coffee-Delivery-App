package com.example.cafe.ui.screens.owner.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.cafe.R
import com.example.cafe.model.CoffeeModel
import com.example.cafe.ui.components.EditTextField
import com.example.cafe.ui.screens.owner.viewmodels.AddItemViewModel
import com.google.firebase.auth.FirebaseAuth

//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OwnerAddItems(
    addItemViewModel: AddItemViewModel = viewModel(),
    navigateToHome: () -> Unit
) {
    Scaffold(
        topBar = {
            OwnerCenterAlignedTopAppBar(title = "Add Item", canNavigateBack = true, navigateBack = {})
        }
    ) { paddingValues ->
        var imageUri by remember { mutableStateOf<Uri?>(null) }
        var title by remember{ mutableStateOf("") }
        var  comboWith by remember { mutableStateOf("") }
        var  originalPrice by remember { mutableStateOf("") }
        var  yourPrice by remember { mutableStateOf("") }
        var quantity by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }

        val context = LocalContext.current
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
        
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    if (imageUri == null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Upload Item Image")
                            Image(
                                painter = painterResource(id = R.drawable.attach_file),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable {
                                        val isGranted = ContextCompat.checkSelfPermission(
                                            context, permissionToRequest
                                        ) == PackageManager.PERMISSION_GRANTED

                                        if (isGranted) {
                                            launcher.launch("image/*")
                                        } else {
                                            permissionLauncher.launch(permissionToRequest)
                                        }
                                    }
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Clear")
                            IconButton(onClick = { imageUri = null }) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                            }
                        }
                        Image(
                            painter = rememberAsyncImagePainter(model = imageUri),
                            contentDescription = null,
                            modifier = Modifier
                                .size(120.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
            item {
                EditTextField(
                    value = title,
                    onValueChange = {title = it},
                    labelText = "Title",
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    errorMessage = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )
            }
            item {
                EditTextField(
                    value = comboWith,
                    onValueChange = {comboWith = it},
                    labelText = "Combo With",
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    errorMessage = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )
            }
            item {
                EditTextField(
                    value = description,
                    onValueChange = {description = it},
                    labelText = "Description",
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    errorMessage = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    singleLine = false,
                    maxLines = 5
                )
            }
            item {
                EditTextField(
                    value = originalPrice,
                    onValueChange = {originalPrice = it},
                    labelText = "Original Price",
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    errorMessage = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )
            }
            item {
                EditTextField(
                    value = yourPrice,
                    onValueChange = {yourPrice = it},
                    labelText = "Your Price",
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    errorMessage = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )
            }
            item {
                EditTextField(
                    value = quantity,
                    onValueChange = {quantity = it},
                    labelText = "Quantity",
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    ),
                    errorMessage = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )
            }
            item {
                Button(
                    onClick = {
                              if(title.isNotEmpty() && imageUri != null &&
                                  comboWith.isNotEmpty() && description.isNotEmpty() &&
                                  originalPrice.isNotEmpty() && yourPrice.isNotEmpty() &&
                                  quantity.isNotEmpty()
                                  ) {
                                  val product: CoffeeModel = CoffeeModel(
                                      title = title,
                                      comboWith = comboWith,
                                      description = description,
                                      originalPrice = originalPrice,
                                      currentPrice = yourPrice,
                                      quantityAvail = quantity.toIntOrNull() ?: 0,
                                      ownerId = FirebaseAuth.getInstance().currentUser!!.uid
                                  )
                                  addItemViewModel.addProductToOwner(imgUri = imageUri!!, product = product, navigateToHome = navigateToHome)
                              } else {
                                  Toast.makeText(context, "Please fill all details.", Toast.LENGTH_SHORT).show()
                              }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    Text(text = "Add to list")
                }
            }

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerCenterAlignedTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    navigateBack: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = title, style = MaterialTheme.typography.headlineSmall)
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = { navigateBack() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            }
        }
    )
}