package com.uniovi.tfg.racketFlex.features.courses.domain

import com.uniovi.tfg.racketFlex.core.model.BookingInfo
import com.uniovi.tfg.racketFlex.core.model.Sport
import com.uniovi.tfg.racketFlex.features.courses.domain.entities.Course

interface CoursesRepository {
    suspend fun getCourses(clubId: String, sport: Sport): List<Course>
    suspend fun joinCourse(clubId: String, courseId: String, userId: String)
    suspend fun getUserCourses(clubId: String, sport: Sport, userId: String): List<Course>
    suspend fun cancelCourseInscription(clubId: String, userId: String, courseId: String)
    suspend fun loadAdminCourses(clubId: String, sport: Sport): List<Course>
    suspend fun createCourse(clubId: String, course: Course)
    suspend fun removeCourse(clubId: String, courseId: String)
    suspend fun getBookingInfo(clubId: String): BookingInfo
}
