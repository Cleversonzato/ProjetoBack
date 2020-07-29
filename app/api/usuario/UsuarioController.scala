package api.usuario

import base.{CRUDController, JsonUtil}
import javax.inject.{Inject, Singleton}
import play.api.libs.json.{JsError, JsObject, JsPath, JsResult, JsSuccess, JsValue, Json, OFormat, OWrites, Reads}
import play.api.mvc.{AnyContent, ControllerComponents, Request, Result}

import scala.concurrent.ExecutionContext

@Singleton
class UsuarioController  @Inject()(implicit ec: ExecutionContext, cc: ControllerComponents, service:UsuarioService) extends CRUDController[Usuario]{

  import reactivemongo.play.json._
  import reactivemongo.play.json.BSONFormats
  implicit def format: OFormat[Usuario] = Json.format[Usuario]

  override def modelName: String = "Usuario"
}