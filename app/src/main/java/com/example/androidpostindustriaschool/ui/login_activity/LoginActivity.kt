package com.example.androidpostindustriaschool.ui.login_activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.androidpostindustriaschool.R
import com.example.androidpostindustriaschool.data.database.DatabaseSQLite
import com.example.androidpostindustriaschool.data.repository.LoginRepository
import com.example.androidpostindustriaschool.ui.main_activity.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var login: EditText
    private lateinit var password: EditText
    private lateinit var incorrectData: TextView
    private lateinit var loginBtn: Button
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        var isLoginCorrect = false
        var isPasswordCorrect = false

        login = findViewById(R.id.username)
        password = findViewById(R.id.password)
        loginBtn = findViewById(R.id.login)
        incorrectData = findViewById(R.id.incorrect_data)


        val repository = LoginRepository(DatabaseSQLite.getDatabase(this).loginDao())
        val viewModelFactory = LoginViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)

        loginBtn.setOnClickListener {
            if (isPasswordCorrect && isLoginCorrect) {
                viewModel.insertUserInfo(login.text.toString(), password.text.toString())
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else if (!isLoginCorrect && !isPasswordCorrect) {
                incorrectData.text = getString(R.string.title_invalid_login_and_password)
            } else if (!isLoginCorrect && isPasswordCorrect) {
                incorrectData.text = getString(R.string.title_invalid_login)
            } else if (isLoginCorrect && !isPasswordCorrect) {
                incorrectData.text = getString(R.string.title_invalid_password)
            }
        }

        login.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {
                isLoginCorrect = s.isNotEmpty()
            }
        })

        password.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {
                isPasswordCorrect = s.length >= 6
            }
        })
    }


}