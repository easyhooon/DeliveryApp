package com.kenshi.deliveryapp.screen.my

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.kenshi.deliveryapp.R
import com.kenshi.deliveryapp.databinding.FragmentMyBinding
import com.kenshi.deliveryapp.extensions.loadCenterInside
import com.kenshi.deliveryapp.screen.base.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel

class MyFragment: BaseFragment<MyViewModel, FragmentMyBinding>() {

    override val viewModel by viewModel<MyViewModel>()

    override fun getViewBinding(): FragmentMyBinding = FragmentMyBinding.inflate(layoutInflater)

    private val gso: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestIdToken("217683681289-skcpodcmfd2gh9kf58d4b027r9v7l5a5.apps.googleusercontent.com")
            .requestEmail()
            .build()
    }

    //Google SignIn Client
    private val gsc by lazy { GoogleSignIn.getClient(requireActivity(), gso) }

    //firebase auth 객체 관리
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    private val loginLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                //exception 대응
                task.getResult(ApiException::class.java)?.let { account ->
                    Log.e(TAG, "firebaseAuthWithGoogle: ${account.id}")
                    viewModel.saveToken(account.idToken ?: throw Exception())
                } ?: throw Exception()
            } catch (e:Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun initViews() = with(binding) {
        loginButton.setOnClickListener {
            signInGoogle()
        }
        logoutButton.setOnClickListener {
            firebaseAuth.signOut()
            viewModel.signOut()
        }
    }

    private fun signInGoogle() {
        val signInIntent = gsc.signInIntent
        //로그인 런처를 띄움
        loginLauncher.launch(signInIntent)
    }

    override fun observeData() = viewModel.myStateLiveData.observe(this) {
        when(it) {
            is MyState.Uninitialized -> initViews()

            is MyState.Loading -> handleLoadingState()

            is MyState.Success -> handleSuccessState(it)

            is MyState.Login -> handleLoginState(it)

            is MyState.Error -> handleErrorState(it)
        }
    }

    private fun handleLoadingState() = with(binding) {
        loginRequiredGroup.isGone = true
        progressBar.isVisible = true

    }
    private fun handleSuccessState(state: MyState.Success) = with(binding) {
        progressBar.isGone = true
        when(state) {
            is MyState.Success.Registered -> {
                handleRegisteredState(state)
            }
            is MyState.Success.NotRegistered -> {
                profileGroup.isGone = true
                loginRequiredGroup.isVisible = true
            }
        }
    }

    private fun handleRegisteredState(state: MyState.Success.Registered) = with(binding) {
        profileGroup.isVisible = true
        loginRequiredGroup.isGone = true
        profileImageView.loadCenterInside(state.profileImageUri.toString(), 60f)
        userNameTextView.text = state.userName
    }
    private fun handleLoginState(state: MyState.Login) {
        binding.progressBar.isVisible = true
        val credential = GoogleAuthProvider.getCredential(state.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    viewModel.setUserInfo(user)
                } else {
                    firebaseAuth.signOut()
                    viewModel.setUserInfo(null)
                }
            }

    }

    private fun handleErrorState(state: MyState.Error) {
        Toast.makeText(requireContext(), state.messageId, Toast.LENGTH_SHORT).show()
    }

    companion object {

        fun newInstance() = MyFragment()

        const val TAG = "MyFragment"
    }

}