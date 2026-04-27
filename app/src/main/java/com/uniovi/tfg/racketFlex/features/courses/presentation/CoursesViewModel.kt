package com.uniovi.tfg.racketFlex.features.courses.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.uniovi.tfg.racketFlex.core.model.BookingInfo
import com.uniovi.tfg.racketFlex.core.model.Sport
import com.uniovi.tfg.racketFlex.features.courses.data.CoursesRepositoryImpl
import com.uniovi.tfg.racketFlex.features.courses.domain.CoursesRepository
import com.uniovi.tfg.racketFlex.features.courses.domain.entities.Course
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalTime

class CoursesViewModel(
    private val clubId: String,
    private val coursesRepository: CoursesRepository = CoursesRepositoryImpl(FirebaseFirestore.getInstance()),
) : ViewModel() {
    var selectedSport by mutableStateOf<Sport?>(null)
    var courses by mutableStateOf<List<Course>>(emptyList())
    var adminCourses by mutableStateOf<List<Course>>(emptyList())
    var userCourses by mutableStateOf<List<Course>>(emptyList())

    var title by mutableStateOf("")
    var description by mutableStateOf("")
    var sport by mutableStateOf<Sport?>(null)
    var maxPlayers by mutableStateOf("")
    var price by mutableStateOf("")
    var selectedDays by mutableStateOf(setOf<DayOfWeek>())

    var startDate by mutableStateOf<Long?>(null)
    var endDate by mutableStateOf<Long?>(null)

    var startTime by mutableStateOf<LocalTime?>(null)
    var endTime by mutableStateOf<LocalTime?>(null)

    var errorMessage by mutableStateOf<String?>(null)

    var bookingInfo by mutableStateOf<BookingInfo?>(null)


    init {
        getBookingInfo()
    }

    fun getBookingInfo() {
        viewModelScope.launch {
            bookingInfo = coursesRepository.getBookingInfo(clubId)
        }
    }

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
            loadUserCourses(userId)
        }
    }

    fun loadAdminCourses() {
        val sport = selectedSport ?: return
        viewModelScope.launch {
            adminCourses = coursesRepository.loadAdminCourses(clubId, sport)
        }
    }

    fun loadUserCourses(userId: String) {
        val sport = selectedSport ?: return
        viewModelScope.launch {
            userCourses = coursesRepository.getUserCourses(clubId, sport, userId)
        }
    }

    fun cancelCourseInscription(courseId: String, userId: String) {
        viewModelScope.launch {
            coursesRepository.cancelCourseInscription(clubId, userId, courseId)
            loadCourses()
            loadUserCourses(userId)
        }
    }

    fun removeCourse(courseId: String) {
        viewModelScope.launch {
            coursesRepository.removeCourse(clubId, courseId)
            loadAdminCourses()
        }
    }

    fun toggleDay(day: DayOfWeek) {
        selectedDays = if (selectedDays.contains(day)) {
            selectedDays - day
        } else {
            selectedDays + day
        }
    }

    fun createCourse() {
        createCourseValidation()
        if (errorMessage != null) return

        val course = Course(
            id = "",
            title = title,
            description = description,
            maxPlayers = maxPlayers.toInt(),
            players = emptyList(),
            sport = sport!!,
            price = price.replace(",", ".").toDouble(),
            initDate = startDate!!,
            endDate = endDate!!,
            daysOfWeek = selectedDays.toList(),
            startTime = startTime,
            endTime = endTime,
        )
        viewModelScope.launch {
            coursesRepository.createCourse(clubId, course)
            clearFields()
            loadAdminCourses()
        }
    }

    private fun clearFields() {
        title = ""
        description = ""
        sport = null
        maxPlayers = ""
        price = ""
        selectedDays = setOf()
        startDate = null
        endDate = null
        startTime = null
        endTime = null
        errorMessage = null
    }

    private fun createCourseValidation() {
        errorMessage = when {
            title.isBlank() -> "El título es obligatorio"
            description.isBlank() -> "La descripción es obligatoria"
            sport == null -> "Selecciona un deporte"
            maxPlayers.toIntOrNull() == null -> "El número de jugadores no es válido"
            price.replace(",", ".").toDoubleOrNull() == null -> "El precio no es válido"
            selectedDays.isEmpty() -> "Selecciona al menos un día"
            startDate == null || endDate == null -> "Selecciona las fechas del curso"
            startTime == null -> "Selecciona la hora de inicio"
            endTime == null -> "Selecciona la hora de fin"
            else -> null
        }
    }

}

class CoursesViewModelFactory(private val clubId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CoursesViewModel(clubId) as T
    }
}