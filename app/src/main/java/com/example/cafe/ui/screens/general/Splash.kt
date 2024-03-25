package com.example.cafe.ui.screens.general

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.cafe.R
import com.example.cafe.utils.SharedPref
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay


@Composable
fun Splash (
    navigateToChoice: () -> Unit,
    isCustomer: (Boolean) -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(true) {
        delay(3000)
        if(FirebaseAuth.getInstance().currentUser == null) {
            navigateToChoice()
        } else {
            isCustomer(SharedPref.getIsCustomer(context))
        }
    }
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 125.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cafe_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = "Bloody Sweet",
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.padding(top = 24.dp, bottom = 16.dp),
                    fontFamily = FontFamily.Cursive,
                    fontWeight = FontWeight.Bold
                )
                Text (
                    text = "Inject more caffeine",
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = FontFamily.Cursive
                )
            }
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.fillMaxSize(),
            ) {
                Text (
                    text = "Designed By\nVaruni",
                    color = Color.Green,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = FontFamily.Cursive
                )
            }
        }
    }

}