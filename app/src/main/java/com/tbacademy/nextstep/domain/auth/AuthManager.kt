package com.tbacademy.nextstep.domain.auth

interface AuthManager {
    fun getCurrentUserId(): String?
}