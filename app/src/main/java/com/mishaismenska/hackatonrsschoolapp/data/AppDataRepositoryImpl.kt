package com.mishaismenska.hackatonrsschoolapp.data

import android.content.Context
import android.icu.util.Measure
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.mishaismenska.hackatonrsschoolapp.data.entities.DrinkEntity
import com.mishaismenska.hackatonrsschoolapp.data.entities.UserEntity
import com.mishaismenska.hackatonrsschoolapp.data.models.Drink
import com.mishaismenska.hackatonrsschoolapp.data.models.DrinkType
import com.mishaismenska.hackatonrsschoolapp.data.models.Gender
import com.mishaismenska.hackatonrsschoolapp.data.models.User
import com.mishaismenska.hackatonrsschoolapp.interfaces.AppDataRepository
import java.time.LocalDate
import java.time.Period
import java.time.ZoneOffset
import javax.inject.Inject


class AppDataRepositoryImpl @Inject constructor(private val context: Context) :
    AppDataRepository {

    private val dao = AppDatabase.getDatabase(context).dao()
    private var currentUserEntity: UserEntity? = null
    private var drinks: LiveData<List<DrinkEntity>>? = null


    override suspend fun getUserWithDrinks(): User {
        if (currentUserEntity == null)
            currentUserEntity = dao.getUser().last()
        if (drinks == null)
            drinks = dao.getDrinks()
        val userAge =
            currentUserEntity!!.ageOnCreation + Period.between(
                currentUserEntity!!.createdOn,
                LocalDate.now()
            ).years
        return User(
            userAge,
            Measure(currentUserEntity!!.weight, currentUserEntity!!.unit),
            Gender.values()[currentUserEntity!!.gender],
            Transformations.map(drinks!!) { list -> drinkEntityToModel(list) }
        )
    }

    private fun drinkEntityToModel(entities: List<DrinkEntity>): List<Drink> {
        return entities.map { entity ->
            Drink(
                DrinkType.values()[entity.type],
                entity.dateTaken,
                Measure(entity.volume, entity.unit),
                entity.eaten
            )
        }
    }

    override suspend fun addDrink(drink: Drink) {
        if (currentUserEntity == null)
            currentUserEntity = dao.getUser().last()
        currentUserEntity?.let {
            dao.insertDrink(
                DrinkEntity(
                    drink.date.toEpochSecond(ZoneOffset.UTC),
                    it.userId,
                    drink.type.ordinal,
                    drink.date,
                    drink.volume.number as Int,
                    drink.volume.unit,
                    drink.eaten
                )
            )
        }

    }

    override suspend fun deleteDrink(drink: Drink) {
        currentUserEntity?.let {
            dao.deleteDrink(
                DrinkEntity(
                    drink.date.toEpochSecond(ZoneOffset.UTC),
                    it.userId,
                    drink.type.ordinal,
                    drink.date,
                    drink.volume.number as Int,
                    drink.volume.unit,
                    drink.eaten
                )
            )
        }
    }

    override suspend fun addUser(age: Int, weight: Measure, gender: Gender) {
        dao.insertUser(
            UserEntity(
                LocalDate.now().toEpochDay(),
                LocalDate.now(),
                age,
                gender.ordinal,
                weight.number as Int,
                weight.unit
            )
        )
        currentUserEntity = dao.getUser().last()
    }

    override suspend fun reset(){
        dao.resetUser()
        dao.resetDrinks()
    }

    override suspend fun updateWeight(newValue: Int) {
        dao.updateWeight(newValue)
    }
}
