package base

import javax.inject.Inject
import play.api.mvc._
import play.api.Logging
import play.api.i18n.I18nSupport
import play.api.libs.json.JsValue
import reactivemongo.bson.BSONObjectID
import reactivemongo.play.json.JSONSerializationPack.Writer
import reactivemongo.play.json._

import scala.concurrent.ExecutionContext


abstract class CRUDController[M <: Model]  @Inject()(implicit ec: ExecutionContext, cc: ControllerComponents, service: CRUDService[M])  extends AbstractController(cc) with I18nSupport with Logging {

  import play.api.libs.json._
//  implicit def writer: OWrites[M]
//  implicit def reader:Reads[M]
implicit def format: OFormat[M]

  def jsonRequest(metodo:M=>Result)(implicit request: Request[AnyContent]):Result = {
    request.body.asJson.map{ jRequest =>
      Json.fromJson[M](jRequest) match {
        case JsSuccess(value, path) =>   metodo(value)
        case JsError(errors) =>  BadRequest( JsError.toJson(errors) )
      }
    }.getOrElse{
      BadRequest(JsonUtil.jsonErro("A requisição não é um JSON válido"))
    }
  }

//  def metodo(modelo: M): Result

  def pegar(id:BSONObjectID)= Action.async  { implicit request: Request[AnyContent] =>
    service.buscar(Json.obj("_id" -> id)).map{
      case Left(erro)=> {println(erro.keys); NotFound(erro.toString())}
      case Right(classe)=>Ok(Json.toJson(classe))
    }
  }

  def criar() = Action { implicit request: Request[AnyContent] => {
      jsonRequest(criarMetodo)
    }
  }

  def criarMetodo(modelo:M) : Result =  {
      println(modelo)
      NotImplemented
  }


}
