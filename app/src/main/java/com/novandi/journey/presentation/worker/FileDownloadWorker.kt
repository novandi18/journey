package com.novandi.journey.presentation.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.novandi.utility.consts.WorkerConsts
import com.novandi.utility.data.getSavedFileUri

class FileDownloadWorker(
    private val context: Context,
    workerParameters: WorkerParameters
): CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        val fileUrl = inputData.getString(WorkerConsts.KEY_FILE_URL) ?: ""
        val fileName = inputData.getString(WorkerConsts.KEY_FILE_NAME) ?: ""
        val fileType = inputData.getString(WorkerConsts.KEY_FILE_TYPE) ?: ""

        if (
            fileName.isEmpty()
            || fileType.isEmpty()
            || fileUrl.isEmpty()
        ) {
            Result.failure()
        }

        val uri = getSavedFileUri(
            fileName = fileName,
            fileType = fileType,
            fileUrl = fileUrl,
            context = context
        )

        return if (uri != null) {
            Result.success(workDataOf(WorkerConsts.KEY_FILE_URI to uri.toString()))
        } else {
            Result.failure()
        }
    }
}