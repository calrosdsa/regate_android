package app.regate.data.sala

import app.regate.data.dto.ResponseMessage
import app.regate.data.dto.empresa.salas.JoinSalaRequest
import app.regate.data.dto.empresa.salas.SalaDetail
import app.regate.data.dto.empresa.salas.SalaDto
import app.regate.data.dto.empresa.grupo.GrupoMessageDto
import app.regate.data.dto.empresa.salas.MessageSalaDto
import app.regate.data.dto.empresa.salas.MessageSalaPagination
import app.regate.data.dto.empresa.salas.PaginationSalaResponse
import app.regate.data.dto.empresa.salas.SalaFilterData
import app.regate.data.dto.empresa.salas.SalaRequestDto

interface SalaDataSource {
   suspend fun syncMessages(d:List<MessageSalaDto>):List<MessageSalaDto>
   suspend fun getEstablecimientoSalas(id:Long):List<SalaDto>
   suspend fun getGrupoSalas(id:Long,page: Int):PaginationSalaResponse
   suspend fun filterSalas(d:SalaFilterData,page:Int):PaginationSalaResponse
   suspend fun getSala(id:Long):SalaDetail
   suspend fun getMessagesSala(id:Long,page: Int):MessageSalaPagination
   suspend fun joinSala(d:JoinSalaRequest): ResponseMessage
   suspend fun createSala(d: SalaRequestDto):ResponseMessage
   suspend fun exitSala(id:Int)
}

