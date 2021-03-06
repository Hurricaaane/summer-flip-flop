package eu.ha3.x.sff.system

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import eu.ha3.x.sff.core.Doc
import eu.ha3.x.sff.core.DocListResponse
import eu.ha3.x.sff.core.NoMessage
import eu.ha3.x.sff.test.testBlocking
import io.reactivex.Single
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.ZoneOffset
import java.time.ZonedDateTime

/**
 * (Default template)
 * Created on 2018-12-09
 *
 * @author Ha3
 */
internal class CoroutineToReactiveDocSystemTest {
    private val docSystem = mock<RxDocPersistenceSystem>()
    private val SUT = CoroutineToReactiveDocPersistenceSystem(docSystem)

    @Test
    internal fun `it should list all docs by proxy`() = testBlocking {
        val item = DocListResponse(listOf(Doc("a", ZonedDateTime.of(2000, 12, 1, 23, 40, 50, 0, ZoneOffset.UTC))))
        whenever(docSystem.listAll()).thenReturn(Single.create { source ->
            source.onSuccess(item)
        })

        // Exercise
        val result = SUT.listAll()

        // Verify
        assertThat(result).isEqualTo(item)
    }

    @Test
    internal fun `it should append doc by proxy`() = testBlocking {
        val item = Doc("a", ZonedDateTime.of(2000, 12, 1, 23, 40, 50, 0, ZoneOffset.UTC))
        whenever(docSystem.appendToDocs(item)).thenReturn(Single.create { source ->
            source.onSuccess(NoMessage)
        })

        // Exercise
        val result = SUT.appendToDocs(item)

        // Verify
        assertThat(result).isEqualTo(NoMessage)
    }
}