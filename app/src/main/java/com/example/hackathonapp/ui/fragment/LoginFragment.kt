package com.example.hackathonapp.ui.fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.example.hackathonapp.R
import com.example.hackathonapp.model.AuthResult
import com.example.hackathonapp.ui.common.mainComponent
import com.example.hackathonapp.viewmodel.LoginVMFactory
import com.example.hackathonapp.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.login_fragment.*
import org.jetbrains.anko.support.v4.toast

class LoginFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = LoginVMFactory(mainComponent)
        viewModel = ViewModelProviders.of(this, factory).get(LoginViewModel::class.java)

        signInBut.setOnClickListener(this)

        viewModel.processing.observe(this, Observer { isProcessing ->
            progressBar.visibility = if (isProcessing) View.VISIBLE else View.GONE
        })

        viewModel.signInResult.observe(this, Observer { result ->
            if (result is AuthResult.Success){
                findNavController().navigateUp()
            }
            else if (result is AuthResult.Error){
                toast(result.message)
            }
        })

        loginView.setText("fdpd6")
        passwordView.setText("123456")
    }

    override fun onClick(view: View?) {
        val login = loginView.text.toString()
        val password = passwordView.text.toString()
        viewModel.signIn(login, password)
    }
}
