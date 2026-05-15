package com.rynkix.dxmod

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rynkix.dxmod.actions.ActionScreen
import com.rynkix.dxmod.roller.RollerScreen
import kotlinx.serialization.Serializable

@Serializable
object Roller

@Serializable
object Actions

@Composable
fun Navigation(
    navController: NavHostController,
    innerPadding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = Actions
    ) {
        composable<Roller> {
            RollerScreen(
                modifier = Modifier.padding(innerPadding)
            )
        }
        composable<Actions> {
            ActionScreen(
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}