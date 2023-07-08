package app.regate.data.sala

import app.regate.data.dto.ResponseMessage
import app.regate.data.dto.empresa.salas.JoinSalaRequest
import app.regate.data.dto.empresa.salas.SalaDetail
import app.regate.data.dto.empresa.salas.SalaDto
import app.regate.data.dto.empresa.grupo.GrupoMessageDto
import app.regate.data.dto.empresa.salas.SalaRequestDto

interface SalaDataSource {
   suspend fun getSalas(id:Long):List<SalaDto>
   suspend fun getSala(id:Long):SalaDetail
   suspend fun getMessagesSala(id:Long):List<GrupoMessageDto>
   suspend fun joinSala(d:JoinSalaRequest): ResponseMessage

   suspend fun createSala(d: SalaRequestDto):ResponseMessage

}

