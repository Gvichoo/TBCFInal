package com.tbacademy.nextstep.domain.core

import com.tbacademy.nextstep.R

sealed class StringResource(val errorMessageResId: Int) {
    object AllFieldsRequired : StringResource(R.string.all_fields_are_required)
    object InvalidEmail : StringResource(R.string.please_enter_a_valid_email_address)
    object PasswordTooShort : StringResource(R.string.password_must_be_at_least_8_characters)
    object LoginFailed : StringResource(R.string.login_failed)
    object PasswordsDoNotMatch : StringResource(R.string.passwords_do_not_match)
    object RegistrationFailed : StringResource(R.string.registration_failed)
    object LanguageChangedTo : StringResource(R.string.language_changed_to)
}