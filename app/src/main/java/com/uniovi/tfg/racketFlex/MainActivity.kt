package com.uniovi.tfg.racketFlex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.uniovi.tfg.racketFlex.core.network.FirebaseAuthService
import com.uniovi.tfg.racketFlex.features.auth.ui.LoginScreen
import com.uniovi.tfg.racketFlex.ui.theme.TFGRacketFlexTheme

class MainActivity : ComponentActivity() {

    private val firebaseAuthService = FirebaseAuthService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TFGRacketFlexTheme {
                LoginScreen()
            }
        }
    }

    /**
     * Permite que si estás loggeado no salga el login
     */
    /*override fun onStart() {
        super.onStart()
        val currentUser = firebaseAuthService.currentUser()
        //if(currentUser != null)
        //navegar a la home

    }*/
}
