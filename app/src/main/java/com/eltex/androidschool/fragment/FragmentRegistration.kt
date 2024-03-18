package com.eltex.androidschool.fragment

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.FragmentRegistrationBinding
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.util.getText
import com.eltex.androidschool.util.toast
import com.eltex.androidschool.util.validateLoginAndNameAfterChangedFocus
import com.eltex.androidschool.util.validateLoginAndNameOnChangedText
import com.eltex.androidschool.util.validatePasswordAfterChangedFocus
import com.eltex.androidschool.util.validatePasswordOnChangedText
import com.eltex.androidschool.viewmodel.RegistrationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class FragmentRegistration : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        val viewModel by viewModels<RegistrationViewModel>()

        val pickAvatar = registerForActivityResult(ActivityResultContracts.GetContent()) {
            it?.let {
                viewModel.saveFile(it)
            }
        }

        viewModel.state.onEach { state ->
            (state.status as? Status.Error)?.let {
                toast(it.reason.getText(requireContext()))
                viewModel.consumeError()
            }

            binding.loginButton.isEnabled = state.valid

            if (state.file != null) {
                binding.preview.isVisible = true
                Glide.with(binding.avatar)
                    .load(state.file)
                    .transform(CircleCrop())
                    .into(binding.avatar)
                binding.takeAvatar.isClickable = false
            } else {
                binding.preview.isVisible = false
                binding.takeAvatar.isClickable = true
            }

        }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        with(binding) {
            login.editText?.validateLoginAndNameAfterChangedFocus(login, binding, viewModel)
            name.editText?.validateLoginAndNameAfterChangedFocus(name, binding, viewModel)
            login.editText?.validateLoginAndNameOnChangedText(login, binding, viewModel)
            name.editText?.validateLoginAndNameOnChangedText(name, binding, viewModel)

            password.editText?.validatePasswordAfterChangedFocus(password, binding, viewModel)
            repeatPassword.editText?.validatePasswordAfterChangedFocus(
                repeatPassword,
                binding,
                viewModel
            )

            password.editText?.validatePasswordOnChangedText(
                password,
                repeatPassword, binding, viewModel
            )

            repeatPassword.editText?.validatePasswordOnChangedText(
                repeatPassword,
                password, binding, viewModel
            )
        }

        binding.takeAvatar.setOnClickListener {
            pickAvatar.launch("image/*")
        }

        binding.remove.setOnClickListener {
            viewModel.saveFile(null)
        }

        binding.loginButton.setOnClickListener {
            val login = binding.login.editText?.text.toString().trim()
            val name = binding.name.editText?.text.toString().trim()
            val pass = binding.password.editText?.text.toString().trim()
            val file = viewModel.state.value.file
            viewModel.loginAfterRegistration(login, pass, name, file)
            if (viewModel.state.value.status == Status.Idle) {
                requireParentFragment()
                    .findNavController()
                    .navigate(R.id.action_fragmentRegistration_to_toolbarFragment)
            } else {
                toast(getString(R.string.registration_error))
            }
        }

        return binding.root
    }
}