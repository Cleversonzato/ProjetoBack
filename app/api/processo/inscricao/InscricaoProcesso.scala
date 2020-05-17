package api.processo.inscricao

import java.time.LocalDateTime

import base.Model
import reactivemongo.bson.BSONObjectID

case class InscricaoProcesso (
                               override  val _id: BSONObjectID,
                              idPerfil: BSONObjectID,
                              idPerfilPublico: BSONObjectID,
                              nomeInscrito:String,
                              idProcesso: BSONObjectID,
                              tituloProcesso:String,
                              horaInscricao:LocalDateTime
                             )
  extends Model