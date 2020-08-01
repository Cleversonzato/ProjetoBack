package api.perfil

import java.time.LocalDate

import api.perfil.papel.Papel
import base.Model
import reactivemongo.bson.BSONObjectID

case class Perfil(
   override val id: Option[BSONObjectID],
   papeis: List[Papel],
   nome: Option[String],
   sobrenome: Option[String],
   descricao: Option[String],
   sexo: Option[String],
   dtNascimento: Option[LocalDate],
   cidade: Option[String],
   pais: Option[String]
) extends Model

