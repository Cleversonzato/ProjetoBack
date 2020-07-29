package api.processo

import base.CRUDController
import javax.inject.{Inject, Singleton}
import play.api.libs.json.{Json, OFormat}
import play.api.mvc.ControllerComponents

import scala.concurrent.ExecutionContext

@Singleton
class ProcessoController @Inject()(implicit ec: ExecutionContext, cc: ControllerComponents, service:ProcessoService) extends CRUDController[Processo]{
  import reactivemongo.play.json._
  import reactivemongo.play.json.BSONFormats
  implicit def format: OFormat[Processo] = Json.format[Processo]
  override def modelName: String = "Processo"
}
