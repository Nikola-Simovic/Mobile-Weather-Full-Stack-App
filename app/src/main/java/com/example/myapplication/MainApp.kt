package com.example.myapplication

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
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


//This is the main part of the application, here the outlines of the app are made, aka. the top and bottom bars on the main screen
//and their functionalities. The content itself is scrolled via a pager which uses the CurrentWeatherScreen or WeatherForecastScreen
//based on the page. The dropdown menu with three items, a map that can be opened if permission is granted, the source website and
//the About page itself. The content is set from the appropriate pages but the outline buttons and functions are located here.

@SuppressLint("MissingPermission")  //this part was followed from the video lectures
@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class, ExperimentalPermissionsApi::class
)
@Composable
fun MainApp(navController: NavController) {     //navController for the navigation function

    val isDarkTheme = isSystemInDarkTheme()     //some color coding based on the theme will be done here

    val titleColor = if (isDarkTheme) Color.White else Color.Black
    val headerColor = if (isDarkTheme) Color(202, 231, 254, 150) else { MaterialTheme.colorScheme.primaryContainer}



    // this passes a lambda that returns the number of pages for the pager
    val pagerState = rememberPagerState {
        2 // number of pages in the pager (currentWeather and forecast screens)
    }

    val context=LocalContext.current
    val locationManager= context.getSystemService(LOCATION_SERVICE) as LocationManager
    val locationPermissionState = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION) //location code

    var lat = 89.99      //the default values are set for the north pole, in case of an error, it's clear that there is an issue.
    var lon =179.99
    var newLat=remember { mutableStateOf(0.0) }  //for the other lat and lon value, 0,0 is a point in the Atlantic ocean so again, clear issue.
    var newLon=remember { mutableStateOf(0.0) }


    val currentPage = remember { mutableStateOf(0) }  //following which page we are on
    currentPage.value = pagerState.currentPage

    var locationError by remember { mutableStateOf<String?>(null) }



    LaunchedEffect(Unit) {  //checks for permission on launch and sends a request to the user or error in case of an issue
        if(!(locationPermissionState.status.isGranted)) {
            try {
                locationPermissionState.launchPermissionRequest()
            } catch (e: Exception) {
                locationError = e.message
            }
        }
    }



    Scaffold(          //the scaffolding outline of the app
        topBar = {
            CenterAlignedTopAppBar(    //the top bar has a title and a dropdown menu
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
                actions = {  //code that follows and controls the dropdown menu including its functionalities is located here
                    // a state variable to manage the expanded state of the dropdown menu
                    var expanded by remember { mutableStateOf(false) }

                    Box(
                        modifier = Modifier.fillMaxHeight(), // fills the entire space of the top app bar
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        // Icon button that opens the dropdown menu
                        IconButton(onClick = { expanded = !expanded }) {  //simply opens the menu on click
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Main Menu",
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                    // Dropdown menu
                    DropdownMenu(         //the code that runs the dropdown
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            onClick = {    //the first item in the dropdown is the map opening, so the following code uses the fused provider
                                //and checks for permission, after which it uses the location and opens a map if there is a map app available
                                if (locationPermissionState.status.isGranted)
                                {
                                    val location=locationManager.getLastKnownLocation(LocationManager.FUSED_PROVIDER)
                                    if ((location?.latitude)!=null) {
                                        lat = location.latitude
                                    }
                                    if ((location?.longitude)!=null) {
                                        lon = location.longitude
                                    }



                                    val locationEventListener = object: LocationListener{  //a listener for the location information
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
                                else { //in case the permission is not granted,a dialogue is launched in the phones language
                                    showPermissionAlertDialog(context)
                                }
                                expanded = false
                            },
                            text = { Text(stringResource(R.string.open_map)) }
                        )
                        DropdownMenuItem(  //The menu item that opens the weather map api which is the source of the data
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
                        DropdownMenuItem( //the final dropdown item, a simple navigation tool going to the about page with jetpack navigation
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

        //The bottom bar holds two icons and a dot indicator for the page the user is on. The left icon is the map icon, which
        //Locates the user and updates the data for the users location (google maps has to be opened on the emulator for the location to update)
        //then in the middle two indicator dots showing the page the user is on and finally a floating action button which is just a quick access
        //button to the source link (thus the link icon on it)

        bottomBar = {
            BottomAppBar(containerColor =Color(202, 231, 254, 150)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,

                ) {
                    // left-side map icon
                    IconButton(onClick = {
                                         if (locationPermissionState.status.isGranted)
                                         {
                                             val location=locationManager.getLastKnownLocation(LocationManager.FUSED_PROVIDER)

                                             if (location != null) {
                                                 newLon.value = location.longitude   //the new lon and lat values are used to update the screen and send the
                                                 newLat.value = location.latitude    //required data to the page so that the proper town's weather can be displayed

                                             }

                                             val locationEventListener = object: LocationListener{
                                                 override fun onLocationChanged(location: Location) {  //a listener to update the location if it changes
                                                     if (location!=null)
                                                     {
                                                         lat = location.latitude
                                                         lon = location.longitude
                                                         newLon.value = location.longitude
                                                         newLat.value = location.latitude


                                                     }
                                                 }

                                                 //the following three are required, but the only thing they do is a toast message in some cases
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
                        else {  //again, if permission hasn't been granted, the app will launch a dialogue at the user if they want these features
                            showPermissionAlertDialog(context)

                        }
                    },
                        modifier = Modifier.padding(4.dp)) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = "Map"
                        )
                    }

                    // center Indicator Dots
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

                    // the right floating button for the quick access to the source link
                    FloatingActionButton(
                        onClick = {
                            val url = "https://openweathermap.org/api"
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse(url)
                            if (intent.resolveActivity(context.packageManager) != null) { //safety check to make sure the value is not null
                                context.startActivity(intent)
                             }
                            else {
                                 Toast.makeText(context, "No application found to handle the intent", Toast.LENGTH_LONG).show() //an error caught message
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
        content = { innerPadding ->  //The content shows the proper screen based on the pager values with the indentation adjusted
            val pagerState = rememberPagerState { 2 }
            // the horizontal pager handles swipe navigation
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) { page ->
                // determines the current page and displays the content accordingly
                currentPage.value = page
                when (page) {
                    0 -> CurrentWeatherScreen(newLat.value,newLon.value)
                    1 -> WeatherForecastScreen(newLat.value,newLon.value)
                }
            }
        }
    )
}

fun showPermissionAlertDialog(context : Context) { //the permission dialogue launcher in case the user hasn't accepted already, it displays a message
    val builder = AlertDialog.Builder(context)     //in the users language telling them that the app needs more permissions and how to grant them if they wish

    builder.setTitle(context.getString(R.string.permission_required))
    builder.setMessage(context.getString(R.string.permission_required_text))

    builder.setPositiveButton("OK") { _, _ ->
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }

// creates the dialog and shows it
    val dialog = builder.create()
    dialog.show()
}

