package com.dgsdev.bufunfatech.Data.Api

import com.dgsdev.bufunfatech.Model.Transaction
import com.dgsdev.bufunfatech.Model.TransactionDTO
import retrofit2.Response
import retrofit2.http.*
interface ServiceApi {
    @GET ("transactions")
    suspend fun getTransactions(): List<Transaction>

    //@GET ("transactions/{id}")
    //suspend fun getTransaction(@Path("id") id: Int): Transaction

    @POST ("transactions")
    suspend fun insertTransaction(@Body transaction: Transaction): Transaction

    @PUT ("transactions/{id}")
    suspend fun updateTransaction(@Path("id") id: Int,@Body transaction: Transaction): Transaction

    @DELETE ("transactions/{id}")
    suspend fun deleteTransaction(@Path("id") id: Int) : Response<Unit>

    @GET("transactions/monthly/{month}/{year}")
    suspend fun getMonthlyTransaction(@Path("month") month: Int, @Path("year") year: Int): List<Transaction>

    @GET ("transactions/yearly/{year}")
    suspend fun getYearlyTransaction(@Path("year") year: Int): List<Transaction>
}