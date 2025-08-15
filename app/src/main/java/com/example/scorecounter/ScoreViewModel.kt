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

    fun resetScores() {
        _players.value?.forEach { it.score = 0 }
        _players.value = _players.value
    }
}
