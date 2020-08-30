package com.mishaismenska.hackatonrsschoolapp.di

import android.content.Context
import com.mishaismenska.hackatonrsschoolapp.presentation.*
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [PresentationModule::class, DataModule::class, DomainModule::class, RetrofitModule::class])
@Singleton
interface ApplicationComponent {
    fun inject(mainFragment: MainFragment)
    fun inject(addUserFragment: AddUserFragment)
    fun inject(addDrinkFragment: AddDrinkFragment)
    fun inject(splashScreenFragment: SplashScreenFragment)
    fun inject(preferences: AppSettingsFragment)
    fun inject(signInFragment: SignInFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }
}
