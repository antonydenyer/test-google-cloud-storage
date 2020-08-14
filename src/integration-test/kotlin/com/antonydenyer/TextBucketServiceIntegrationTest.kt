package com.antonydenyer

import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import org.testcontainers.containers.BindMode
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
internal class TextBucketServiceIntegrationTest {

    @Container
    private val gcs = KGenericContainer("fsouza/fake-gcs-server")
            .withExposedPorts(4443)
            .withClasspathResourceMapping("data", "/data", BindMode.READ_WRITE)
            .withCreateContainerCmdModifier {
                it.withEntrypoint("/bin/fake-gcs-server", "-data", "/data", "-scheme", "http")
            }

    @Test
    fun `can get file as string`() {
        val storage: Storage = StorageOptions.newBuilder()
                .setHost("http://${gcs.host}:${gcs.firstMappedPort}")
                .build()
                .service

        val bucket = TextBucketService("my_bucket", storage)

        val result = bucket.get("my_file.txt")

        assertThat(result, equalTo("content"))
    }
}
