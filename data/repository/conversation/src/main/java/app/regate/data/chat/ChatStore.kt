package app.regate.data.chat

import app.regate.data.daos.ChatDao
import app.regate.data.daos.EstablecimientoDao
import app.regate.data.db.DatabaseTransactionRunner
import app.regate.data.dto.chat.ChatDto
import app.regate.data.dto.empresa.establecimiento.EstablecimientoDto
import app.regate.data.mappers.establecimiento.EstablecimientoDtoToEstablecimiento
import app.regate.inject.ApplicationScope
import app.regate.models.chat.Chat
import app.regate.models.establecimiento.Establecimiento
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreBuilder

data class ChatParams(
    val parent_id:Long,
    val typeChat:Int,
    val name:String = "",
    val photo:String? = null
)

@ApplicationScope
@Inject
class ChatStore(
    chatDao: ChatDao,
    chatDataSourceImpl: ChatDataSourceImpl,
    transactionRunner: DatabaseTransactionRunner,
):Store<ChatParams, Chat> by StoreBuilder.from(
    fetcher = Fetcher.of {params:ChatParams->
       chatDataSourceImpl.getChat(params.parent_id,params.typeChat).copy(
           name = params.name,
           photo = params.photo
       )
    },
    sourceOfTruth = SourceOfTruth.Companion.of(
        reader ={params:ChatParams->
            chatDao.observeChatByType(params.parent_id,params.typeChat)
        },
        writer = { _, response:ChatDto->
            transactionRunner{
               chatDao.insertOnConflictIgnore(
                   Chat(
                       id = response.id,
                       parent_id = response.parent_id,
                       type_chat = response.type_chat,
                       name = response.name,
                       photo = response.photo
                   )
               )
            }
        },
        delete = {},
        deleteAll = {}
    )
).build()