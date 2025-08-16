package com.example.scorecounter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel : ViewModel() {
    private val _players = MutableLiveData<List<Player>>(emptyList())
    val players: LiveData<List<Player>> = _players

    private inline fun update(block: (MutableList<Player>) -> Unit) {
        val next = _players.value?.toMutableList() ?: mutableListOf()
        block(next)
        _players.value = next
    }

    fun addPlayer(name: String) = update { it.add(Player(name)) }

    fun removePlayer(index: Int) = update { list ->
        if (index in list.indices) list.removeAt(index)
    }

    fun updatePlayerName(index: Int, newName: String) = update { list ->
        if (index in list.indices) list[index] = list[index].copy(name = newName)
    }

    fun changeScore(index: Int, delta: Int) = update { list ->
        if (index in list.indices) list[index] = list[index].copy(score = list[index].score + delta)
    }

    fun setScore(index: Int, newScore: Int) = update { list ->
        if (index in list.indices) list[index] = list[index].copy(score = newScore)
    }

    fun resetScores() = update { list ->
        for (i in list.indices) list[i] = list[i].copy(score = 0)
    }

    fun movePlayerUp(index: Int) = update { list ->
        if (index > 0 && index < list.size) java.util.Collections.swap(list, index, index - 1)
    }

    fun movePlayerDown(index: Int) = update { list ->
        if (index >= 0 && index < list.size - 1) java.util.Collections.swap(list, index, index + 1)
    }
}
