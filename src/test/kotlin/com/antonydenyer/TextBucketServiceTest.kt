package com.antonydenyer

import com.google.cloud.storage.Blob
import com.google.cloud.storage.Storage
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import java.io.OutputStream
import org.junit.jupiter.api.Test

internal class TextBucketServiceTest {

    @Test
    fun `can get file as string`() {
        val blob = mockk<Blob>()
        val stream = slot<OutputStream>()

        every {
            blob.downloadTo(capture(stream))
        } answers {
            stream.captured.write("content".toByteArray())
        }

        val storage: Storage = mockk()
        every { storage.get("my_bucket", "my_file.txt") } returns blob

        val bucket = TextBucketService("my_bucket", storage)

        val result = bucket.get("my_file.txt")

        assertThat(result, equalTo("content"))
    }
}
