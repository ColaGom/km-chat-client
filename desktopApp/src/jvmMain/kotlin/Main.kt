import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import io.colagom.ui.ChatApp

fun main() =
    singleWindowApplication(
        title = "KMChat - Desktop",
        state = WindowState(size = DpSize(360.dp, 640.dp))
    ) {
        ChatApp()
    }
