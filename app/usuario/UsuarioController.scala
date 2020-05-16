package usuario

import base.{CRUDController, JsonUtil}
import javax.inject.{Inject, Singleton}
import play.api.libs.json.{JsPath, JsValue, Json, OFormat, OWrites, Reads}
import play.api.mvc.{AnyContent, ControllerComponents, Request, Result}

import scala.concurrent.ExecutionContext

@Singleton
class UsuarioController  @Inject()(implicit ec: ExecutionContext, cc: ControllerComponents, service:UsuarioService) extends CRUDController[Usuario]{

  import reactivemongo.play.json._;
  implicit def format: OFormat[Usuario] = Json.format[Usuario];



}
