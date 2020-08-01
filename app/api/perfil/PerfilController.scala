package api.perfil

import api.perfil.papel.{Administrador, Avaliador, Cliente, Papel, Tipo}
import base.CRUDController
import javax.inject.{Inject, Singleton}
import play.api.libs.json.{Json, OFormat}
import play.api.mvc.ControllerComponents

import scala.concurrent.ExecutionContext

@Singleton
class PerfilController  @Inject()(implicit ec: ExecutionContext, cc: ControllerComponents, service:PerfilService) extends CRUDController[Perfil]{

  import reactivemongo.play.json._
  import reactivemongo.play.json.BSONFormats
  implicit def formatAdministrador: OFormat[Administrador] = Json.format[Administrador]
  implicit def formatAvaliador: OFormat[Avaliador] = Json.format[Avaliador]
  implicit def formatCliente: OFormat[Cliente] = Json.format[Cliente]
  implicit def formatTipoPapel: OFormat[Tipo] = Json.format[Tipo]
  implicit def formatPapel: OFormat[Papel] = Json.format[Papel]
  implicit def format: OFormat[Perfil] = Json.format[Perfil]

  override def modelName: String = "Perfil"
}
