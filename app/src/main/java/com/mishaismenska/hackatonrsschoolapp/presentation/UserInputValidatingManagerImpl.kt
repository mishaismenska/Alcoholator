package com.mishaismenska.hackatonrsschoolapp.presentation

import android.content.Context
import com.google.android.material.textfield.TextInputLayout
import com.mishaismenska.hackatonrsschoolapp.R
import com.mishaismenska.hackatonrsschoolapp.databinding.FragmentAddUserBinding
import com.mishaismenska.hackatonrsschoolapp.presentation.interfaces.UserInputValidatingManager
import javax.inject.Inject

class UserInputValidatingManagerImpl @Inject constructor(private val context: Context) :
    UserInputValidatingManager {
    private fun isEmpty(wrapper: TextInputLayout): Boolean {
        val text = wrapper.editText!!.text.toString()
        return if (text.trim().isEmpty()) {
            wrapper.error = context.getString(R.string.couldnt_be_empty)
            true
        } else {
            wrapper.error = null
            false
        }
    }

    private fun isAgeCorrect(wrapper: TextInputLayout): Boolean {
        val age = wrapper.editText!!.text.toString().toInt()
        return if (age >= 18) {
            wrapper.error = null
            true
        } else {
            wrapper.error = context.getString(R.string.too_young_error)
            false
        }
    }

    private fun isWeightCorrect(wrapper: TextInputLayout): Boolean {
        val weight = wrapper.editText!!.text.toString().toInt()
        return if (weight in 30..350) {
            wrapper.error = null
            true
        } else {
            wrapper.error = context.getString(R.string.too_weird_error)
            false
        }
    }

    override fun validateUserInput(binding: FragmentAddUserBinding): Boolean {
        val isAgeCorrect =
            !isEmpty(binding.ageInputWrapper) && isAgeCorrect(binding.ageInputWrapper)
        val isWeightCorrect =
            !isEmpty(binding.weightInputWrapper) && isWeightCorrect(binding.weightInputWrapper)
        val isGenderCorrect = !isEmpty(binding.genderInputWrapper)
        return isAgeCorrect && isWeightCorrect && isGenderCorrect
    }
}
