package school.cactus.succulentshop.signup.validation

import school.cactus.succulentshop.R
import school.cactus.succulentshop.validation.Validator

class UsernameValidator : Validator {
    override fun validate(field: String): Int? = when {
        field.isEmpty() -> R.string.userNameRequired
        !field.userNameCharacterControl() -> R.string.badUserNameFormat
        field.length <= 2 -> R.string.shortUserName
        field.length >= 20 -> R.string.longUserName
        else -> null
    }

    private fun String.userNameCharacterControl() = all { it.isLetterOrDigit() || it == '_' }
}

