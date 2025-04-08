package com.tbacademy.nextstep.presentation.extension

import com.tbacademy.nextstep.domain.core.InputValidationResult
import com.tbacademy.nextstep.presentation.common.mapper.toErrorMessageRes

fun InputValidationResult.getErrorMessageResId(): Int? {
    return when (this) {
        is InputValidationResult.Failure -> this.error.toErrorMessageRes()
        else -> null
    }
}