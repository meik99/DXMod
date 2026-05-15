package com.rynkix.dxmod

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.rynkix.dxmod.ui.theme.DXModTheme

import androidx.navigation.compose.rememberNavController
import androidx.room.Room

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Companion.applicationContext = this

        enableEdgeToEdge()
        setContent {
            AppScaffold()
        }
    }

    companion object {
        lateinit var applicationContext: Context
            private set

        val db: AppDatabase by lazy {
            Room.databaseBuilder(applicationContext, AppDatabase::class.java, "dxmod").build()
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun AppScaffold() {
    val navController = rememberNavController()

    DXModTheme {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                DrawerContent(drawerState, navController)
            },
        ) {
            Scaffold(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .safeDrawingPadding(),
                topBar = {
                    AppBar(drawerState)
                },
            ) { innerPadding ->
                Navigation(navController, innerPadding)
            }
        }
    }
}
