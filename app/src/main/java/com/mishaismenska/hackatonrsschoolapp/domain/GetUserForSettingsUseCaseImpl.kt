package com.mishaismenska.hackatonrsschoolapp.domain

import android.icu.util.Measure
import com.mishaismenska.hackatonrsschoolapp.data.interfaces.AppDataRepository
import com.mishaismenska.hackatonrsschoolapp.presentation.interfaces.GetUserForSettingsUseCase
import com.mishaismenska.hackatonrsschoolapp.presentation.models.UserSettingsUIModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import javax.inject.Inject

class GetUserForSettingsUseCaseImpl @Inject constructor(private val appDataRepository: AppDataRepository) :
    GetUserForSettingsUseCase {
    override suspend fun getUser(): UserSettingsUIModel {
        var user: UserSettingsUIModel? = null
        appDataRepository.getUser().take(1).collect {
            user =
                UserSettingsUIModel(Measure(it[0].weight, it[0].unit), it[0].userName, it[0].gender)
        }
        return user!!
    }
}