package org.dfm.third.party.rest

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.dfm.third.party.domain.model.ThirdParty
import org.dfm.third.party.domain.model.ThirdPartyInfo
import org.dfm.third.party.domain.port.RequestThirdParty

@ExtendWith(MockitoExtension::class)
@SpringBootTest(classes = [ThirdPartyPoetryRestAdapterApplication::class], webEnvironment = RANDOM_PORT)
@EnableAutoConfiguration(exclude = [DataSourceAutoConfiguration::class])
class ThirdPartyResourceTest {
    companion object {
        private const val LOCALHOST = "http://localhost:"
        private const val API_URI = "/api/v1/thirdParties"
    }

    @LocalServerPort
    private val port: Int = 0
    @Autowired
    private lateinit var restTemplate: TestRestTemplate
    @Autowired
    private lateinit var requestThirdParty: RequestThirdParty

    @Test
    fun `should start the rest adapter application`() {
        assertThat(java.lang.Boolean.TRUE).isTrue()
    }

    @Test
    fun `should give thirdParties when asked for thirdParties with the support of domain stub`() {
        // Given
        Mockito.lenient().`when`(requestThirdParty.getThirdParties()).thenReturn(mockThirdPartyInfo())
        // When
        val url = "$LOCALHOST$port$API_URI"
        val responseEntity = restTemplate.getForEntity(url, ThirdPartyInfo::class.java)
        // Then
        assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(responseEntity.body).isNotNull
        assertThat(responseEntity.body.thirdParties).isNotEmpty.extracting("description").contains("Johnny Johnny Yes Papa !!")
    }

    private fun mockThirdParty(id: Long, description: String): ThirdParty {
        return ThirdParty(id, description)
    }

    private fun mockThirdPartyInfo(): ThirdPartyInfo {
        return ThirdPartyInfo(listOf(mockThirdParty(1L, "Johnny Johnny Yes Papa !!")))
    }
}
