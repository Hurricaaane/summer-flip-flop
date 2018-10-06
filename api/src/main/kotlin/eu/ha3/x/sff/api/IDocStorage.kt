package eu.ha3.x.sff.api

import eu.ha3.x.sff.core.Doc
import io.reactivex.Single

/**
 * (Default template)
 * Created on 2018-10-06
 *
 * @author gyam
 */
interface IDocStorage {
    fun listAll(): Single<List<Doc>>
}