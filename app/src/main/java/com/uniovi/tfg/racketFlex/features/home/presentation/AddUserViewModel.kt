package com.uniovi.tfg.racketFlex.features.home.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.uniovi.tfg.racketFlex.core.model.UserRole
import com.uniovi.tfg.racketFlex.features.home.data.UsersRepositoryImpl
import com.uniovi.tfg.racketFlex.features.home.domain.UsersRepository

class AddUserViewModel(
    private val clubId: String,
    private val usersRepository: UsersRepository = UsersRepositoryImpl(FirebaseFirestore.getInstance()),
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


}

class AddUserViewModelFactory(
    private val clubId: String,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddUserViewModel(clubId) as T
    }
}