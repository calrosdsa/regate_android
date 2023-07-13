package app.regate.data.grupo

import app.regate.data.dto.ResponseMessage
import app.regate.data.dto.account.user.ProfileDto
import app.regate.data.dto.empresa.grupo.AddUserGrupoRequest
import app.regate.data.dto.empresa.grupo.FilterGrupoData
import app.regate.data.dto.empresa.grupo.GrupoDto
import app.regate.data.dto.empresa.salas.SalaDetail
import app.regate.data.dto.empresa.salas.SalaDto
import app.regate.data.dto.empresa.grupo.GrupoMessageDto
import app.regate.data.dto.empresa.grupo.GrupoResponse
import app.regate.models.Message

interface GrupoDataSource {
   suspend fun syncMessages(d:List<GrupoMessageDto>):List<GrupoMessageDto>
   suspend fun filterGrupos(d:FilterGrupoData):List<GrupoDto>
   suspend fun getGrupo(id:Long):GrupoResponse
   suspend fun getMessagesGrupo(id:Long,page:Int):List<GrupoMessageDto>
   suspend fun joinGrupo(d:AddUserGrupoRequest): ResponseMessage
   suspend fun getUsersGrupo(id:Long):List<ProfileDto>

//   suspend fun createGrupo(d: SalaRequestDto):ResponseMessage

}

