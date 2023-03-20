package xyz.tberghuis.floatingtimer.service

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import xyz.tberghuis.floatingtimer.OverlayViewHolder
import xyz.tberghuis.floatingtimer.logd

// doitwrong
@Composable
fun ClickTarget(
  serviceState: ServiceState,
  controller: OverlayController,
  overlayState: OverlayState,
  viewHolder: OverlayViewHolder,
  onDropOnTrash: () -> Unit,
  onClick: () -> Unit
) {
  Box(modifier = Modifier
    .pointerInput(Unit) {
      detectDragGestures(onDragStart = {
        logd("clicktarget onDragStart")
        overlayState.showTrash = true
      }, onDrag = { change, dragAmount ->
        change.consume()
        val dragAmountIntOffset = IntOffset(dragAmount.x.roundToInt(), dragAmount.y.roundToInt())
        val _timerOffset = overlayState.timerOffset + dragAmountIntOffset
        var x = max(_timerOffset.x, 0)
        x = min(x, serviceState.screenWidthPx - controller.timerSizePx)
        var y = max(_timerOffset.y, 0)
        y = min(y, serviceState.screenHeightPx - controller.timerSizePx)
        overlayState.timerOffset = IntOffset(x, y)
      }, onDragEnd = {
        logd("onDragEnd")
        overlayState.showTrash = false

        if (overlayState.isTimerHoverTrash) {
          onDropOnTrash()
          return@detectDragGestures
        }

        viewHolder.params.x = overlayState.timerOffset.x
        viewHolder.params.y = overlayState.timerOffset.y
        logd("onDragEnd x ${overlayState.timerOffset.x}")
        controller.windowManager.updateViewLayout(viewHolder.view, viewHolder.params)
      })
    }
    .clickable {
      onClick()
    }) {
//        Text("click target")
  }
}