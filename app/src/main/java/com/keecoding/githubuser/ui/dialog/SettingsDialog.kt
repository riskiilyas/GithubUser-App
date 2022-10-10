package com.keecoding.githubuser.ui.dialog

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.button.MaterialButton
import com.keecoding.girhubusersub2.R
import com.keecoding.githubuser.BaseActivity
import com.keecoding.githubuser.viewmodel.PrefViewModel


class SettingsDialog : DialogFragment(R.layout.settings_layout) {

    private lateinit var viewModel: PrefViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (requireActivity() as BaseActivity).getViewModel(PrefViewModel::class.java)

        view.findViewById<SwitchCompat>(R.id.switchCompat).apply {
            viewModel.darkMode.observe(viewLifecycleOwner) {
                isChecked = it
            }

            setOnCheckedChangeListener { _, isDark ->
                viewModel.toggleTheme(isDark)
            }
        }

        view.findViewById<ImageButton>(R.id.btnSourceCode).setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW)
            browserIntent.data = Uri.parse("https://github.com/riskiilyas")
            startActivity(browserIntent)
        }

        view.findViewById<MaterialButton>(R.id.btnCancelExit).setOnClickListener {
            dismiss()
        }
    }
}