package me.dio.bankline_android.ui.statement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import me.dio.bankline_android.R
import me.dio.bankline_android.data.State
import me.dio.bankline_android.databinding.ActivityBankStatementBinding
import me.dio.bankline_android.domain.Correntista
import me.dio.bankline_android.domain.Movimentacao
import me.dio.bankline_android.domain.TipoMovimentacao
import java.lang.IllegalArgumentException

class BankStatementActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_ACCOUNT_HOLDER = "me.dio.bankline_android.ui.statement.EXTRA_ACCOUNT_HOLDER"
    }

    private val binding by lazy{
        ActivityBankStatementBinding.inflate(layoutInflater)
    }

    private val accountHolder by lazy{
        intent.getParcelableExtra<Correntista>(EXTRA_ACCOUNT_HOLDER) ?: throw IllegalArgumentException()
    }

    private val viewModel by viewModels<BankStatementViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //Log.d("TESTE", "Chegou o ID: ${accountHolder.id}")
        binding.rvBankStatement.layoutManager = LinearLayoutManager(this)

        findBankStatement()

        binding.srlBankStatement.setOnRefreshListener { findBankStatement() }

    }

    private fun findBankStatement() {
        /* Dados estaticos para teste
        val dataSet = ArrayList<Movimentacao>()
        dataSet.add(Movimentacao(1, "03/05/2022 09:24:55", "Lorem Ipsum", 1000.0, TipoMovimentacao.RECEITA, 1))
        dataSet.add(Movimentacao(1, "03/05/2022 09:24:55", "Lorem Ipsum", 1000.0, TipoMovimentacao.DESPESA, 1))
        dataSet.add(Movimentacao(1, "09/05/2022 09:36:55", "Lorem Ipsum", 3000.0, TipoMovimentacao.RECEITA, 1))
        dataSet.add(Movimentacao(1, "09/05/2022 09:37:55", "Lorem Ipsum", 3000.0, TipoMovimentacao.DESPESA, 1))
        dataSet.add(Movimentacao(1, "09/05/2022 09:38:55", "Lorem Ipsum", 3000.0, TipoMovimentacao.RECEITA, 1))
        binding.rvBankStatement.adapter = BankStatementAdapter(dataSet)
        */
      viewModel.findBankStatement(accountHolder.id).observe(this){ state ->
          when(state){

              is State.Success -> {
                  binding.rvBankStatement.adapter = state.data?.let { BankStatementAdapter(it) }
                  binding.srlBankStatement.isRefreshing = false
              }
              is State.Error -> {
                  state.message?.let { Snackbar.make(binding.rvBankStatement, it, Snackbar.LENGTH_LONG).show() }
                  binding.srlBankStatement.isRefreshing = false
              }
              State.Wait -> binding.srlBankStatement.isRefreshing = true
          }

      }

    }
}