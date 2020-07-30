package base

import javax.inject.Inject
import play.api.mvc._
import play.api.Logging
import play.api.i18n._
import reactivemongo.bson.BSONObjectID
import reactivemongo.play.json._
import scala.concurrent.{ExecutionContext, Future}


abstract class CRUDController[M <: Model]  @Inject()(implicit ec: ExecutionContext, cc: ControllerComponents, service: CRUDService[M])  extends AbstractController(cc) with I18nSupport with Logging with JsonUtil {

  //variaveis

  import play.api.libs.json._
  implicit def format: OFormat[M]
  def modelName:String

  //métodos expostos

  def buscar(id:BSONObjectID):Action[AnyContent] = Action.async  { implicit request: Request[AnyContent] =>
    service.buscar(Json.obj("id" -> id))(request.lang).map{
      case Left(erro)=> NotFound(erro)
      case Right(classe)=>Ok(Json.toJson(classe))
  }}

  def criar():Action[AnyContent] = Action.async { implicit request: Request[AnyContent] => {
    trataJsonRequest(paraModelo, aoCriarModelo)
  }}

  def editar():Action[AnyContent] = Action.async { implicit request: Request[AnyContent] => {
    trataJsonRequest(paraModelo, aoEditarModelo)
  }}

  def remover(id:BSONObjectID) = Action.async { implicit request: Request[AnyContent] => {
    service.remover(id)(request.lang).map{
      case Left(erro)=>InternalServerError(erro)
      case Right(mensagem) => Ok(mensagem)
    }
  }}

  def trataJsonRequest(paraModelo: JsValue=> Either[JsObject, M], acaoComModelo: (M, Lang) =>  Future[Either[JsObject, M]]) (implicit request: Request[AnyContent]): Future[Result] = {
    request.body.asJson.map{ jRequest =>
      (jRequest \ this.modelName).validate[JsValue] match {
        case JsError(erro) => Future(BadRequest( jsonErro(this.modelName + messagesApi("erro.requisicao.indefinido")(request.lang)) ))
        case JsSuccess(value, path) => {
          paraModelo( value ) match {
            case Left(erro) => Future(BadRequest(jsonErro(erro)))
            case Right(modelo) => {
              acaoComModelo(modelo, request.lang).map {
                case Left(erro) => InternalServerError(erro)
                case Right(classe) => Ok(Json.toJson(classe))
              }
            }
          }
        }
      }
    }.getOrElse{
      Future(BadRequest(jsonErro(mensagem = messagesApi("erro.requisicao.json")(request.lang))))
    }
  }

  def paraModelo(jBody:JsValue): Either[JsObject, M] = {
    Json.fromJson[M](jBody) match {
      case JsSuccess(value, path) => Right(value)
      case erro @ JsError(_) => {Left(jsonJsError(erro)) }
    }
  }

  def aoCriarModelo(modelo: M, lang: Lang) : Future[Either[JsObject, M]] ={
    service.criar(modelo)(lang)
  }

  def aoEditarModelo(modelo: M, lang: Lang):  Future[Either[JsObject, M]] ={
    service.editar(modelo)(lang)
  }

}
