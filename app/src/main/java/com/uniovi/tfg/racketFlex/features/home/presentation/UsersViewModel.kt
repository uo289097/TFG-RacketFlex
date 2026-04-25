package com.uniovi.tfg.racketFlex.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.uniovi.tfg.racketFlex.core.model.User
import com.uniovi.tfg.racketFlex.features.home.data.UsersRepositoryImpl
import com.uniovi.tfg.racketFlex.features.home.domain.UsersRepository
import kotlinx.coroutines.launch

class UsersViewModel(
    private val clubId: String,
    private val user: User,
    private val usersRepository: UsersRepository = UsersRepositoryImpl(FirebaseFirestore.getInstance()),
) : ViewModel() {

    init {
        viewModelScope.launch {
            usersRepository.getUsers(clubId)
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
