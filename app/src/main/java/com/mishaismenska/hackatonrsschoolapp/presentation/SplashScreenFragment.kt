package com.mishaismenska.hackatonrsschoolapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.mishaismenska.hackatonrsschoolapp.R
import com.mishaismenska.hackatonrsschoolapp.di.App
import com.mishaismenska.hackatonrsschoolapp.domain.interfaces.AppDataRepository
import com.mishaismenska.hackatonrsschoolapp.domain.interfaces.GetUserExistenceUseCase
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SplashScreenFragment : Fragment() {

    @Inject
    lateinit var existenceUseCase: GetUserExistenceUseCase

    @Inject
    lateinit var appDataRepository: AppDataRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as App).appComponent.inject(this)
        lifecycleScope.launch(Dispatchers.IO) {
            if (existenceUseCase.checkIfUserExists()) {
                runBlocking {
                    launch(Dispatchers.IO) {
                        appDataRepository.synchronizeUserDetails()
                    }
                }

                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_fragment_container, MainFragment()).commit()
            } else {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_fragment_container, SignInFragment()).commit()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }
}
