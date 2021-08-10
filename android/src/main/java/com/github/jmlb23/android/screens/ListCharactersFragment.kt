package com.github.jmlb23.android.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.jmlb23.android.R
import com.github.jmlb23.android.adapters.CharactersAdapter
import com.github.jmlb23.android.databinding.FragmentListCharactersBinding
import com.github.jmlb23.marvel.Character
import com.github.jmlb23.marvel.presentation.list.CharacterListView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject


class ListCharactersFragment : Fragment() {

    private val view by inject<CharacterListView>()
    private val currentPage = MutableStateFlow(0)
    val isLoading = MutableStateFlow(true)
    private val adapter = CharactersAdapter().apply { setOnClickListener(::setOnItemClickListener) }

    override fun onStart() {
        super.onStart()
        currentPage.onEach {
            view.getList(it)
        }.launchIn(lifecycleScope)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return FragmentListCharactersBinding.inflate(inflater, container, false).apply {
            rvCharacters.adapter = adapter
            rvCharacters.layoutManager =
                LinearLayoutManager(context).apply { orientation = LinearLayoutManager.VERTICAL }
            rvCharacters.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if ((recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition() == adapter.itemCount - 1 && !isLoading.value) {
                        currentPage.value = currentPage.value + 1
                        isLoading.value = true
                    }

                }
            })
            render()
        }.root
    }

    private fun FragmentListCharactersBinding.render() {
        isLoading.onEach {
            loader.isVisible = it
        }.launchIn(lifecycleScope)
        view.start({
            Log.d("ERRORS", it.toString())
            Snackbar.make(this.root, getString(R.string.error), Snackbar.LENGTH_SHORT)
                .let { snack ->
                    snack.setAction(getString(R.string.retry)) { d ->
                        currentPage.value = currentPage.value
                        snack.dismiss()
                    }
                }.show()
        }, {
            Log.d("CHARACTERS", it.toString())
            adapter.addAll(it)
            isLoading.value = false
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        view.dispose()
    }

    private fun setOnItemClickListener(character: Character) {
        findNavController().navigate(R.id.characterDetailFragment, bundleOf("id" to (character.id ?: 0).toInt()))
    }
}


