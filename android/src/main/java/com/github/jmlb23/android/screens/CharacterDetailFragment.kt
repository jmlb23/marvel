package com.github.jmlb23.android.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.Coil
import coil.ImageLoader
import coil.load
import coil.util.DebugLogger
import com.github.jmlb23.android.databinding.FragmentCharacterDetailBinding
import com.github.jmlb23.marvel.presentation.detail.CharacterView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.github.jmlb23.android.R
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject

class CharacterDetailFragment : Fragment() {

    private val view by inject<CharacterView>()


    private val args by navArgs<CharacterDetailFragmentArgs>()
    override fun onStart() {
        super.onStart()
        lifecycleScope.launch(Dispatchers.IO) {
            view.getDetail(args.id)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentCharacterDetailBinding.inflate(inflater, container, false).apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            render()

        }.root
    }

    private fun FragmentCharacterDetailBinding.render() {
        view.start({
            Snackbar.make(this.root, getString(R.string.error), Snackbar.LENGTH_SHORT)
                .let { snack ->
                    snack.setAction(getString(R.string.retry)) { d ->
                        lifecycleScope.launch {
                            view.getDetail(args.id)
                            snack.dismiss()
                        }
                    }
                }.show()
        }, {
            name.text = it.name
            description.text =
                it.description.takeIf { it.orEmpty().isNotEmpty() } ?: getString(R.string.empty_description)
            banner.load(it.thumbnail?.path + "/portrait_uncanny." + it.thumbnail?.extension)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        view.dispose()
    }

}