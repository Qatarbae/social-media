package com.eltex.androidschool.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.FragmentBottomMenuBinding
import com.eltex.androidschool.model.job.MyJob

class BottomMenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentBottomMenuBinding.inflate(inflater, container, false)

        val postsClickListener = View.OnClickListener {
            findNavController()
                .navigate(R.id.action_bottomMenuFragment_to_newPostFragment)
        }

        val eventsClickListener = View.OnClickListener {
            findNavController()
                .navigate(R.id.action_bottomMenuFragment_to_newEventFragment)
        }

        val usersClickListener = View.OnClickListener {
            findNavController()
                .navigate(R.id.fragmentUsers)
        }

        val navController =
            requireNotNull(childFragmentManager.findFragmentById(R.id.container)).findNavController()

        // Слушатель переключения вкладок
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.fragmentPosts -> {
                    binding.add.setOnClickListener(postsClickListener)
                    binding.add.animate()
                        .scaleX(1F)
                        .scaleY(1F)
                }

                R.id.fragmentEvents -> {
                    binding.add.setOnClickListener(eventsClickListener)
                    binding.add.animate()
                        .scaleX(1F)
                        .scaleY(1F)
                }

                R.id.fragmentUsers -> {
                    binding.add.setOnClickListener(usersClickListener)
                    binding.add.animate()
                        .scaleX(0F)
                        .scaleY(0F)
                }

                R.id.commentFragment -> {
                    binding.add.setOnClickListener(null)
                    binding.add.animate()
                        .scaleX(0F)
                        .scaleY(0F)
                }
            }
        }

        binding.bottomMenu.setupWithNavController(navController)

        return binding.root
    }
}
