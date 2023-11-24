package com.dgsdev.bufunfatech.ViewModel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.dgsdev.bufunfatech.Data.Api.ServiceApi
import com.dgsdev.bufunfatech.Model.Transaction
import com.dgsdev.bufunfatech.Network.ServiceProvider
import com.dgsdev.bufunfatech.Repository.TransactionRepository
import kotlinx.coroutines.launch


class TransactionViewModel(application: Application): AndroidViewModel(application) {

    private val repository: TransactionRepository

    init{
        val serviceApi = ServiceProvider.createService(ServiceApi::class.java)
        repository= TransactionRepository(serviceApi)
    }

    fun addTransaction(transaction: Transaction) = viewModelScope.launch {
        repository.insertTransaction(transaction)
    }

    fun getTransaction(): LiveData<List<Transaction>> = liveData {
        emit(repository.getAllTransactions())
    }

    fun getMonthlyTransaction(month:Int,year:Int): LiveData<List<Transaction>> = liveData {
        emit(repository.getMonthlyTransaction(month,year))
    }

    fun getYearlyTransaction(year: Int): LiveData<List<Transaction>> =  liveData {
        emit(repository.getYearlyTransaction(year))
    }

    fun deleteTransaction(id:Int){
        viewModelScope.launch {
            repository.deleteTransaction(id)
        }
    }

    //fun updateTransaction(transaction: Transaction) {
    //    viewModelScope.launch {
    //        repository.updateTransaction(transaction.id, transaction)
    //    }
    //}

    fun updateTransaction(transaction: Transaction){
        viewModelScope.launch {
            transaction.id?.let { repository.updateTransaction(it, transaction) }
        }
    }
}