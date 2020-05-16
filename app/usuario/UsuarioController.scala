package usuario

import base.CRUDController
import javax.inject.{Inject, Singleton}
import play.api.libs.json.{JsPath, JsValue, Json, OFormat, OWrites, Reads}
import play.api.mvc.{AnyContent, ControllerComponents, Request, Result}

import scala.concurrent.ExecutionContext

@Singleton
class UsuarioController  @Inject()(implicit ec: ExecutionContext, cc: ControllerComponents, service:UsuarioService) extends CRUDController[Usuario]{

  import reactivemongo.play.json._
//  override implicit def writer: OWrites[Usuario] = Json.writes[Usuario];
//  override implicit def reader: Reads[Usuario] = Json.reads[Usuario]
implicit def format: OFormat[Usuario] = Json.format[Usuario];

//  def autenticar() = Action { implicit request: Request[AnyContent] =>
//    jsonRequest(teste)
//  }
  //Implicits



  def teste(valor:JsValue) = {
    val jsonTransformer = (  JsPath \ "teste").json.prune
    Ok(valor.transform(jsonTransformer).get.as[JsValue])
  }




}
