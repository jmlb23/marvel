package com.github.jmlb23.android.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.github.jmlb23.android.R
import com.github.jmlb23.android.databinding.LayoutCharacterItemBinding
import com.github.jmlb23.marvel.Character
import com.github.jmlb23.marvel.data.withParams

class CharactersAdapter : RecyclerView.Adapter<CharactersAdapter.CharacterVH>() {
    private val _elements = mutableListOf<Character>()
    private var onClickListener: (Character) -> Unit = {}

    fun addAll(elements: Iterable<Character>) {
        _elements.clear()
        _elements.addAll(elements)
        notifyDataSetChanged()
    }

    fun setOnClickListener(listener: (Character) -> Unit){
        onClickListener = listener
    }

    inner class CharacterVH(private val binding: LayoutCharacterItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(character: Character) {
            with(binding) {
                binding.root.setOnClickListener {
                    onClickListener(character)
                }
                name.text = character.name
                thumbnail.load(character.thumbnail?.path + "/portrait_xlarge." + character.thumbnail?.extension)
                description.text = character.description.takeIf { it.orEmpty().isNotEmpty() } ?: binding.root.context.getString(R.string.empty_description)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterVH =
        LayoutCharacterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false).let(::CharacterVH)

    override fun onBindViewHolder(holder: CharacterVH, position: Int) {
        holder.bind(_elements[position])
    }

    override fun getItemCount(): Int = _elements.size
}