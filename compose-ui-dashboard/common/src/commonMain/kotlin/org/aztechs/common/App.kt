package org.aztechs.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.wpi.first.networktables.NetworkTableEntry
import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.networktables.NetworkTableType
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import org.aztechs.common.ui.VerticalScrollBar

@Composable
fun App() {
    var entries by remember { mutableStateOf(emptyArray<NetworkTableEntry>()) }
    val listState = rememberLazyListState()

    Row(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1F).padding(8.dp)
        ) {
            items(entries.sortedBy { it.name }) { entry ->
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    Text(entry.name)
                    Text(entry.type.name)
                    Text(
                        with(entry.value) {
                            when (entry.type) {
                                NetworkTableType.kUnassigned -> "<unassigned>"
                                NetworkTableType.kBoolean -> boolean
                                NetworkTableType.kDouble -> double
                                NetworkTableType.kString -> string
                                NetworkTableType.kRaw -> "<byte array>"
                                NetworkTableType.kBooleanArray -> booleanArray.joinToString()
                                NetworkTableType.kDoubleArray -> doubleArray.joinToString()
                                NetworkTableType.kStringArray -> stringArray.joinToString()
                                NetworkTableType.kRpc -> "<RPC>"
                                else -> "<null>"
                            }.toString()
                        }
                    )
                }
            }
        }

        VerticalScrollBar(listState)
    }

    LaunchedEffect(Unit) {
        val inst = NetworkTableInstance.getDefault()
        inst.startClient("localhost")

        while (isActive) {
            delay(1000)
            entries = inst.getEntries("", 0)
        }
    }
}
