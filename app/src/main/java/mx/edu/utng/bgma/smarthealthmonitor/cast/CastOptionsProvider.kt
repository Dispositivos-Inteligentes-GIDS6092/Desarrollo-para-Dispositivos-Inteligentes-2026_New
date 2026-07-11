package mx.edu.utng.bgma.smarthealthmonitor.cast

import android.content.Context
import com.google.android.gms.cast.framework.CastOptions
import com.google.android.gms.cast.framework.OptionsProvider
import com.google.android.gms.cast.framework.SessionProvider
import com.google.android.gms.cast.framework.media.CastMediaOptions
import com.google.android.gms.cast.framework.media.NotificationOptions
import com.google.android.gms.cast.MediaControlIntent
import org.tensorflow.lite.schema.CastOptions

class CastOptionsProvider : OptionsProvider {

    override fun getCastOptions(context: Context): CastOptions {
        return CastOptions.Builder()
            .setReceiverApplicationId(
                CastMediaControlIntent.DEFAULT_MEDIA_RECEIVER_APPLICATION_ID
            )
            .setCastMediaOptions(
                CastMediaOptions.Builder()
                    .setNotificationOptions(
                        NotificationOptions.Builder()
                            .setActions(
                                listOf(
                                    NotificationOptions.ACTION_SKIP_PREV,
                                    NotificationOptions.ACTION_PLAY_PAUSE,
                                    NotificationOptions.ACTION_SKIP_NEXT
                                )
                            )
                            .build()
                    )
                    .build()
            )
            .build()
    }

    override fun getAdditionalSessionProviders(context: Context): List<SessionProvider> {
        return emptyList()
    }
}