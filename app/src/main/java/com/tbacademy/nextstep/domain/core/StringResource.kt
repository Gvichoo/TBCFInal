package com.tbacademy.nextstep.domain.core

import com.tbacademy.nextstep.R

sealed class StringResource(val errorMessageResId: Int) {
    data object AllFieldsRequired : StringResource(R.string.all_fields_are_required)
    data object InvalidEmail : StringResource(R.string.please_enter_a_valid_email_address)
    data object PasswordTooShort : StringResource(R.string.password_must_be_at_least_8_characters)
    data object LoginFailed : StringResource(R.string.login_failed)
    data object PasswordsDoNotMatch : StringResource(R.string.passwords_do_not_match)
    data object RegistrationFailed : StringResource(R.string.registration_failed)
    data object LanguageChangedTo : StringResource(R.string.language_changed_to)
}