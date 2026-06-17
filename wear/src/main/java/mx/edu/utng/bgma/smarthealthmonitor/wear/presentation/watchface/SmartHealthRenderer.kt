package mx.edu.utng.bgma.smarthealthmonitor.wear.presentation.watchface


import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.view.SurfaceHolder
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.RenderParameters
import androidx.wear.watchface.ComplicationsSlotsManager
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.style.CurrentUserStyleRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.runningFold
import java.time.ZonedDateTime

class SmartHealthRenderer(
    private val context: Context,
    surfaceHolder: SurfaceHolder,
    watchState: WatchState,
    complicationSlotsManager: ComplicationsSlotsManager,
    currentUserStyleRepository: CurrentUserStyleRepository,
    interactiveDrawModeUpdateDelayMillis: Long
) : Renderer.CanvasRenderer2<Renderer.SharedAssets>(
    surfaceHolder,
    currentUserStyleRepository,
    watchState,
    CanvasType.HARDWARE,
    interactiveDrawModeUpdateDelayMillis
) {

    private val paintHora = Paint().apply {
        color = Color.WHITE
        textSize = 72f
        isAntiAlias = true
        typeface = Typeface.DEFAULT_BOLD
        textAlign = Paint.Align.CENTER
    }

    private val paintFC = Paint().apply {
        color = Color.RED
        textSize = 30f
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }

    private val paintSub = Paint().apply {
        color = Color.GRAY
        textSize = 22f
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }

    private val paintAOD = Paint().apply {
        color = Color.WHITE
        textSize = 72f
        isAntiAlias = false  // Ahorra batería en AOD
        typeface = Typeface.DEFAULT_BOLD
        textAlign = Paint.Align.CENTER
    }

    private val paintAODSub = Paint().apply {
        color = Color.GRAY
        textSize = 22f
        isAntiAlias = false  // Ahorra batería en AOD
        textAlign = Paint.Align.CENTER
    }

    override suspend fun createSharedAssets(): SharedAssets {
        return object : SharedAssets {
            override fun onDestroy() {}
        }
    }

    override fun render(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: SharedAssets
    ) {
        // Fondo negro - ahorra batería
        canvas.drawColor(Color.BLACK)

        val cx = bounds.exactCenterX()
        val cy = bounds.exactCenterY()

        // Verificar si estamos en modo AOD
        val isAOD = renderParameters.drawMode == RenderParameters.DrawMode.AMBIENT

        // Elegir pinturas según modo
        val paintHoraActual = if (isAOD) paintAOD else paintHora
        val paintSubActual = if (isAOD) paintAODSub else paintSub

        // Hora digital centrada
        val hora = String.format("%02d:%02d",
            zonedDateTime.hour,
            zonedDateTime.minute
        )
        canvas.drawText(hora, cx, cy - 10f, paintHoraActual)

        // Segundo (pequeño debajo)
        val seg = String.format("%02d", zonedDateTime.second)
        canvas.drawText(seg, cx, cy + 30f, paintSubActual)

        // FC solo en modo interactivo (ahorra batería en AOD)
        if (!isAOD) {
            val fc = SmartHealthRepository.fcFlow.value
            if (fc > 0) {
                val fcStr = "♥ $fc bpm"
                canvas.drawText(fcStr, cx, cy + 70f, paintFC)
            }
        }
    }

    override fun renderHighlightLayer(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: SharedAssets
    ) {
        // Solo para depuración
        renderParameters.highlightLayer?.let {
            canvas.drawColor(it.backgroundTint)
        }
    }
}