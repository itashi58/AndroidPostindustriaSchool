package com.example.androidpostindustriaschool.ui.activities.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.androidpostindustriaschool.R
import com.example.androidpostindustriaschool.data.database.DatabaseSQLite
import com.example.androidpostindustriaschool.data.repository.LoginRepository
import com.example.androidpostindustriaschool.ui.activities.main.MainActivity
import com.example.androidpostindustriaschool.util.Constants.Companion.INCORRECT_LOG
import com.example.androidpostindustriaschool.util.Constants.Companion.INCORRECT_LOG_AND_PASS
import com.example.androidpostindustriaschool.util.Constants.Companion.INCORRECT_PASS
import com.example.androidpostindustriaschool.util.Constants.Companion.LOGIN_SUCCESSFUL

class LoginActivity : AppCompatActivity() {

    private lateinit var login: EditText
    private lateinit var password: EditText
    private lateinit var incorrectData: TextView
    private lateinit var loginBtn: Button
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initViews()

        val repository = LoginRepository(DatabaseSQLite.getDatabase(this).loginDao())
        val viewModelFactory = LoginViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)

        setListeners()
        setObservers()
    }

    private fun setObservers() {
        viewModel.loginResponseCode.observe(this, { responseCode ->
            when (responseCode) {
                LOGIN_SUCCESSFUL -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                INCORRECT_LOG_AND_PASS -> incorrectData.text =
                    getString(R.string.msg_invalid_login_and_password)
                INCORRECT_LOG -> incorrectData.text = getString(R.string.msg_invalid_login)
                INCORRECT_PASS -> incorrectData.text = getString(R.string.msg_invalid_password)
            }
        })
    }

    private fun setListeners() {
        loginBtn.setOnClickListener {
            viewModel.insertUserInfo(login.text.toString(), password.text.toString())
        }
    }

    private fun initViews() {
        login = findViewById(R.id.et_username)
        password = findViewById(R.id.et_password)
        loginBtn = findViewById(R.id.btn_login)
        incorrectData = findViewById(R.id.tv_incorrect_data)
    }


}