package api.processo.inscricao

import base.CRUDController
import javax.inject.{Inject, Singleton}
import play.api.libs.json.{Json, OFormat}
import play.api.mvc.ControllerComponents

import scala.concurrent.ExecutionContext

@Singleton
class InscricaoProcessoController @Inject()(implicit ec: ExecutionContext, cc: ControllerComponents, service:InscricaoProcessoService) extends CRUDController[InscricaoProcesso]{

  import reactivemongo.play.json._
  import reactivemongo.play.json.BSONFormats
  implicit def format: OFormat[InscricaoProcesso] = Json.format[InscricaoProcesso];

}
