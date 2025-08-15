package com.example.scorecounter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Remove
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.scorecounter.ui.theme.ScoreCounterTheme

class MainActivity : ComponentActivity() {
    private val viewModel: ScoreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScoreCounterTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ScoreCounterScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun ScoreCounterScreen(viewModel: ScoreViewModel) {
    val players by viewModel.players.observeAsState(mutableListOf())
    var newPlayerName by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Row {
            OutlinedTextField(
                value = newPlayerName,
                onValueChange = { newPlayerName = it },
                label = { Text("플레이어 이름") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (newPlayerName.isNotBlank()) {
                    viewModel.addPlayer(newPlayerName)
                    newPlayerName = ""
                }
            }) {
                Text("추가")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        players.forEachIndexed { index, player ->
            PlayerRow(
                player = player,
                onScoreChange = { delta -> viewModel.changeScore(index, delta) },
                onNameChange = { newName -> viewModel.updatePlayerName(index, newName) },
                onRemove = { viewModel.removePlayer(index) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { viewModel.resetScores() }, modifier = Modifier.fillMaxWidth()) {
            Text("점수 초기화")
        }
    }
}

@Composable
fun PlayerRow(
    player: Player,
    onScoreChange: (Int) -> Unit,
    onNameChange: (String) -> Unit,
    onRemove: () -> Unit
) {
    var editingName by remember { mutableStateOf(false) }
    var nameText by remember { mutableStateOf(player.name) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        if (editingName) {
            OutlinedTextField(
                value = nameText,
                onValueChange = { nameText = it },
                modifier = Modifier.width(120.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                singleLine = true,
                label = { Text("이름") }
            )
            IconButton(onClick = {
                if (nameText.isNotBlank()) {
                    onNameChange(nameText)
                    editingName = false
                }
            }) {
                Icon(Icons.Default.Check, contentDescription = "확인")
            }
        } else {
            Text(text = player.name, modifier = Modifier.width(120.dp))
            IconButton(onClick = { editingName = true }) {
                Icon(Icons.Default.Edit, contentDescription = "이름 수정")
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "${player.score}", modifier = Modifier.width(40.dp))
        IconButton(onClick = { onScoreChange(1) }) {
            Icon(Icons.Default.Add, contentDescription = "점수 증가")
        }
        IconButton(onClick = { onScoreChange(-1) }) {
            Icon(Icons.Default.Remove, contentDescription = "점수 감소")
        }
        IconButton(onClick = onRemove) {
            Icon(Icons.Default.Delete, contentDescription = "플레이어 삭제")
        }
    }
}
