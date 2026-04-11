package com.uniovi.tfg.racketFlex.features.courses.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.uniovi.tfg.racketFlex.core.model.Sport
import com.uniovi.tfg.racketFlex.features.courses.data.CoursesRepositoryImpl
import com.uniovi.tfg.racketFlex.features.courses.domain.CoursesRepository
import com.uniovi.tfg.racketFlex.features.courses.domain.entities.Course
import kotlinx.coroutines.launch

class CoursesViewModel(
    private val clubId: String,
    private val coursesRepository: CoursesRepository = CoursesRepositoryImpl(FirebaseFirestore.getInstance()),
) : ViewModel() {
    var selectedSport by mutableStateOf<Sport?>(null)
    var courses by mutableStateOf<List<Course>>(emptyList())
    var userCourses by mutableStateOf<List<Course>>(emptyList())

    fun selectSport(sport: Sport) {
        selectedSport = sport
    }

    fun loadCourses() {
        val sport = selectedSport ?: return
        viewModelScope.launch {
            courses = coursesRepository.getCourses(clubId, sport)
        }
    }

    fun joinCourse(courseId: String, userId: String) {
        viewModelScope.launch {
            coursesRepository.joinCourse(clubId, courseId, userId)
            loadCourses()
        }
    }

    fun loadUserCourses(userId: String) {
        val sport = selectedSport ?: return
        viewModelScope.launch {
            userCourses = coursesRepository.getUserCourses(clubId, sport, userId)
        }
    }

}

class CoursesViewModelFactory(private val clubId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CoursesViewModel(clubId) as T
    }
}