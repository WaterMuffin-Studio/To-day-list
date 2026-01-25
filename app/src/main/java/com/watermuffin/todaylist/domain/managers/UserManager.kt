package com.watermuffin.todaylist.domain.managers

import com.watermuffin.todaylist.data.database.entities.UserEntity
import com.watermuffin.todaylist.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserManager @Inject constructor(
    private val userRepository: UserRepository
) {
    fun getActiveUser(): Flow<UserEntity?> = userRepository.getActiveUser()

    suspend fun getCurrentUserId(): Long? {
        // * надо сделать тут кэширование в будущем
        return userRepository.getActiveUser().firstOrNull()?.id
    }
}