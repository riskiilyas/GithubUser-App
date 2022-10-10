package com.keecoding.githubuser

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController

abstract class BaseFragment : Fragment() {

    val baseActivity get() = (activity as BaseActivity)

    abstract fun setupArguments(arguments: Bundle?)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupArguments(arguments)
    }

    protected fun <V : ViewModel> getViewModel(theClass: Class<V>): V {
        return (activity as BaseActivity).getViewModel(theClass)
    }

    protected fun navigate(nav: NavDirections) {
        findNavController().navigate(nav)
    }

    protected fun showToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(requireContext(), msg, duration).show()
    }

    protected fun popBackStack() {
        findNavController().popBackStack()
    }

}