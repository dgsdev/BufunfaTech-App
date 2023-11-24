package com.dgsdev.bufunfatech.Repository


import com.dgsdev.bufunfatech.Data.Api.ServiceApi
import com.dgsdev.bufunfatech.Model.Transaction
import com.dgsdev.bufunfatech.Network.ServiceProvider

class TransactionRepository(serviceApi: ServiceApi) {

    private val serviceApi: ServiceApi = ServiceProvider.createService(ServiceApi::class.java)

    suspend fun getAllTransactions(): List<Transaction> {
        return serviceApi.getTransactions()
    }
    suspend fun getMonthlyTransaction(month:Int, year:Int): List<Transaction> {
        return serviceApi.getMonthlyTransaction(month, year)
    }

    suspend fun getYearlyTransaction(year:Int): List<Transaction> {
        return serviceApi.getYearlyTransaction(year)
    }

    suspend fun insertTransaction(transaction: Transaction) {
        try {
            serviceApi.insertTransaction(transaction)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun deleteTransaction(id:Int){
        serviceApi.deleteTransaction(id)
    }

    suspend fun updateTransaction(id: Int, transaction: Transaction){
        try {
            serviceApi.updateTransaction(id, transaction)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //serviceApi.updateTransaction(id, transaction)
    }
}