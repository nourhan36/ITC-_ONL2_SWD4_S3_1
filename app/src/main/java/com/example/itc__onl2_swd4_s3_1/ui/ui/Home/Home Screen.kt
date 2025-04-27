package com.example.itc__onl2_swd4_s3_1.ui.Home
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add

import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.itc__onl2_swd4_s3_1.R
import com.example.itc__onl2_swd4_s3_1.ui.ui.Home.NavItem
import com.example.itc__onl2_swd4_s3_1.ui.ui.Home.getCurrentDate
import kotlinx.coroutines.launch


class HomeScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            navBar()
        }
    }
}


@Composable
fun navBar(modifier: Modifier = Modifier) {
    val navItemList = listOf(
        NavItem("Home", painterResource(R.drawable.home) ),
        NavItem("Salah" , painterResource(R.drawable.salah_icon)),
        NavItem("Dhikr", painterResource(R.drawable.zikr)),
        NavItem("Streak", painterResource(R.drawable.progress)),
        NavItem("More", painterResource(R.drawable.menu) )
    )
    var selectedIndex by remember {
        mutableIntStateOf(0)
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var isDarkTheme by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf("English") }


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerHeader()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Top
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { /* handle click */ }
                            .padding(vertical = 12.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.qiblah), // replace with your image
                            contentDescription = "Qiblah",
                            modifier = Modifier.size(30.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "Qiblah", fontSize = 18.sp)
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.lightdark), // replace with your image
                            contentDescription = "light/dark mode",
                            modifier = Modifier.size(35.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Dark Mode",  fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(150.dp))

                        Switch(
                            checked = isDarkTheme,
                            onCheckedChange = { isDarkTheme = it }
                        )
                    }

                    //Text("Language", modifier = Modifier.padding(16.dp))
                    LanguageSelector(
                        selectedLanguage = selectedLanguage,
                        onLanguageSelected = {
                            selectedLanguage = it
                            // Optional: Save it to preferences or handle localization
                        }
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { /* go to sign in page */ }
                            .padding(vertical = 12.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logout), // replace with your image
                            contentDescription = "logout",
                            modifier = Modifier.size(35.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "Logout", fontSize = 18.sp)
                    }                    // Add more items as needed
                }
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                LargeFloatingActionButton(
                    onClick = {  /* add habit screen here*/ },
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                ) {
                    Icon(Icons.Default.Add, contentDescription = "add")
                }
            },
            floatingActionButtonPosition = FabPosition.Center,


            bottomBar = {
                NavigationBar {
                    navItemList.forEachIndexed { index, navItem ->
                        NavigationBarItem(
                            selected = selectedIndex == index,
                            onClick = {
                                when(index){
                                    0 -> {selectedIndex = index
                                            //go to home screen
                                    }
                                    1 -> {
                                        selectedIndex = index
                                        //go to salah screen
                                    }
                                    2 -> {
                                        selectedIndex = index
                                        //go to dhikr screen
                                    }
                                    3 -> {
                                        selectedIndex = index
                                        //go to streak screen
                                    }
                                    4 -> {
                                        scope.launch {
                                            drawerState.open()
                                        }
                                        selectedIndex = index
                                    //go to salah screen

                                        }
                                }
//                                if (selectedIndex == 4) {
//                                    selectedIndex = index
//                                    scope.launch {
//                                        drawerState.open()
//                                    }
//                                }
//                                else{
//                                    selectedIndex = index
//                                }
                            },
                            icon = {
                                Icon(navItem.icon, "icon")
                            },
                            label = {
                                Text(
                                    text = navItem.label
                                )
                            }
                        )
                    }
                }
            }
        )

//comment trial

        { innerPadding ->
            Content(modifier = Modifier.padding(innerPadding))
        }
    }
}


@Preview
@Composable
private fun previewMain() {
    navBar()
}

@Composable
fun Content(modifier: Modifier = Modifier) {

    Image(
        painter = painterResource(id = R.drawable.ramadan_img),
        contentDescription = "Ramadan Kareem"
    )
    Column {
        Text(
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .padding(top = 22.dp)
                .fillMaxWidth(),
            text = "Ramadan Habit Tracker",
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 30.sp
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        )
        {
            Text(
                modifier = Modifier
                    .padding(horizontal = 15.dp),
                text = getCurrentDate(),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
            Column {
                Text(
                    text = "Cairo",
                    fontSize = 20.sp,
                )
                Text(
                    text = "Egypt",
                    fontSize = 20.sp,
                )
            }


        }
        Spacer(Modifier.padding(top=150.dp))
        Row(
            modifier=Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            var selectedIndex2 by remember { mutableStateOf(-1) }
            Text(
                modifier=Modifier.clickable {
                    selectedIndex2 = 0
                    /*go to all habits screen*/ }
                    .border(
                        border = if (selectedIndex2 == 0) BorderStroke(2.dp, Color.Blue) else BorderStroke(0.dp, Color.Transparent),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(8.dp),
                fontSize = 25.sp,
                text = "ALL",
                color = if (selectedIndex2 == 0) Color.Red else Color.Black,
            )
            Text(
                modifier=Modifier.clickable {
                    selectedIndex2 = 1
                    /*go to all habits screen*/ }
                    .border(
                        border = if (selectedIndex2 == 1) BorderStroke(2.dp, Color.Blue) else BorderStroke(0.dp, Color.Transparent),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(8.dp),
                fontSize = 25.sp,
                text = "Complete",
                color = if (selectedIndex2 == 1) Color.Red else Color.Black,

                )
            Text(
                modifier=Modifier.clickable {
                    selectedIndex2 = 2
                    /*go to all habits screen*/ }
                    .border(
                        border = if (selectedIndex2 == 2) BorderStroke(2.dp, Color.Blue) else BorderStroke(0.dp, Color.Transparent),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(8.dp),
                fontSize = 25.sp,
                text = "Incomplete",
                color = if (selectedIndex2 == 2) Color.Red else Color.Black,

                )
        }


    }

}

@Composable
fun DrawerHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Example: User profile image (optional)
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Profile",
            tint = Color.White,
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Welcome,",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "user name",
            color = Color.White.copy(alpha = 0.7f),
            style = MaterialTheme.typography.bodySmall
        )
    }
}


@Composable
fun LanguageSelector(
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }


    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = true }
            .padding( vertical = 12.dp ,)
    ) {
        Image(
            painter = painterResource(id = R.drawable.language),
            contentDescription = "language icon",
            modifier = Modifier.size(35.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = selectedLanguage,
            fontSize = 18.sp,
        )

        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = "Dropdown Icon",
            modifier = Modifier.size(24.dp)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("English") },
                onClick = {
                    onLanguageSelected("English")
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("العربية") },
                onClick = {
                    onLanguageSelected("العربية")
                    expanded = false
                }
            )
        }
    }
}

