package com.mishaismenska.hackatonrsschoolapp.domain.interfaces

import com.mishaismenska.hackatonrsschoolapp.domain.models.DrinkDomainModel
import com.mishaismenska.hackatonrsschoolapp.domain.models.UserDomainModel
import com.mishaismenska.hackatonrsschoolapp.domain.models.UserWithDrinksDomainModel
import com.mishaismenska.hackatonrsschoolapp.staticPresets.Gender
import kotlinx.coroutines.flow.Flow

interface AppDataRepository {
    suspend fun getUserWithDrinks(): UserWithDrinksDomainModel?
    suspend fun getUser(): Flow<List<UserDomainModel>>
    suspend fun getDrinks(): Flow<List<DrinkDomainModel>>
    suspend fun addDrink(drinkDomainModel: DrinkDomainModel)
    suspend fun deleteDrink(recyclerPosition: Int)
    suspend fun addUser(age: Int, weight: Double, gender: Gender)
    suspend fun setUserName(newName: String)
    suspend fun reset()
    suspend fun setWeight(newValue: Double)
    suspend fun setGender(newValue: Int)
}
