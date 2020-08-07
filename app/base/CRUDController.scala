package base

import javax.inject.Inject
import play.api.mvc._
import play.api.Logging
import play.api.i18n._
import reactivemongo.bson.BSONObjectID
import reactivemongo.play.json._
import scala.concurrent.{ExecutionContext, Future}


abstract class CRUDController[M <: Model]  @Inject()(implicit ec: ExecutionContext, cc: ControllerComponents, service: CRUDService[M])
  extends AbstractController(cc) with I18nSupport with Logging with JsonUtil {

  // variaveis

  import play.api.libs.json._
  implicit def format: OFormat[M]
  def modelName:String

  // métodos expostos

  def buscar(id:BSONObjectID):Action[AnyContent] = Action.async  { implicit request: Request[AnyContent] =>
    service.buscar(Json.obj("id" -> id))(request.lang).map{
      case Left(erro)=> NotFound(erro)
      case Right(classe)=>Ok(Json.toJson(classe))
  }}

  def remover(id:BSONObjectID) = Action.async { implicit request: Request[AnyContent] => {
    service.remover(id)(request.lang).map{
      case Left(erro)=>InternalServerError(erro)
      case Right(mensagem) => Ok(mensagem)
    }
  }}

  def criar():Action[AnyContent] = Action.async { implicit request: Request[AnyContent] => {
    implicit def lang = request.lang
    requestParaModelo(request) match {
      case Left(erro) => erro
      case Right(modelo) => criacaoEResultado(modelo)
    }
  }}

  def editar():Action[AnyContent] = Action.async { implicit request: Request[AnyContent] => {
    implicit def lang = request.lang
    requestParaModelo(request) match {
      case Left(erro) => erro
      case Right(modelo) => edicaoEResultado(modelo)
    }
  }}

  // métodos agregadores
  def requestParaModelo(request: Request[AnyContent])(implicit lang:Lang): Either[Future[Result], M] ={
    requestParaBody(request).flatMap( bodyParaJsModel(_).flatMap( jsModelParaModel(_) ) )
  }

  def criacaoEResultado(modelo: M)(implicit lang:Lang): Future[Result]  ={
    criarModelo(modelo).map( retornoParaResult(_) )
  }

  def edicaoEResultado(modelo: M)(implicit lang:Lang): Future[Result]  ={
    editarModelo(modelo).map( retornoParaResult(_))
  }

  // métodos/ funções

  def requestParaBody(request: Request[AnyContent])(implicit lang:Lang): Either[Future[Result], JsValue] ={
    request.body.asJson.map( Right(_) )
        .getOrElse{  Left(Future(BadRequest(jsonErro(mensagem = messagesApi("erro.requisicao.json")))))   }
  }

  def bodyParaJsModel(json: JsValue)(implicit lang:Lang): Either[Future[Result], JsValue] ={
    (json \ this.modelName).validate[JsValue] match {
      case JsSuccess(value, path) => Right(value)
      case JsError(erro) => Left(Future(BadRequest( jsonErro(this.modelName + messagesApi("erro.requisicao.indefinido")) )))
    }
  }

  def jsModelParaModel(json: JsValue)(implicit lang:Lang): Either[Future[Result], M] = {
    Json.fromJson[M](json) match {
      case JsSuccess(value, path) => Right(value)
      case erro @ JsError(_) => Left(Future(BadRequest(jsonJsError(erro))))
    }
  }

  def criarModelo(modelo: M)(implicit lang:Lang): Future[Either[JsObject, M]] = {
    service.criar(modelo)(lang)
  }

  def editarModelo(modelo: M)(implicit lang:Lang): Future[Either[JsObject, M]] = {
    service.editar(modelo)(lang)
  }

  def retornoParaResult(retorno: Either[JsObject, M])(implicit lang:Lang): Result ={
    retorno match {
        case Left(erro) => InternalServerError(erro)
        case Right(modelo) => modeloParaResult(modelo)
      }
  }

  def modeloParaResult(modelo:M)(implicit lang:Lang): Result={
    Ok( Json.obj(this.modelName ->  Json.toJson(modelo)) )
  }


}
