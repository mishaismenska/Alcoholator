package com.mishaismenska.hackatonrsschoolapp.presentation

import android.icu.text.MeasureFormat
import android.icu.util.Measure
import android.icu.util.MeasureUnit
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mishaismenska.hackatonrsschoolapp.R
import com.mishaismenska.hackatonrsschoolapp.data.mlToOz
import com.mishaismenska.hackatonrsschoolapp.databinding.DrinkRecyclerItemBinding
import com.mishaismenska.hackatonrsschoolapp.databinding.MainInfoCardBinding
import com.mishaismenska.hackatonrsschoolapp.presentation.models.DrinkUIModel
import com.mishaismenska.hackatonrsschoolapp.presentation.models.UserStateUIModel
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

class DrinksRecyclerAdapter(
    initialUserStateUIMode: UserStateUIModel
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var userState: UserStateUIModel = initialUserStateUIMode
        set(value) {
            field = value
            notifyItemChanged(0)
        }

    var drinks = emptyList<DrinkUIModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val binding =
                MainInfoCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            StateViewHolder(binding)
        } else {
            val binding =
                DrinkRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            DrinkViewHolder(
                binding,
                parent.context.resources.getStringArray(R.array.drink_types)
            )
        }
    }

    override fun getItemCount(): Int {
        return drinks.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position >= 1) {
            (holder as DrinkViewHolder).bind(
                drinks[position - 1],
                Locale.getDefault().country == "US"
            )
        } else (holder as StateViewHolder).bind(userState)
    }

    class DrinkViewHolder(
        private val binding: DrinkRecyclerItemBinding,
        private val drinkTypes: Array<String>
    ) : RecyclerView.ViewHolder(binding.root) {

        private fun convertDate(date: LocalDateTime): String {
            val duration = Duration.between(date, LocalDateTime.now())
            val context = binding.root.context
            return when (duration.toMinutes().toInt()) {
                in 0..59 -> context.getString(R.string.minuts_ago)
                in 60..1439 -> context.getString(R.string.hours_ago, duration.toHours())
                in 1440..4319 -> context.getString(R.string.days_ago, duration.toDays())
                else -> context.getString(R.string.long_time_ago)
            }
        }

        fun bind(DrinkUIModel: DrinkUIModel, unit: Boolean) {
            binding.drinkName.text = drinkTypes[DrinkUIModel.type.ordinal]
            binding.dateTextView.text = convertDate(DrinkUIModel.date)
            binding.drinkDescription.text =
                if (unit)
                    MeasureFormat.getInstance(Locale.getDefault(), MeasureFormat.FormatWidth.WIDE)
                        .format(
                            Measure(
                                mlToOz(DrinkUIModel.volume.number as Int),
                                MeasureUnit.FLUID_OUNCE
                            )
                        )
                else
                    MeasureFormat.getInstance(Locale.getDefault(), MeasureFormat.FormatWidth.WIDE)
                        .format(DrinkUIModel.volume)
        }
    }

    class StateViewHolder(private val binding: MainInfoCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userState: UserStateUIModel) {
            binding.mainInfoCard.visibility = View.VISIBLE
            val resources = binding.root.context.resources
            val states = resources.getStringArray(R.array.drunk_states)
            if (userState.soberTime.toMillis() > 0)
                binding.soberingTimer.text = resources.getString(
                    R.string.time_format,
                    userState.soberTime.toHours(),
                    userState.soberTime.toMinutes() % 60
                )
            else binding.soberingTimer.text = resources.getString(R.string.you_are_sober)
            binding.alcoholConcentration.text = resources.getString(
                R.string.concentration_format, "%.2f".format(userState.alcoholConcentration)
            )
            binding.stateDescription.text = states[userState.behaviour.ordinal]
        }
    }
}