package com.uniovi.tfg.racketFlex.features.home.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.uniovi.tfg.racketFlex.core.model.User
import com.uniovi.tfg.racketFlex.core.network.FirebaseAuthService
import com.uniovi.tfg.racketFlex.features.auth.data.AuthRepositoryImpl
import com.uniovi.tfg.racketFlex.features.auth.domain.AuthRepository
import com.uniovi.tfg.racketFlex.features.home.data.UsersRepositoryImpl
import com.uniovi.tfg.racketFlex.features.home.domain.UsersRepository
import kotlinx.coroutines.launch

class UsersViewModel(
    private val clubId: String,
    private val user: User,
    private val usersRepository: UsersRepository = UsersRepositoryImpl(FirebaseFirestore.getInstance()),
    private val authRepository: AuthRepository = AuthRepositoryImpl(FirebaseAuthService())
) : ViewModel() {
    var searchText by mutableStateOf("")
    var users by mutableStateOf<List<User>>(emptyList())

    val filteredUsers: List<User>
        get() = users.filter {
            it.email.contains(searchText, ignoreCase = true)
        }

    init {
        getUsers()
    }

    fun getUsers() {
        viewModelScope.launch {
            users = usersRepository.getUsers(clubId)
        }
    }

    fun updateUserName(user: User, newName: String) {
        viewModelScope.launch {
            if (newName.isEmpty()) return@launch
            usersRepository.updateUserName(user.email, newName)

            users = users.map {
                if (it.email == user.email) it.copy(name = newName) else it
            }
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            usersRepository.deleteUser(user.email)
            users = users.filter { it.email != user.email }
        }
    }


}

class UsersViewModelFactory(
    private val clubId: String,
    private val user: User
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UsersViewModel(clubId, user) as T
    }
}
