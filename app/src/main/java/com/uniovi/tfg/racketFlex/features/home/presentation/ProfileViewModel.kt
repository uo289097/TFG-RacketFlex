package com.uniovi.tfg.racketFlex.features.home.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.uniovi.tfg.racketFlex.core.model.User
import com.uniovi.tfg.racketFlex.features.home.data.ProfileRepositoryImpl
import com.uniovi.tfg.racketFlex.features.home.domain.ProfileRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileViewModel(
    private val user: User,
    private val profileRepository: ProfileRepository = ProfileRepositoryImpl(FirebaseFirestore.getInstance()),
) : ViewModel() {

    var name by mutableStateOf(user.name)
    var actualPassword by mutableStateOf("")
    var newPassword by mutableStateOf("")
    var repeatedPassword by mutableStateOf("")

    var errorMessage by mutableStateOf<String?>(null)

    fun loadUser(user: User) {
        viewModelScope.launch {
            val user = profileRepository.getUser(user.email)
            name = user.name
            actualPassword = ""
            newPassword = ""
            repeatedPassword = ""
            errorMessage = null
        }

    }

    fun saveChanges(onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                errorMessage = null

                val shouldUpdateName = name.trim() != user.name.trim()
                val shouldChangePassword =
                    actualPassword.isNotEmpty() &&
                            newPassword.isNotEmpty() &&
                            repeatedPassword.isNotEmpty()

                if (shouldChangePassword) {
                    if (newPassword != repeatedPassword) {
                        errorMessage = "Las contraseñas no coinciden"
                        return@launch
                    }

                    if (newPassword.length < 6) {
                        errorMessage = "La contraseña debe tener al menos 6 caracteres"
                        return@launch
                    }

                    if (actualPassword == newPassword) {
                        errorMessage = "La contraseña actual no puede ser la misma que la nueva"
                        return@launch
                    }
                }

                if (shouldChangePassword) {
                    changePassword()
                }

                if (shouldUpdateName) {
                    profileRepository.updateName(user.email, name)
                }
                if (errorMessage == null)
                    onSuccess()

            } catch (e: Exception) {
                errorMessage = e.message ?: "Error al guardar cambios"
                Log.d("ProfileViewModel", "Error al guardar cambios: ${e.message}")
            }
        }
    }

    suspend fun changePassword() {
        val user = FirebaseAuth.getInstance().currentUser

        if (user == null || user.email == null) {
            return
        }

        val credential = EmailAuthProvider.getCredential(
            user.email!!,
            actualPassword
        )

        user.reauthenticate(credential).await()
        user.updatePassword(newPassword).await()
    }

}

class ProfileViewModelFactory(private val user: User) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(user) as T
    }
}