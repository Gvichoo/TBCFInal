package com.tbacademy.nextstep.domain.core

import com.tbacademy.nextstep.R

sealed class InputValidationError(val errorMessageResId: Any) {
    // General
    data object Empty : InputValidationError(R.string.field_is_empty)
    // Email
    data object InvalidEmailFormat : InputValidationError(R.string.invalid_email_format)
    // Username
    data object UsernameShort :
        InputValidationError(R.string.username_must_be_at_least_4_characters)
    data object UsernameLong :
        InputValidationError(R.string.nickname_must_be_no_longer_than_20_characters)
    data object InvalidUsernameFormat :
        InputValidationError(R.string.nickname_can_only_contain_letters_numbers_and_underscores)
    // Password
    data object PasswordShort :
        InputValidationError(R.string.password_should_be_at_least_8_characters)
    data object PasswordWeak :
        InputValidationError(R.string.password_must_contain_at_least_one_letter_and_one_digit)
    data object PasswordsDoNotMatch : InputValidationError(R.string.passwords_do_not_match)

}