package org.aztechs.common.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun VerticalScrollBar(
    state: LazyListState,
    modifier: Modifier = Modifier,
    trackColor: Color = MaterialTheme.colors.contentColorFor(MaterialTheme.colors.background).copy(alpha = 0.1F),
    handleColor: Color = MaterialTheme.colors.surface,
) {
    val totalItems by remember(state) {
        derivedStateOf { state.layoutInfo.totalItemsCount }
    }

    if (totalItems == 0) return

    BoxWithConstraints(
        modifier = modifier
            .fillMaxHeight()
            .padding(2.dp)
            .width(16.dp)
            .drawBehind { drawRoundRect(trackColor, cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())) }
    ) {
        val maxHeightPx = with(LocalDensity.current) { maxHeight.toPx() }
        val top by remember(state) { derivedStateOf { state.firstVisibleItemIndex / totalItems.toFloat() } }
        val height by remember(state) { derivedStateOf { state.layoutInfo.visibleItemsInfo.size / totalItems.toFloat() } }

        var elevation by remember { mutableStateOf(4.dp) }
        val elevationAnimation by animateDpAsState(elevation)
        var dragStart by remember { mutableStateOf(state.firstVisibleItemIndex) }
        var dragAcc by remember { mutableStateOf(0F) }
        val scope = rememberCoroutineScope()

        Surface(
            color = handleColor,
            elevation = elevationAnimation,
            shape = RoundedCornerShape(percent = 50),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(height)
                .offset(y = maxHeight * top)
                .draggable(
                    state = rememberDraggableState { delta ->
                        dragAcc += (delta * totalItems / maxHeightPx)
                        scope.launch {
                            state.scrollToItem((dragStart + dragAcc).roundToInt().coerceIn(0, totalItems))
                        }
                    },
                    orientation = Orientation.Vertical,
                    startDragImmediately = true,
                    onDragStarted = {
                        elevation = 8.dp
                        dragAcc = 0F
                        dragStart = state.firstVisibleItemIndex
                    },
                    onDragStopped = {
                        elevation = 4.dp
                    }
                )
        ) { }
    }
}
