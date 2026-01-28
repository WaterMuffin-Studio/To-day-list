package com.watermuffin.todaylist.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.watermuffin.todaylist.data.database.entities.UserEntity
import com.watermuffin.todaylist.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val users: StateFlow<List<UserEntity>?> = userRepository
        .getAllUsers()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )


    val activeUser: StateFlow<UserEntity?> = userRepository
        .getActiveUser()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    suspend fun selectUser(userId: Long) {
        userRepository.switchUser(userId)
    }

    suspend fun createUser(name: String, avatarFileName: String?): Long {
        return userRepository.createUser(name, avatarFileName)
    }

    suspend fun updateUserName(userId: Long, newName: String) {
        userRepository.updateUserName(userId, newName)
    }

    suspend fun updateUserAvatar(userId: Long, avatarFileName: String?) {
        userRepository.updateUserAvatar(userId, avatarFileName)
    }

    fun getUser(userId: Long): StateFlow<UserEntity?> = userRepository
        .getUserById(userId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
}