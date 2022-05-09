package me.dio.bankline_android.data

import android.util.Log
import androidx.lifecycle.liveData
import me.dio.bankline_android.data.remote.BanklineApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



object BanklineRepository {

    private val TAG = javaClass.simpleName

    private val restApi by lazy {
        Retrofit.Builder()
             .baseUrl("http://192.168.100.110:8080/")
            //.baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BanklineApi::class.java)
}
        fun findBankStatement(accountHolderId: Int) = liveData {
                   emit(State.Wait)
                    try{
                     emit(State.Success(data =  restApi.findBankStatement(accountHolderId)))
                    }catch (e: Exception){
                        Log.e(TAG, e.message, e)
                        emit(State.Error(e.message))
                    }
        }
}