package com.rynkix.dxmod

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.rynkix.dxmod.ui.theme.GreenGrey40
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun DrawerContent(drawerState: DrawerState, navController: NavHostController) {
    val coroutineScope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(12.dp))
            Text(
                "DXMod",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleLarge
            )
            HorizontalDivider()

            // Text("Section 1", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
            NavigationDrawerItem(
                label = { Text("Chat") },
                selected = navBackStackEntry?.destination?.hasRoute(Roller::class) ?: false,
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = GreenGrey40
                ),
                onClick = {
                    navController.navigate(route = Roller)

                    coroutineScope.launch {
                        drawerState.close()
                    }
                }
            )
            NavigationDrawerItem(
                label = { Text("Actions") },
                selected = navBackStackEntry?.destination?.hasRoute(Actions::class) ?: false,
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = GreenGrey40
                ),
                onClick = {
                    navController.navigate(route = Actions)

                    coroutineScope.launch {
                        drawerState.close()
                    }
                }
            )
        }

    }
}