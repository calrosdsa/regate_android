package app.regate.data.sala

import app.regate.data.dto.ResponseMessage
import app.regate.data.dto.empresa.salas.JoinSalaRequest
import app.regate.data.dto.empresa.salas.SalaDetail
import app.regate.data.dto.empresa.salas.SalaDto
import app.regate.data.dto.empresa.grupo.GrupoMessageDto
import app.regate.data.dto.empresa.salas.CompleteSalaRequest
import app.regate.data.dto.empresa.salas.MessageSalaDto
import app.regate.data.dto.empresa.salas.MessageSalaPagination
import app.regate.data.dto.empresa.salas.PaginationSalaResponse
import app.regate.data.dto.empresa.salas.SalaCompleteDetail
import app.regate.data.dto.empresa.salas.SalaCompleteDto
import app.regate.data.dto.empresa.salas.SalaFilterData
import app.regate.data.dto.empresa.salas.SalaRequestDto
import app.regate.data.dto.empresa.salas.UserSala

interface SalaDataSource {
   suspend fun getCompleteSalaHistory(salaId:Long):SalaCompleteDetail
   suspend fun completeSala(d:CompleteSalaRequest)
   suspend fun syncMessages(d:List<MessageSalaDto>):List<MessageSalaDto>
   suspend fun getEstablecimientoSalas(id:Long):List<SalaDto>
   suspend fun getGrupoSalas(id:Long,page: Int):PaginationSalaResponse
   suspend fun filterSalas(d:SalaFilterData,page:Int):PaginationSalaResponse
   suspend fun getSalasUser(page:Int):PaginationSalaResponse

   suspend fun getSala(id:Long):SalaDetail
   suspend fun getMessagesSala(id:Long,page: Int):MessageSalaPagination
   suspend fun joinSala(d:JoinSalaRequest): ResponseMessage
   suspend fun createSala(d: SalaRequestDto):ResponseMessage
   suspend fun exitSala(id:Int)
   suspend fun getUsersSala(salaId: Long):List<UserSala>
}

