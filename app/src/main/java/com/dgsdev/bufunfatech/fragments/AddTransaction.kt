package com.dgsdev.bufunfatech.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.dgsdev.bufunfatech.Model.Transaction
import com.dgsdev.bufunfatech.R
import com.dgsdev.bufunfatech.ViewModel.TransactionViewModel
import com.dgsdev.bufunfatech.databinding.FragmentAddTransactionBinding

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.SharedPreferences
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.*


class AddTransaction : Fragment(), View.OnClickListener {

   val transactions by navArgs<AddTransactionArgs>()
   private lateinit var binding: FragmentAddTransactionBinding

   private var formattedDateForApi: String? = null

   lateinit var userDetails: SharedPreferences
   private var category = ""
    var day=0
    var month=0
    var year=0

   private val viewModel: TransactionViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        getActivity()?.getWindow()?.setStatusBarColor(ContextCompat.getColor(requireActivity(), R.color.background))
        binding =  FragmentAddTransactionBinding.inflate(inflater, container, false)
        val bottomNav: BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigation)
        bottomNav.visibility = View.GONE
        setListner(binding)
        datePicker(binding)
        userDetails = requireActivity().getSharedPreferences("UserDetails", AppCompatActivity.MODE_PRIVATE)
        if(transactions.from){
            setDatas()
            binding.addTransaction.setText("Salvar Transação")
            binding.titleAddTransacttion.setText("Editar Transação")
            binding.back.setOnClickListener {
                val arg = AddTransactionDirections.actionAddTransactionToTransactionDetails(transactions.data,"AddTransaction")
                Navigation.findNavController(binding.root)
                    .navigate(arg)
            }
        }else{
            binding.back.setOnClickListener { Navigation.findNavController(binding.root).navigate(R.id.action_addTransaction_to_dashboard2) }
        }

        binding.addTransaction.setOnClickListener{ addNewTransaction() }
        return binding.root
    }

    private fun setListner(binding: FragmentAddTransactionBinding) {
        binding.comida.setOnClickListener(this)
        binding.shopping.setOnClickListener(this)
        binding.transporte.setOnClickListener(this)
        binding.saude.setOnClickListener(this)
        binding.outros.setOnClickListener(this)
        binding.educacao.setOnClickListener(this)

    }


    private fun setDatas(){
        binding.editTitle.setText(transactions.data.title)
        binding.editDate.setText(transactions.data.date)
        binding.editMoney.setText(transactions.data.amount.toString())
        binding.editNote.setText(transactions.data.note)
        category=transactions.data.category
        when (category) {
            "Comida" -> {
                setCategory(binding.comida, binding.comida)
            }
            "Shopping" -> {
                setCategory(binding.shopping, binding.shopping)
            }
            "Transporte" -> {
                setCategory(binding.transporte, binding.transporte)
            }
            "Saúde" -> {
                setCategory(binding.saude, binding.saude)
            }
            "Outros" -> {
                setCategory(binding.outros, binding.outros)
            }
            "Educação" -> {
                setCategory(binding.educacao, binding.educacao)
            }
        }
    }

    private fun addNewTransaction() {
       val title = binding.editTitle.text.toString()
       val amount = binding.editMoney.text.toString()
       val note = binding.editNote.text.toString()
       val date = formattedDateForApi

       if (title == "" || amount == "" || note == "" || date == "" || category == ""){
           Toast.makeText(context, "Insira os detalhes necessários", Toast.LENGTH_SHORT).show()
       } else {

           if ( transactions.from){
               val transaction = date?.let {
                   Transaction(
                       transactions.data.id,
                       type = "Expense",
                       title = title,
                       amount = amount.toDouble(),
                       note = note,
                       date = it,
                       day = day,
                       month = month,
                       year = year,
                       category = category
                   )
               }
               if (transaction != null) {
                   viewModel.updateTransaction(transaction)
               }
               Toast.makeText(context, "Transação atualizada com sucesso!", Toast.LENGTH_SHORT).show()
               val arg = transaction?.let {
                   AddTransactionDirections.actionAddTransactionToTransactionDetails(
                       it,"AddTransaction")
               }
               if (arg != null) {
                   Navigation.findNavController(binding.root)
                       .navigate(arg)
               }
           }else {
               val transaction = date?.let {
                   Transaction(
                       null,
                       type = "Expense",
                       title = title,
                       amount = amount.toDouble(),
                       note = note,
                       date = it,
                       day = day,
                       month = month,
                       year = year,
                       category = category

                   )
               }
               if (transaction != null) {
                   viewModel.addTransaction(transaction)
               }
               Toast.makeText(context, "Transação adicionada com sucesso!", Toast.LENGTH_SHORT).show()
               Navigation.findNavController(binding.root)
                   .navigate(R.id.action_addTransaction_to_dashboard2)
           }
       }
    }

    @SuppressLint("SimpleDateFormat")
    fun formatToIso8601(cal: Calendar): String {
        val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        outputFormat.timeZone = TimeZone.getTimeZone("UTC")
        return outputFormat.format(cal.time)
    }

    @SuppressLint("SimpleDateFormat")
    fun datePicker(binding:FragmentAddTransactionBinding){

        val cal = Calendar.getInstance()

        binding.editDate.setText(SimpleDateFormat("dd MMMM  yyyy").format(System.currentTimeMillis()))
        day = SimpleDateFormat("dd").format(System.currentTimeMillis()).toInt()
        month = SimpleDateFormat("MM").format(System.currentTimeMillis()).toInt()
        year = SimpleDateFormat("yyyy").format(System.currentTimeMillis()).toInt()

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val displayFormat = SimpleDateFormat("dd MMMM yyyy", Locale.US)
            binding.editDate.setText(displayFormat.format(cal.time))

            // Data para ISO 8601 para uso na API
            formattedDateForApi = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }.format(cal.time)
        }
            //formatToIso8601(cal)

        binding.editDate.setOnClickListener {
            DatePickerDialog(
                requireContext(), dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.comida -> {
                setCategory(v,binding.comida)
            }
            binding.shopping -> {
                setCategory(v,binding.shopping)
            }
            binding.transporte -> {
                setCategory(v,binding.transporte)
            }
            binding.saude -> {
                setCategory(v,binding.saude)
            }
            binding.outros -> {
                setCategory(v,binding.outros)
            }
            binding.educacao -> {
                setCategory(v,binding.educacao)
            }
        }
    }

    private fun setCategory(v: View, button: MaterialButton) {
        category = button.text.toString()
        button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.mtrl_btn_text_btn_bg_color_selector))
        button.setIconTintResource(R.color.purple_200)
        button.setStrokeColorResource(R.color.purple_200)
        button.setTextColor(ContextCompat.getColor(requireContext(), R.color.purple_200))

        when (v) {
            binding.comida -> {
                removeBackground(binding.shopping)
                removeBackground(binding.transporte)
                removeBackground(binding.saude)
                removeBackground(binding.outros)
                removeBackground(binding.educacao)
            }
            binding.shopping -> {
                removeBackground(binding.comida)
                removeBackground(binding.transporte)
                removeBackground(binding.saude)
                removeBackground(binding.outros)
                removeBackground(binding.educacao)
            }
            binding.transporte -> {
                removeBackground(binding.shopping)
                removeBackground(binding.comida)
                removeBackground(binding.saude)
                removeBackground(binding.outros)
                removeBackground(binding.educacao)
            }
            binding.saude -> {
                removeBackground(binding.shopping)
                removeBackground(binding.transporte)
                removeBackground(binding.comida)
                removeBackground(binding.outros)
                removeBackground(binding.educacao)
            }
            binding.outros -> {
                removeBackground(binding.shopping)
                removeBackground(binding.transporte)
                removeBackground(binding.saude)
                removeBackground(binding.comida)
                removeBackground(binding.educacao)
            }
            binding.educacao -> {
                removeBackground(binding.shopping)
                removeBackground(binding.transporte)
                removeBackground(binding.saude)
                removeBackground(binding.outros)
                removeBackground(binding.comida)
            }
        }
    }

   private fun removeBackground(button: MaterialButton) {
        button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent))
        button.setIconTintResource(R.color.textSecondary)
        button.setStrokeColorResource(R.color.textSecondary)
        button.setTextColor(ContextCompat.getColor(requireContext(), R.color.textSecondary))
   }


}

