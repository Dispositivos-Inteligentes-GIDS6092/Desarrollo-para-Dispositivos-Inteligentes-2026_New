package mx.utng.smarthealthmonitor.tv


import android.content.Context
import com.google.android.gms.cast.CastMediaControlIntent
import com.google.android.gms.cast.framework.CastOptions
import com.google.android.gms.cast.framework.OptionsProvider
import com.google.android.gms.cast.framework.SessionProvider

class SmartHealthCastOptionsProvider : OptionsProvider {

    override fun getCastOptions(context: Context): CastOptions {
        return CastOptions.Builder()
            // DEFAULT_MEDIA_RECEIVER: el receptor genérico de Google
            // Para producción: registrar tu propio App ID en cast.google.com/publish
            .setReceiverApplicationId(
                CastMediaControlIntent.DEFAULT_MEDIA_RECEIVER_APPLICATION_ID
            )
            .build()
    }

    override fun getAdditionalSessionProviders(ctx: Context): List<SessionProvider>? = null
}