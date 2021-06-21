package school.cactus.succulentshop.signup.validation

import school.cactus.succulentshop.R
import school.cactus.succulentshop.validation.Validator

class EmailValidator : Validator {
    override fun validate(field: String): Int? = when {
        field.isEmpty() -> R.string.emailRequired
        !field.emailCharacterControl() -> R.string.invalidEmail
        field.length <= 5 -> R.string.invalidEmail
        field.length >= 50 -> R.string.invalidEmail
        else -> null
    }

    private fun String.emailCharacterControl() = any { it == '@' } && any { it == '.' }
}