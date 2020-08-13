package com.mishaismenska.hackatonrsschoolapp.di

import android.content.Context
import com.mishaismenska.hackatonrsschoolapp.ui.*
import dagger.BindsInstance
import dagger.Component

@Component(modules = [BusinessLogicModule::class, DbModule::class])
interface AppComponent {
    fun inject(mainFragment: MainFragment)
    fun inject(addUserFragment: AddUserFragment)
    fun inject(addDrinkFragment: AddDrinkFragment)
    fun inject(splashScreenFragment: SplashScreenFragment)
    fun inject(preferences: AppSettingsFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}
