package com.project.edu_law.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.project.edu_law.ui.navigation.Screen
import com.project.edu_law.ui.theme.*

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(Screen.Home, Screen.Scenario)

    Surface(
//        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        color = White,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        NavigationBar(
            containerColor = White,
            modifier = Modifier.height(80.dp),
            tonalElevation = 0.dp
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            items.forEach { screen ->
                val isSelected = currentRoute == screen.route

                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    label = {
                        Text(
                            text = screen.title,
                            color = if (isSelected) BluePrimary else GrayText,
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    icon = {
                        Icon(
                            imageVector = screen.icon,
                            contentDescription = screen.title,
                            tint = if (isSelected) BluePrimary else GrayText
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent,
                        selectedIconColor = BluePrimary,
                        unselectedIconColor = GrayText,
                        selectedTextColor = BluePrimary,
                        unselectedTextColor = GrayText
                    )
                )
            }
        }
    }
}

@Preview
@Composable
fun BottomNavigationPreview() {
    val navController = rememberNavController()
    BottomNavigationBar(navController)
}