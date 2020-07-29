package api.perfil.publico

import java.time.{LocalDate, LocalDateTime}

import base.Model
import reactivemongo.bson.BSONObjectID

case class PerfilPublico(
                          override val id: Option[BSONObjectID],
                          idPerfil: BSONObjectID,
                          ultimaAlteracao: LocalDateTime,
                          tituloPerfil: String,
                          nome: String,
                          dtNascimento: Option[LocalDate],
                          contatos: Option[List[String]],
                          informacoesAdicionais: Option[String],
                          formacaoes: Option[List[Formacao]],
                          experiencias:Option[List[Experiencia]]
                        )
  extends Model

