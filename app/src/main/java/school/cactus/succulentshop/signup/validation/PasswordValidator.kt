package school.cactus.succulentshop.signup.validation

import school.cactus.succulentshop.R
import school.cactus.succulentshop.validation.Validator

class PasswordValidator : Validator {
    override fun validate(field: String): Int? = when {
        field.isEmpty() -> R.string.passwordRequired
        field.length <= 7 -> R.string.shortPassword
        field.length >= 40 -> R.string.longPassword
        !field.passwordCharacterControl() -> R.string.badEmailFormat
        else -> null
    }

    private fun String.passwordCharacterControl() =
        isContainsLowerCase() && isContainsUpperCase() && isContainsSpecialCharacter() && isContainsDigit()

    private fun String.isContainsLowerCase() = any { it.isLowerCase() }
    private fun String.isContainsUpperCase() = any { it.isUpperCase() }
    private fun String.isContainsDigit() = any { it.isDigit() }
    private fun String.isContainsSpecialCharacter() = any { !it.isLetterOrDigit() }
}