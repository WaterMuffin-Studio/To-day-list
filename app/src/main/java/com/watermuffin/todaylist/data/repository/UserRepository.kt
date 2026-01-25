package com.watermuffin.todaylist.data.repository

import com.watermuffin.todaylist.data.database.dao.UserDao
import com.watermuffin.todaylist.data.database.entities.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao
) {
    fun getAllUsers(): Flow<List<UserEntity>> = userDao.getAllUsers()

    fun getUserById(userId: Long): Flow<UserEntity> = userDao.getUserById(userId)

    fun getActiveUser(): Flow<UserEntity?> = userDao.getActiveUser()

    suspend fun createUser(name: String, avatarFileName: String?): Long {
        userDao.deactivateAllUsers()

        val user = UserEntity(
            name = name,
            avatarFileName = avatarFileName
        )
        return userDao.insertUser(user)
    }

    suspend fun switchUser(userId: Long) {
        userDao.deactivateAllUsers()
        userDao.activateUser(userId)
    }

    suspend fun updateUserName(userId: Long, newName: String): Boolean {
        return try {
            val user = userDao.getUserById(userId)
            user.let {
                val updatedUser = it.first().copy(name = newName)
                userDao.updateUser(updatedUser)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun updateUserAvatar(userId: Long, avatarFileName: String?): Boolean {
        return try {
            val user = userDao.getUserById(userId)
            user.let {
                val updatedUser = it.first().copy(avatarFileName = avatarFileName)
                userDao.updateUser(updatedUser)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

}