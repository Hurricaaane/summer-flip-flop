package eu.ha3.x.sff.api

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.stub
import eu.ha3.x.sff.core.*
import eu.ha3.x.sff.system.SDocPersistenceSystem
import eu.ha3.x.sff.test.testBlocking
import eu.ha3.x.sff.test.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime

/**
 * (Default template)
 * Created on 2018-10-06
 *
 * @author Ha3
 */
internal class ReactiveDocStorageTest {
    private val mockDocSystem = mock<SDocPersistenceSystem>()
    private val mockCurrentTimeFn = mock<() -> ZonedDateTime>()
    private val SUT = ReactiveDocStorage(mockDocSystem, currentTimeFn = mockCurrentTimeFn)

    @Test
    internal fun `it should list all docs from doc system`() = testBlocking {
        val expected = DocListResponse(listOf(Doc("basicName", ZonedDateTime.now())))
        mockDocSystem.stub {
            onBlocking { listAll() }.doReturn(expected)
        }

        // Exercise
        val result = SUT.listAll()

        // Verify
        result.test()
                .assertNoErrors()
                .assertValue(verify {
                    assertThat(this).isEqualTo(expected)
                })
    }

    @Test
    internal fun `it should append a doc to the system`() = testBlocking {
        val currentTime = ZonedDateTime.now()
        val expected = Doc("basicName", currentTime)
        mockDocSystem.stub {
            onBlocking { appendToDocs(expected) }.doReturn(NoMessage)
        }
        mockCurrentTimeFn.stub {
            on { invoke() }.doReturn(currentTime)
        }

        // Exercise
        val result = SUT.appendToDocs(DocCreateRequest("basicName"))

        // Verify
        result.test()
                .assertNoErrors()
                .assertValue(verify {
                    assertThat(this).isEqualTo(expected)
                })
    }

    @Test
    internal fun `it should list pagianted from doc system`() = testBlocking {
        val expected = DocListResponse(listOf(Doc("basicName", ZonedDateTime.now())))
        mockDocSystem.stub {
            onBlocking { listPaginated(PaginatedPersistence(1)) }.doReturn(expected)
        }

        // Exercise
        val result = SUT.listPaginated(DocListPaginationRequest(1))

        // Verify
        result.test()
              .assertNoErrors()
              .assertValue(verify {
                  assertThat(this).isEqualTo(expected)
              })
    }
}