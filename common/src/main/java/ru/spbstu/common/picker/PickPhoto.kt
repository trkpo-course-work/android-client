package ru.spbstu.common.picker

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract

class PickPhoto : ActivityResultContract<Int, Uri?>() {
    override fun createIntent(context: Context, input: Int?): Intent =
        Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        if (resultCode != Activity.RESULT_OK) {
            return null
        }
        return intent?.data
    }
}