package mx.edu.utng.bgma.smarthealthmonitor.wear.presentation.watchface

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.SurfaceHolder
import androidx.wear.watchface.DrawMode
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.style.CurrentUserStyleRepository
import mx.edu.utng.bgma.smarthealthmonitor.data.SmartHealthRepository
import java.time.ZonedDateTime
import java.util.Locale

class SmartHealthRenderer(
    surfaceHolder: SurfaceHolder,
    currentUserStyleRepository: CurrentUserStyleRepository,
    watchState: WatchState,
    canvasType: Int,
    interactiveDrawModeUpdateDelayMillis: Long,
    clearWithBackgroundTintBeforeRenderingHighlightLayer: Boolean
) : Renderer.CanvasRenderer2<SmartHealthRenderer.SmartHealthSharedAssets>(
    surfaceHolder,
    currentUserStyleRepository,
    watchState,
    canvasType,
    interactiveDrawModeUpdateDelayMillis,
    clearWithBackgroundTintBeforeRenderingHighlightLayer
) {

    class SmartHealthSharedAssets : SharedAssets {
        override fun onDestroy() {}
    }

    private val textPaint = Paint().apply {
        isAntiAlias = true
        color = Color.WHITE
        textSize = 40f
        textAlign = Paint.Align.CENTER
    }

    private val subTextPaint = Paint().apply {
        isAntiAlias = true
        color = Color.CYAN
        textSize = 24f
        textAlign = Paint.Align.CENTER
    }

    override suspend fun createSharedAssets(): SmartHealthSharedAssets {
        return SmartHealthSharedAssets()
    }

    override fun render(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: SmartHealthSharedAssets
    ) {
        canvas.drawColor(Color.BLACK)

        val isAOD = renderParameters.drawMode == DrawMode.AMBIENT

        // Dibujar Hora
        val hora = String.format(Locale.getDefault(), "%02d:%02d", zonedDateTime.hour, zonedDateTime.minute)
        textPaint.color = if (isAOD) Color.GRAY else Color.WHITE
        canvas.drawText(hora, bounds.centerX().toFloat(), bounds.centerY().toFloat() - 20f, textPaint)

        if (!isAOD) {
            // Dibujar FC desde el repositorio
            val fc = SmartHealthRepository.fcFlow.value
            val textoFC = if (fc > 0) "❤️ $fc bpm" else "-- bpm"
            canvas.drawText(textoFC, bounds.centerX().toFloat(), bounds.centerY().toFloat() + 30f, subTextPaint)
            
            // Dibujar Pasos
            val pasos = SmartHealthRepository.pasosFlow.value
            val textoPasos = "🏃 $pasos"
            canvas.drawText(textoPasos, bounds.centerX().toFloat(), bounds.centerY().toFloat() + 65f, subTextPaint)
        }
    }

    override fun renderHighlightLayer(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: SmartHealthSharedAssets
    ) {
        // No necesario para este ejemplo simple
    }
}
