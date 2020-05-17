package base

import javax.inject.Inject
import play.api.mvc._
import play.api.Logging
import play.api.i18n.I18nSupport
import reactivemongo.bson.BSONObjectID
import reactivemongo.play.json._

import scala.concurrent.{ExecutionContext, Future}


abstract class CRUDController[M <: Model]  @Inject()(implicit ec: ExecutionContext, cc: ControllerComponents, service: CRUDService[M])  extends AbstractController(cc) with I18nSupport with Logging {

  //variaveis

  import play.api.libs.json._
  implicit def format: OFormat[M]

  //métodos expostos

  def pegar(id:BSONObjectID)= Action.async  { implicit request: Request[AnyContent] =>
    service.buscar(Json.obj("_id" -> id)).map{
      case Left(erro)=> NotFound(erro)
      case Right(classe)=>Ok(Json.toJson(classe))
  }}

  def criar() = Action.async { implicit request: Request[AnyContent] => {
    jsonRequest(aoCriar)
  }}

  def atualizar() = Action.async { implicit request: Request[AnyContent] => {
    jsonRequest(aoAtualizar)
  }}

  def excluir(id:BSONObjectID) = Action.async { implicit request: Request[AnyContent] => {
    service.excluir(id).map{
      case Left(erro)=>InternalServerError(erro)
      case Right(mensagem) => Ok(mensagem)
    }
  }}

  //funções

  def jsonRequest(metodo:M=>Future[Result])(implicit request: Request[AnyContent]):Future[Result] = {
    request.body.asJson.map{ jRequest =>
      Json.fromJson[M](jRequest) match {
        case JsSuccess(value, path) => {
          metodo(value)
        }
        case JsError(errors) =>  Future(BadRequest( JsError.toJson(errors) ))
      }
    }.getOrElse{
      Future(BadRequest(JsonUtil.jsonErro("A requisição não é um JSON válido")))
    }
  }


  def aoCriar(modelo:M) : Future[Result]={
    service.criar(modelo).map {
      case Left(erro) => InternalServerError(erro)
      case Right(classe) => Ok(Json.toJson(classe))
    }
  }

  def aoAtualizar(modelo:M) : Future[Result] = {
    service.atualizar(modelo).map {
      case Left(erro) => InternalServerError(erro)
      case Right(classe) => Ok(Json.toJson(classe))
    }
  }

}
