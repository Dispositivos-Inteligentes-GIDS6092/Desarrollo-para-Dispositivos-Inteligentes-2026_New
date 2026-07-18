package mx.edu.utng.bgma.smarthealthmonitor.data.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import mx.edu.utng.bgma.smarthealthmonitor.data.repository.SyncRepository

class NeonSyncWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val repository = SyncRepository()
        repository.syncWithNeon()
        return Result.success()
    }
}
