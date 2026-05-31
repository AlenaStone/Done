package com.done.app.data.repository

import com.done.app.data.local.AssignmentDao
import com.done.app.data.model.Assignment
import kotlinx.coroutines.flow.Flow

class AssignmentRepository(
    private val assignmentDao: AssignmentDao
) {

    fun getAssignmentForCourse(courseId: Int): Flow<List<Assignment>> {
        return assignmentDao.getAssignmentForCourse(courseId)
    }

    fun getAllAssignments(): Flow<List<Assignment>> {
        return assignmentDao.getAllAssignments()
    }

    suspend fun insertAssignment(
        assignment: Assignment
    ) {
        assignmentDao.insertAssignment(assignment)
    }

    suspend fun deleteAssignment(
        assignment: Assignment
    ) {
        assignmentDao.deleteAssignment(assignment)
    }

    suspend fun updateAssignment(
        assignment: Assignment
    ) {
        assignmentDao.updateAssignment(assignment)
    }
}
