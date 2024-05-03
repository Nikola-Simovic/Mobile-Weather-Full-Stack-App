package com.example.myapplication

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.content.Context.LOCATION_SERVICE
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState


@SuppressLint("MissingPermission")
@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class, ExperimentalPermissionsApi::class
)
@Composable
fun MainApp(navController: NavController) {

    val isDarkTheme = isSystemInDarkTheme()

    val titleColor = if (isDarkTheme) Color.White else Color.Black
    val headerColor = if (isDarkTheme) Color(202, 231, 254, 150) else { MaterialTheme.colorScheme.primaryContainer}



    // Pass a lambda that returns the number of pages for the pager
    val pagerState = rememberPagerState {
        2 // Number of pages in your pager (screen1 and screen2)
    }

    val context=LocalContext.current
    val locationManager= context.getSystemService(LOCATION_SERVICE) as LocationManager
    val locationPermissionState = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)

    var lat = 89.99
    var lon =179.99
    var newLat=remember { mutableStateOf(0.0) }
    var newLon=remember { mutableStateOf(0.0) }


    val currentPage = remember { mutableStateOf(0) }
    currentPage.value = pagerState.currentPage

    var locationError by remember { mutableStateOf<String?>(null) }



    LaunchedEffect(Unit) {
        if(!(locationPermissionState.status.isGranted)) {
            try {
                locationPermissionState.launchPermissionRequest()
            } catch (e: Exception) {
                locationError = e.message
            }
        }
    }



    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = headerColor,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Box(
                        modifier = Modifier.fillMaxSize(), // Fill the entire space of the top app bar
                        contentAlignment = Alignment.Center  // Center the title both vertically and horizontally
                    ) {
                        Text(
                            stringResource(R.string.weather),
                            overflow = TextOverflow.Ellipsis,
                            color = titleColor,
                            fontSize = 35.sp,
                        )
                    }
                },
                actions = {
                    // State variable to manage the expanded state of the dropdown menu
                    var expanded by remember { mutableStateOf(false) }

                    Box(
                        modifier = Modifier.fillMaxHeight(), // Fill the entire space of the top app bar
                        contentAlignment = Alignment.CenterEnd  // Center the action button both vertically and horizontally
                    ) {
                        // Icon button that opens the dropdown menu
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Main Menu"
                            )
                        }
                    }
                    // Dropdown menu
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            onClick = {
                                if (locationPermissionState.status.isGranted)
                                {
                                    val location=locationManager.getLastKnownLocation(LocationManager.FUSED_PROVIDER)
                                    if ((location?.latitude)!=null) {
                                        lat = location.latitude
                                    }
                                    if ((location?.longitude)!=null) {
                                        lon = location.longitude
                                    }



                                    val locationEventListener = object: LocationListener{
                                        override fun onLocationChanged(location: Location) {
                                            if (location!=null)
                                            {
                                                 lat = location.latitude
                                                 lon = location.longitude

                                            }
                                        }

                                        override fun onStatusChanged(provider: String, status: Int, extras: Bundle?) {

                                        }

                                        override fun onProviderEnabled(provider: String) {
                                            Toast.makeText(context, "Provider $provider is enabled", Toast.LENGTH_SHORT).show()
                                        }

                                        override fun onProviderDisabled(provider: String) {
                                            Toast.makeText(context, "Provider $provider is disabled", Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                    locationManager.requestLocationUpdates(LocationManager.FUSED_PROVIDER,0,0.0f,locationEventListener)
                                    val geoUri = Uri.parse("geo:$lat,$lon?q=$lat,$lon")

                                    val intent = Intent(Intent.ACTION_VIEW, geoUri)

                                    if (intent.resolveActivity(context.packageManager) != null) {
                                        context.startActivity(intent)
                                    } else {
                                        Toast.makeText(context, "No app available to show the location", Toast.LENGTH_SHORT).show()
                                    }




                                }
                                else {
                                    val builder = AlertDialog.Builder(context)

                                    builder.setTitle("Permission Required")
                                    builder.setMessage("This app requires your permission. To enable this functionality, go to Permissions -> Location -> Allow while using the app.")

                                    builder.setPositiveButton("OK") { _, _ ->
                                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                        val uri = Uri.fromParts("package", context.packageName, null)
                                        intent.data = uri
                                        context.startActivity(intent)
                                    }

                                    // Create the dialog and show it
                                    val dialog = builder.create()
                                    dialog.show()
                                }
                                expanded = false
                            },
                            text = { Text(stringResource(R.string.open_map)) }
                        )
                        DropdownMenuItem(
                            onClick = {
                                val url = "https://openweathermap.org/api"
                                val intent = Intent(Intent.ACTION_VIEW)
                                intent.data = Uri.parse(url)
                                if (intent.resolveActivity(context.packageManager) != null) {
                                    context.startActivity(intent)
                                }
                                else {
                                    Toast.makeText(context, "No application found to handle the intent", Toast.LENGTH_SHORT).show()
                                }
                                expanded = false

                            },
                            text = { Text(stringResource(R.string.our_source)) }
                        )
                        DropdownMenuItem(
                            onClick = {

                                navController.navigate("about")
                                expanded = false
                            },
                            text = { Text(stringResource(R.string.about)) }
                        )
                    }
                },
                modifier = Modifier
                    .height(70.dp)



            )
        }
        ,
        bottomBar = {
            BottomAppBar(containerColor =Color(202, 231, 254, 150)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,

                ) {
                    // Left-side icon
                    IconButton(onClick = {
                                         if (locationPermissionState.status.isGranted)
                                         {
                                             val location=locationManager.getLastKnownLocation(LocationManager.FUSED_PROVIDER)

                                             if (location != null) {
                                                 newLon.value = location.longitude
                                                 newLat.value = location.latitude

                                             }

                                             val locationEventListener = object: LocationListener{
                                                 override fun onLocationChanged(location: Location) {
                                                     if (location!=null)
                                                     {
                                                         lat = location.latitude
                                                         lon = location.longitude
                                                         newLon.value = location.longitude
                                                         newLat.value = location.latitude


                                                     }
                                                 }

                                                 override fun onStatusChanged(provider: String, status: Int, extras: Bundle?) {

                                                 }

                                                 override fun onProviderEnabled(provider: String) {
                                                     Toast.makeText(context, "Provider $provider is enabled", Toast.LENGTH_SHORT).show()
                                                 }

                                                 override fun onProviderDisabled(provider: String) {
                                                     Toast.makeText(context, "Provider $provider is disabled", Toast.LENGTH_SHORT).show()
                                                 }
                                             }
                                             locationManager.requestLocationUpdates(LocationManager.FUSED_PROVIDER,0,0.0f,locationEventListener)



                                         }
                        else {
                                             val builder = AlertDialog.Builder(context)

                                             builder.setTitle("Permission Required")
                                             builder.setMessage("This app requires your permission. To enable this functionality, go to Permissions -> Location -> Allow while using the app.")

                                             builder.setPositiveButton("OK") { _, _ ->
                                                 val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                                 val uri = Uri.fromParts("package", context.packageName, null)
                                                 intent.data = uri
                                                 context.startActivity(intent)
                                             }

                                             // Create the dialog and show it
                                             val dialog = builder.create()
                                             dialog.show()
                        }
                    },
                        modifier = Modifier.padding(4.dp)) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = "Map"
                        )
                    }

                    // Center: Indicator Dots
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(2) { index ->
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(
                                        color = if (currentPage.value == index) Color.Black else Color.Gray,
                                        shape = CircleShape
                                    )
                            )
                            if (index == 0) {
                                Spacer(modifier = Modifier.width(10.dp))
                            }
                        }
                    }

                    // Right-side Floating Action Button
                    FloatingActionButton(
                        onClick = {
                            val url = "https://openweathermap.org/api"
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse(url)
                            if (intent.resolveActivity(context.packageManager) != null) {
                                context.startActivity(intent)
                             }
                            else {
                                 Toast.makeText(context, "No application found to handle the intent", Toast.LENGTH_LONG).show()
                            }
                                  },
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.chain_link_icon),
                            contentDescription = "FAB icon",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

        },
        content = { innerPadding ->
            val pagerState = rememberPagerState { 2 }
            // Use HorizontalPager to handle swipe navigation
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) { page ->
                // Determine the current page and display content accordingly
                currentPage.value = page
                when (page) {
                    0 -> CurrentWeatherScreen(newLat.value,newLon.value)
                    1 -> WeatherForecastScreen(newLat.value,newLon.value)
                }
            }
        }
    )
}


