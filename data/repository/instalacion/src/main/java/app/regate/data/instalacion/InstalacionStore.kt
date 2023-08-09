package app.regate.data.instalacion

import app.regate.data.daos.InstalacionDao
import app.regate.data.db.DatabaseTransactionRunner
import app.regate.data.dto.empresa.instalacion.InstalacionDto
import app.regate.data.mappers.toInstalacion
import app.regate.inject.ApplicationScope
import app.regate.models.Instalacion
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreBuilder


@ApplicationScope
@Inject
class InstalacionStore(
    instalacionDao: InstalacionDao,
    instalacionDataSourceImpl: InstalacionDataSourceImpl,
    transactionRunner: DatabaseTransactionRunner
):Store<Long,Instalacion> by StoreBuilder.from(
    fetcher = Fetcher.of {
        instalacionDataSourceImpl.getInstalacion(it)
    },
    sourceOfTruth = SourceOfTruth.of(
        reader = {id:Long->
            instalacionDao.observeInstalacion(id)
        },
        writer = {_,response:Instalacion->
            transactionRunner{
//                val instalaciones = response.map { it.toInstalacion() }
                instalacionDao.upsert(response)
            }
        },
        delete = instalacionDao::delete,
        deleteAll = instalacionDao::deleteAll
    )
).build()