package com.example.itc__onl2_swd4_s3_1.ui.ui.Home


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = {  /* add habit screen here*/ },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondary,
            ) {
                Icon(Icons.Default.Add , contentDescription = "add")
            }
        },
        floatingActionButtonPosition = FabPosition.Center,



        bottomBar = {
            NavigationBar {
                navItemList.forEachIndexed{index, navItem ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = {
                            selectedIndex = index
                        },
                        icon = {
                            Icon( navItem.icon , "icon")
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
