package com.done.app.data.repository

import com.done.app.data.local.ExamDao
import com.done.app.data.model.Exam
import kotlinx.coroutines.flow.Flow

class ExamRepository(
    private val examDao: ExamDao
) {

    fun getExamsForCourse(courseId: Int): Flow<List<Exam>> {
        return examDao.getExamsForCourse(courseId)
    }

    fun getAllExams(): Flow<List<Exam>> {
        return examDao.getAllExams()
    }

    suspend fun insertExam(
        exam: Exam
    ) {
        examDao.insertExam(exam)
    }

    suspend fun deleteExam(
        exam: Exam
    ) {
        examDao.deleteExam(exam)
    }

    suspend fun updateExam(
        exam: Exam
    ) {
        examDao.updateExam(exam)
    }
}
