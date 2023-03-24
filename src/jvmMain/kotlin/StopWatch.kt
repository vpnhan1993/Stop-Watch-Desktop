import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.*
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class StopWatch {

    var formattedTime by mutableStateOf("00:00:000")
    private var coroutineScope = CoroutineScope(Dispatchers.Main)
    private var isActive = false
    private var timeInMili = 0L
    private var lastTime = 0L

    fun start() {
        if (this@StopWatch.isActive) return

        coroutineScope.launch {
            lastTime = System.currentTimeMillis()
            this@StopWatch.isActive = true

            while (this@StopWatch.isActive) {
                delay(10L)
                timeInMili += System.currentTimeMillis() - lastTime
                lastTime = System.currentTimeMillis()
                formattedTime = formatTime(timeInMili)
            }
        }
    }

    fun pause() {
        isActive = false
    }

    fun reset() {
        coroutineScope.cancel()
        coroutineScope = CoroutineScope(Dispatchers.Main)
        timeInMili = 0L
        lastTime = 0L
        formattedTime = "00:00:000"
        isActive = false
    }

    private fun formatTime(timeInMili: Long): String {
        val localDateTime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(timeInMili), ZoneId.systemDefault()
        )
        val formatter = DateTimeFormatter.ofPattern("mm:ss:SSS", Locale.getDefault())
        return localDateTime.format(formatter)
    }
}