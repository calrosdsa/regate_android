package app.regate.data.establecimiento

import app.regate.data.daos.EstablecimientoDao
import app.regate.data.db.DatabaseTransactionRunner
import app.regate.data.dto.empresa.establecimiento.EstablecimientoDto
import app.regate.data.mappers.establecimiento.EstablecimientoDtoToEstablecimiento
import app.regate.inject.ApplicationScope
import app.regate.models.establecimiento.Establecimiento
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreBuilder


@ApplicationScope
@Inject
class EstablecimientoStore(
    establecimientoDao: EstablecimientoDao,
    establecimientoDataSourceImpl: EstablecimientoDataSourceImpl,
    transactionRunner: DatabaseTransactionRunner,
    establecimientoMapper: EstablecimientoDtoToEstablecimiento
):Store<Long, Establecimiento> by StoreBuilder.from(
    fetcher = Fetcher.of {id:Long->
        establecimientoDataSourceImpl.getEstablecimiento(id)
    },
    sourceOfTruth = SourceOfTruth.Companion.of(
        reader ={id:Long->
            establecimientoDao.getEstablecimiento(id)
        },
        writer = { _, response:EstablecimientoDto->
            transactionRunner{
                establecimientoDao.upsert(establecimientoMapper.map(response))
            }
        },
        delete = establecimientoDao::delete,
        deleteAll = establecimientoDao::deleteAll
    )
).build()