package com.uniovi.tfg.racketFlex.features.home.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.uniovi.tfg.racketFlex.core.model.User
import com.uniovi.tfg.racketFlex.core.model.UserRole
import com.uniovi.tfg.racketFlex.core.network.FirebaseAuthService
import com.uniovi.tfg.racketFlex.features.auth.data.AuthRepositoryImpl
import com.uniovi.tfg.racketFlex.features.auth.domain.AuthRepository
import com.uniovi.tfg.racketFlex.features.home.data.UsersRepositoryImpl
import com.uniovi.tfg.racketFlex.features.home.domain.UsersRepository
import kotlinx.coroutines.launch

class AddUserViewModel(
    private val clubId: String,
    private val usersRepository: UsersRepository = UsersRepositoryImpl(FirebaseFirestore.getInstance()),
    private val authRepository: AuthRepository = AuthRepositoryImpl(FirebaseAuthService())
) : ViewModel() {

    var email by mutableStateOf("")
    var name by mutableStateOf("")
    var password by mutableStateOf("")
    var repeatedPassword by mutableStateOf("")
    var errorMessage by mutableStateOf<String?>(null)
    var userRole by mutableStateOf<UserRole?>(null)

    fun selectRole(role: UserRole) {
        userRole = role
    }

    fun createUser(onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                errorMessage = null
                if (email.isBlank()) {
                    errorMessage = "El email no puede estar vacío"
                    return@launch
                }
                if (name.isBlank()) {
                    errorMessage = "El nombre no puede estar vacío"
                    return@launch
                }
                if (password.length < 6) {
                    errorMessage = "La contraseña debe tener al menos 6 caracteres"
                    return@launch
                }
                if (password != repeatedPassword) {
                    errorMessage = "Las contraseñas deben ser iguales"
                    return@launch
                }
                if (userRole == null) {
                    errorMessage = "Seleccione un rol para el usuario"
                    return@launch
                }
                if (usersRepository.checkUser(email, clubId)) {
                    errorMessage = "Ya existe un usuario con este email"
                    return@launch
                }
                val user = User(
                    email = email,
                    club = clubId,
                    name = name,
                    role = userRole!!
                )
                authRepository.createUser(email, password)
                usersRepository.createUser(user)

                if (errorMessage == null)
                    onSuccess()
            } catch (e: Exception) {
                errorMessage = e.message ?: "Error al guardar cambios"
                Log.d("AddUserViewModel", "Error al guardar cambios: ${e.message}")
            }
        }
    }


}

class AddUserViewModelFactory(
    private val clubId: String,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddUserViewModel(clubId) as T
    }
}