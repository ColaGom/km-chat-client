import androidx.compose.ui.window.Window
import io.colagom.ui.ChatApp
import org.jetbrains.skiko.wasm.onWasmReady

fun main() {
    onWasmReady {
        Window("KMChat - WEB") {
            ChatApp()
        }
    }
}

