package api.perfil.publico

import base.CRUDController
import javax.inject.{Inject, Singleton}
import play.api.libs.json.{Json, OFormat}
import play.api.mvc.ControllerComponents


import scala.concurrent.ExecutionContext

@Singleton
class PerfilPublicoController @Inject()(implicit ec: ExecutionContext, cc: ControllerComponents, service:PerfilPublicoService) extends CRUDController[PerfilPublico]{

  import reactivemongo.play.json._
  import reactivemongo.play.json.BSONFormats
  implicit def formatFormacao: OFormat[Formacao] = Json.format[Formacao]
  implicit def formatExperiencia: OFormat[Experiencia] = Json.format[Experiencia]
  implicit def format: OFormat[PerfilPublico] = Json.format[PerfilPublico]
  override def modelName: String = "PerfilPublico"

}
