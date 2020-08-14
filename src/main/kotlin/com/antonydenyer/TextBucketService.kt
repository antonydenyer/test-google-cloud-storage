package com.antonydenyer

import com.google.cloud.storage.Blob
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import java.io.ByteArrayOutputStream
import java.io.OutputStream

class TextBucketService(
        private val bucket: String,
        private val storage: Storage = StorageOptions.newBuilder()
                .build()
                .service
) {

    fun get(file: String): String {
        return storage.get(bucket, file).stream().toString()
    }
}

private fun Blob.stream(): OutputStream {
    val buffer = ByteArrayOutputStream()
    this.downloadTo(buffer)
    return buffer
}
