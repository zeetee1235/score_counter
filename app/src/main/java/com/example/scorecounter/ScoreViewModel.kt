package com.example.scorecounter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel : ViewModel() {
    private val _players = MutableLiveData<MutableList<Player>>(mutableListOf())
    val players: LiveData<MutableList<Player>> = _players

    fun addPlayer(name: String) {
        _players.value?.add(Player(name))
        _players.value = _players.value
    }

    fun removePlayer(index: Int) {
        _players.value?.removeAt(index)
        _players.value = _players.value
    }

    fun updatePlayerName(index: Int, newName: String) {
        _players.value?.get(index)?.name = newName
        _players.value = _players.value
    }

    fun changeScore(index: Int, delta: Int) {
        _players.value?.get(index)?.let {
            it.score += delta
        }
        _players.value = _players.value
    }

    fun setScore(index: Int, newScore: Int) {
        _players.value?.get(index)?.let {
            it.score = newScore
        }
        _players.value = _players.value
    }

    fun resetScores() {
        _players.value?.forEach { it.score = 0 }
        _players.value = _players.value
    }

    fun movePlayerUp(index: Int) {
        val list = _players.value ?: return
        if (index > 0 && index < list.size) {
            java.util.Collections.swap(list, index, index - 1)
            _players.value = list
        }
    }

    fun movePlayerDown(index: Int) {
        val list = _players.value ?: return
        if (index >= 0 && index < list.size - 1) {
            java.util.Collections.swap(list, index, index + 1)
            _players.value = list
        }
    }
}
