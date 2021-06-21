package school.cactus.succulentshop.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import school.cactus.succulentshop.R
import school.cactus.succulentshop.Result.*
import school.cactus.succulentshop.auth.JwtStore
import school.cactus.succulentshop.infra.BaseViewModel
import school.cactus.succulentshop.infra.snackbar.SnackbarAction
import school.cactus.succulentshop.infra.snackbar.SnackbarState
import school.cactus.succulentshop.signup.validation.EmailValidator
import school.cactus.succulentshop.signup.validation.PasswordValidator
import school.cactus.succulentshop.signup.validation.UsernameValidator

class SignupViewModel(
    private val store: JwtStore,
    private val repository: SignupRepository
) : BaseViewModel() {

    private val emailValidator = EmailValidator()
    private val passwordValidator = PasswordValidator()
    private val usernameValidator = UsernameValidator()

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val username = MutableLiveData<String>()

    private val _emailErrorMessage = MutableLiveData<Int>()
    private val _passwordErrorMessage = MutableLiveData<Int>()
    private val _usernameErrorMessage = MutableLiveData<Int>()

    val emailErrorMessage: LiveData<Int> = _emailErrorMessage
    val passwordErrorMessage: LiveData<Int> = _passwordErrorMessage
    val usernameErrorMessage: LiveData<Int> = _usernameErrorMessage

    fun onSignupButtonClick() = viewModelScope.launch {
        if (isEmailValid() and isPasswordValid() and isUsernameValid()) {
            val result = repository.sendSignupRequest(
                email.value.orEmpty(),
                password.value.orEmpty(),
                username.value.orEmpty()
            )

            when (result) {
                is Success -> onSuccess(result.value)
                is ClientError -> onClientError(result.value)
                UnexpectedError -> onUnexpectedError()
                Failure -> onFailure()
            }
        }
    }

    private fun onSuccess(jwt: String) {
        store.saveJwt(jwt)
        navigateToProductsPage()
    }

    private fun onClientError(errorMessage: String) {
        _snackbarState.value = SnackbarState(
            error = errorMessage,
            duration = BaseTransientBottomBar.LENGTH_LONG
        )
    }

    private fun onUnexpectedError() {
        _snackbarState.value = SnackbarState(
            errorRes = R.string.unexpected_error_occurred,
            duration = BaseTransientBottomBar.LENGTH_LONG
        )
    }

    private fun onFailure() {
        _snackbarState.value = SnackbarState(
            errorRes = R.string.check_your_connection,
            duration = Snackbar.LENGTH_INDEFINITE,
            action = SnackbarAction(
                text = R.string.retry,
                action = {
                    onSignupButtonClick()
                }
            )
        )
    }

    fun navigateToLoginPage() {
        val directions = SignupFragmentDirections.signupToLogin()
        navigation.navigate(directions)
    }

    private fun navigateToProductsPage() {
        val directions = SignupFragmentDirections.signUpSuccessful()
        navigation.navigate(directions)
    }

    private fun isEmailValid(): Boolean {
        _emailErrorMessage.value = emailValidator.validate(email.value.orEmpty())
        return _emailErrorMessage.value == null
    }

    private fun isPasswordValid(): Boolean {
        _passwordErrorMessage.value = passwordValidator.validate(password.value.orEmpty())
        return _passwordErrorMessage.value == null
    }

    private fun isUsernameValid(): Boolean {
        _usernameErrorMessage.value = usernameValidator.validate(username.value.orEmpty())
        return _usernameErrorMessage.value == null
    }
}