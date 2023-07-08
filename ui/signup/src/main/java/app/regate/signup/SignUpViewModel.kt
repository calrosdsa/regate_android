package app.regate.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import me.tatarka.inject.annotations.Inject

@Inject
class SignUpViewModel (): ViewModel(){
    private val genero = MutableStateFlow<Genero?>(null)
    private val birtDay = MutableStateFlow<String>("")

    val state:StateFlow<SignUpState> = combine(
        genero,
        birtDay
    ){genero,birthDay->
        SignUpState(
            genero = genero,
            birthDay = birthDay
        )
    }.stateIn(
        scope =viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue =SignUpState.Empty
    )

    init {
//        setGenero(Genero.MALE)
    }

    fun setGenero(value:Genero?){
        genero.tryEmit(value)
    }
}