package api.perfil

import java.time.LocalDate

import base.Model
import reactivemongo.bson.BSONObjectID

case class Perfil(
                   override val _id: BSONObjectID,
                   papel: PapelPerfil,
                   nome: Option[String],
                   sobrenome: Option[String],
                   descricao: Option[String],
                   rg: Option[String],
                   cpf: Option[String],
                   telefone: Option[List[String]],
                   sexo: Option[String],
                   dtNascimento: Option[LocalDate],
                   numero: Option[String],
                   rua: Option[String],
                   complemento: Option[String],
                   bairro: Option[String],
                   cep: Option[String],
                   cidade: Option[String],
                   pais: Option[String]
                 )  extends Model
