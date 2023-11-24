package com.dgsdev.bufunfatech.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.dgsdev.bufunfatech.Model.Transaction
import com.dgsdev.bufunfatech.R
import com.dgsdev.bufunfatech.databinding.TransactionItemBinding
import com.dgsdev.bufunfatech.fragments.*
import java.text.ParseException
import java.util.Locale

class TransactionAdapter(val context: Context, val activity:Activity,val fragment:String, private val transList: List<Transaction>) : RecyclerView.Adapter<TransactionAdapter.transactionViewHolder>(){

    class transactionViewHolder(val binding:TransactionItemBinding) : RecyclerView.ViewHolder(binding.root)

    lateinit var userDetails: SharedPreferences
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): transactionViewHolder {
        return transactionViewHolder(TransactionItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n", "SuspiciousIndentation", "SimpleDateFormat")
    override fun onBindViewHolder(holder: transactionViewHolder, position: Int) {
        val data = transList[position]
        holder.binding.date.textSize = 10f
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val parsedDate = inputFormat.parse(data.date)

            val outputFormat = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale("pt", "BR"))
            holder.binding.date.text = outputFormat.format(parsedDate)
        } catch (e: ParseException) {
            e.printStackTrace()
            holder.binding.date.text = data.date
        }
        holder.binding.title.text = data.title
        holder.binding.money.text = "R$ "+data.amount.toInt().toString()
        //holder.binding.date.text = data.date
        holder.binding.category.text = data.category

        when(data.category){
            "Comida" -> {
                holder.binding.cardIcon.setImageResource(R.drawable.ic_baseline_fastfood_24)
                holder.binding.cardIcon.setColorFilter(ContextCompat.getColor(context, R.color.yellow))
                holder.binding.category.setTextColor(ContextCompat.getColor(context, R.color.yellow))
                holder.binding.cardImage.setCardBackgroundColor(ContextCompat.getColor(context, R.color.yellow_light))
            }
            "Shopping" -> {
                holder.binding.cardIcon.setImageResource(R.drawable.ic_baseline_shopping_cart_24)
                holder.binding.cardIcon.setColorFilter(ContextCompat.getColor(context, R.color.lightBlue))
                holder.binding.category.setTextColor(ContextCompat.getColor(context, R.color.lightBlue))
                holder.binding.cardImage.setCardBackgroundColor(ContextCompat.getColor(context, R.color.lightBlue_light))
            }
            "Transporte" -> {
                holder.binding.cardIcon.setImageResource(R.drawable.ic_baseline_directions_transit_24)
                holder.binding.cardIcon.setColorFilter(ContextCompat.getColor(context, R.color.violet))
                holder.binding.category.setTextColor(ContextCompat.getColor(context, R.color.violet))
                holder.binding.cardImage.setCardBackgroundColor(ContextCompat.getColor(context, R.color.violet_light))
            }
            "Saúde" -> {
                holder.binding.cardIcon.setImageResource(R.drawable.ic_baseline_favorite_24)
                holder.binding.cardIcon.setColorFilter(ContextCompat.getColor(context, R.color.red))
                holder.binding.category.setTextColor(ContextCompat.getColor(context, R.color.red))
                holder.binding.cardImage.setCardBackgroundColor(ContextCompat.getColor(context, R.color.red_light))
            }
            "Outros" -> {
                holder.binding.cardIcon.setImageResource(R.drawable.ic_baseline_category_24)
                holder.binding.cardIcon.setColorFilter(ContextCompat.getColor(context, R.color.lightBrown))
                holder.binding.category.setTextColor(ContextCompat.getColor(context, R.color.lightBrown))
                holder.binding.cardImage.setCardBackgroundColor(ContextCompat.getColor(context, R.color.lightBrown_light))
            }
            "Educação" -> {
                holder.binding.cardIcon.setImageResource(R.drawable.ic_baseline_auto_stories_24)
                holder.binding.cardIcon.setColorFilter(ContextCompat.getColor(context, R.color.green))
                holder.binding.category.setTextColor(ContextCompat.getColor(context, R.color.green))
                holder.binding.cardImage.setCardBackgroundColor(ContextCompat.getColor(context, R.color.green_light))
            }
        }

        holder.binding.root.setOnClickListener {
             if(fragment == "Dashboard"){
                 val argument = DashboardDirections.goToTransactionDetails(data,fragment)
                    Navigation.findNavController(it).navigate(argument)
             } else if(fragment == "AllTransactions"){
                 val argument = AllTransactionsDirections.allTransactionToTransactionDetails(data,fragment)
                 Navigation.findNavController(it).navigate(argument)
             }
        }
    }
    override fun getItemCount() = transList.size
}


