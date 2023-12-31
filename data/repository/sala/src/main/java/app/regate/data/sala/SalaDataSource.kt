package app.regate.data.sala

import app.regate.data.dto.ResponseMessage
import app.regate.data.dto.SearchFilterRequest
import app.regate.data.dto.chat.IdDto
import app.regate.data.dto.empresa.salas.JoinSalaRequest
import app.regate.data.dto.empresa.salas.SalaDetail
import app.regate.data.dto.empresa.salas.SalaDto
import app.regate.data.dto.empresa.salas.CompleteSalaRequest
import app.regate.data.dto.empresa.salas.CreateSalaResponse
import app.regate.data.dto.empresa.salas.MessageSalaDto
import app.regate.data.dto.empresa.salas.MessageSalaPagination
import app.regate.data.dto.empresa.salas.PaginationSalaResponse
import app.regate.data.dto.empresa.salas.SalaCompleteDetail
import app.regate.data.dto.empresa.salas.SalaFilterData
import app.regate.data.dto.empresa.salas.SalaRequestDto
import app.regate.data.dto.empresa.salas.UserSalaDto

interface SalaDataSource {
   suspend fun getCompleteSalaHistory(salaId:Long):SalaCompleteDetail
   suspend fun deleteComplete(d:CompleteSalaRequest)
   suspend fun completeSala(d:CompleteSalaRequest)
   suspend fun getEstablecimientoSalas(id:Long):List<SalaDto>
   suspend fun getGrupoSalas(id:Long,page: Int):PaginationSalaResponse
   suspend fun filterSalas(d:SalaFilterData,page:Int):PaginationSalaResponse
   suspend fun getSalasUser(page:Int):PaginationSalaResponse

   suspend fun getSala(id:Long):SalaDetail
   suspend fun joinSala(d:JoinSalaRequest): IdDto
   suspend fun createSala(d: SalaRequestDto):CreateSalaResponse
   suspend fun exitSala(id:Int)
   suspend fun getUsersSala(salaId: Long):List<UserSalaDto>

   suspend fun searchSalas(d:SearchFilterRequest,page:Int,size:Int):PaginationSalaResponse
}

