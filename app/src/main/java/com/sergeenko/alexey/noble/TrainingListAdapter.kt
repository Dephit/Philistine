package com.sergeenko.alexey.noble

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sergeenko.alexey.noble.dataclasses.Language
import kotlinx.android.synthetic.main.training_card.view.*
import kotlinx.android.synthetic.main.training_history_item.view.*

class TrainingListAdapter(var lang: Language) : PagedListAdapter<TrainingItem, RecyclerView.ViewHolder>(UserDiffCallback) {

    var nextDate = convertLongToTimeDDMM(0L)

    class ClientHolder(private val textView: ConstraintLayout) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientHolder {
        val textView = LayoutInflater.from(parent.context).inflate(R.layout.training_card, parent, false) as ConstraintLayout
        return ClientHolder(textView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.apply {
            holder.itemView.apply {
                val date = convertLongToTimeDDMM(dateOfTraining)
                if(nextDate != date){
                    nextDate = date
                    date_text.text = date
                    date_text.visibility = View.VISIBLE
                }else {
                    date_text.visibility = View.GONE
                }

                training_dur_text.text = lang.length_of_program
                training_name2.text = lang.values_on_training_end
                training_name_shown.text = getMuscleName(lang, name)
                training_dur_text2.text =  "${convertStringToDate(totalMinutes)}:${convertStringToDate(totalSeconds)}"
                closed_image.setOnClickListener {
                    closed_image.isActivated = !closed_image.isActivated
                    full_train_info.visibility = if(closed_image.isActivated) View.VISIBLE else View.GONE
                }

                setMuscle(muscle0, lang.chest, muscles!![0].musclePower.toString())
                setMuscle(muscle1, lang.abs, muscles!![1].musclePower.toString())
                setMuscle(muscle2, lang.hip_front, muscles!![2].musclePower.toString())
                setMuscle(muscle3, lang.hip_back, muscles!![3].musclePower.toString())
                setMuscle(muscle4, lang.glutes, muscles!![4].musclePower.toString())
                setMuscle(muscle5, lang.shoulder_blades, muscles!![5].musclePower.toString())

                setMuscle(muscle6, lang.back, muscles!![6].musclePower.toString())
                setMuscle(muscle7, lang.lower_back, muscles!![7].musclePower.toString())
                setMuscle(muscle8, lang.arms, muscles!![8].musclePower.toString())
                setMuscle(muscle9, lang.additional_1, muscles!![9].musclePower.toString())
                setMuscle(muscle10, lang.additional_2, muscles!![10].musclePower.toString())
                setMuscle(muscle11, lang.additional_3, muscles!![11].musclePower.toString())

                setMuscle(muscle12, lang.common_power, commonPower.toString())
                setMuscle(muscle13, lang.impulse, impulse.toString())
                setMuscle(muscle14, lang.pause, pause.toString())
                setMuscle(muscle15, lang.frequency, frequency.toString())
                setMuscle(muscle16, lang.frequency_fill, frequencyFill.toString())
            }
        }
        }

    @SuppressLint("SetTextI18n")
    private fun setMuscle(muscle0: View?, muscle: Muscle) {
        muscle0?.name_one?.text = muscle.muscleName + ":"
        muscle0?.number?.text = "${muscle.musclePower}%"
    }

    @SuppressLint("SetTextI18n")
    private fun setMuscle(muscle0: View?, name: String, power: String) {
        muscle0?.name_one?.text = "$name:"
        muscle0?.number?.text = "$power"
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int = position

    companion object {
        val UserDiffCallback = object : DiffUtil.ItemCallback<TrainingItem>() {
            override fun areItemsTheSame(oldItem: TrainingItem, newItem: TrainingItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TrainingItem, newItem: TrainingItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}