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
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.scorecounter.ui.theme.ScoreCounterTheme
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoreCounterScreen(viewModel: ScoreViewModel) {
    val players by viewModel.players.observeAsState(mutableListOf())
    var showAddDialog by rememberSaveable { mutableStateOf(false) }
    var newPlayerName by rememberSaveable { mutableStateOf("") }

    val totalScore = players.sumOf { it.score }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Score Counter • 합계: $totalScore") },
                actions = {
                    IconButton(onClick = { viewModel.resetScores() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "점수 초기화")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "플레이어 추가")
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
            if (players.isEmpty()) {
                Text(text = "플레이어를 추가해주세요.", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(12.dp))
            }
            LazyColumn(
                contentPadding = PaddingValues(bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(players) { index, player ->
                    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                        PlayerRow(
                            player = player,
                            onScoreChange = { delta -> viewModel.changeScore(index, delta) },
                            onSetScore = { newScore -> viewModel.setScore(index, newScore) },
                            onNameChange = { newName -> viewModel.updatePlayerName(index, newName) },
                            onRemove = { viewModel.removePlayer(index) },
                            onMoveUp = { viewModel.movePlayerUp(index) },
                            onMoveDown = { viewModel.movePlayerDown(index) }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("플레이어 추가") },
            text = {
                OutlinedTextField(
                    value = newPlayerName,
                    onValueChange = { newPlayerName = it },
                    singleLine = true,
                    label = { Text("이름") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newPlayerName.isNotBlank()) {
                        viewModel.addPlayer(newPlayerName.trim())
                        newPlayerName = ""
                        showAddDialog = false
                    }
                }) { Text("추가") }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) { Text("취소") }
            }
        )
    }
}

@Composable
fun PlayerRow(
    player: Player,
    onScoreChange: (Int) -> Unit,
    onSetScore: (Int) -> Unit,
    onNameChange: (String) -> Unit,
    onRemove: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit
) {
    var editingName by remember { mutableStateOf(false) }
    var nameText by remember { mutableStateOf(player.name) }
    var scoreText by remember { mutableStateOf(player.score.toString()) }

    LaunchedEffect(player.score) {
        // Keep text in sync when score changes from buttons or resets
        scoreText = player.score.toString()
    }

    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
        if (editingName) {
            OutlinedTextField(
                value = nameText,
                onValueChange = { nameText = it },
                modifier = Modifier.weight(1f),
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
            Text(text = player.name, modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
            IconButton(onClick = { editingName = true }) {
                Icon(Icons.Default.Edit, contentDescription = "이름 수정")
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        OutlinedTextField(
            value = scoreText,
            onValueChange = { txt ->
                scoreText = txt
                // Accept optional leading '-' and digits
                val cleaned = txt.trim()
                if (cleaned.isNotEmpty() && cleaned != "-") {
                    cleaned.toIntOrNull()?.let { onSetScore(it) }
                }
            },
            modifier = Modifier.width(96.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.width(4.dp))
        FilledTonalIconButton(onClick = { onScoreChange(1) }) {
            Icon(Icons.Default.Add, contentDescription = "점수 증가")
        }
        Spacer(modifier = Modifier.width(4.dp))
        FilledTonalIconButton(onClick = { onScoreChange(-1) }) {
            Icon(Icons.Default.Remove, contentDescription = "점수 감소")
        }
        Spacer(modifier = Modifier.width(4.dp))
        FilledTonalIconButton(onClick = { onScoreChange(5) }) {
            Text("+5")
        }
        Spacer(modifier = Modifier.width(4.dp))
        FilledTonalIconButton(onClick = { onScoreChange(-5) }) {
            Text("-5")
        }
        Spacer(modifier = Modifier.width(4.dp))
        FilledTonalIconButton(onClick = { onScoreChange(10) }) {
            Text("+10")
        }
        Spacer(modifier = Modifier.width(4.dp))
        FilledTonalIconButton(onClick = { onScoreChange(-10) }) {
            Text("-10")
        }
        Spacer(modifier = Modifier.width(4.dp))
        FilledTonalIconButton(
            onClick = onRemove,
            colors = IconButtonDefaults.filledTonalIconButtonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        ) {
            Icon(Icons.Default.Delete, contentDescription = "플레이어 삭제")
        }
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(onClick = onMoveUp) { Icon(Icons.Default.KeyboardArrowUp, contentDescription = "위로 이동") }
        IconButton(onClick = onMoveDown) { Icon(Icons.Default.KeyboardArrowDown, contentDescription = "아래로 이동") }
    }
}
