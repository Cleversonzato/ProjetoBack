package api.perfil

import base.CRUDController
import javax.inject.{Inject, Singleton}
import play.api.libs.json.{Json, OFormat}
import play.api.mvc.ControllerComponents

import scala.concurrent.ExecutionContext

@Singleton
class PerfilController  @Inject()(implicit ec: ExecutionContext, cc: ControllerComponents, service:PerfilService) extends CRUDController[Perfil]{

  import reactivemongo.play.json._
  import reactivemongo.play.json.BSONFormats
  implicit def formatPapel: OFormat[PapelPerfil] = Json.format[PapelPerfil]
  implicit def format: OFormat[Perfil] = Json.format[Perfil]
  override def modelName: String = "Perfil"
}
