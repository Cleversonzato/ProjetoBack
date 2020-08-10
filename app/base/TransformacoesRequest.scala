//package base
//
//import api.usuario.Usuario
//import javax.inject.Inject
//import play.api.i18n.Lang
//import play.api.libs.json.{JsError, JsObject, JsSuccess, JsValue, Json}
//import play.api.mvc.{AnyContent, Request, Result, _}
//import play.api.i18n._
//
//import scala.concurrent.{ExecutionContext, Future}
//
//trait TransformacoesRequest[D] {
//  def apply(request: Request[AnyVal]): Either[Future[Result], D]
//}
//
//// implementação da trait
//object TransformacoesRequest {
//
//  // possibilita o requestParaDados(request)
//  def requestParaDados[D](request: Request[AnyVal])(implicit transformador: TransformacoesRequest[D] ): Either[Future[Result], D] = {
//    transformador(request)
//  }
//
//  // para possibilitar o request.paraDados
//  implicit class TransformacoesRequestOps[D](request: Request[AnyVal]){
//    def paraDados = TransformacoesRequestOps[D].apply(request)
//  }
//
//  // definição-implementação do comportamento com uma função genética como parâmetro
//  def instance[D](transformador: Request[AnyVal] => Either[Future[Result], D]): TransformacoesRequest[D] = {
//    new TransformacoesRequest[D] {
//      def apply(request: Request[AnyVal]): Either[Future[Result], D] = transformador(request)
//    }
//  }
//
//  //definindo os valores do transformador acima
//
//  implicit val transformadorGenerico: TransformacoesRequest[Usuario]={
//    instance(funcDeTransformação)
//  }
//
//  // definição-implementação do comportamento com um usuário
//
//  implicit val requestParaUsuario: TransformacoesRequest[Usuario] = {
//    new TransformacoesRequest[Usuario] {
//      def apply(request: Request[AnyVal]): Either[Future[Result], Usuario] = ???
//    }
//  }
//
//
//
//  def funcDeTransformação[](request: Request[AnyVal]):Either[Future[Result], D] = ???
//
//  //def requestParaModelo2(request: Request[AnyContent])(implicit lang:Lang): Either[Future[Result], Usuario] = ???
//
//
////trait TransformacoesRequest[M <: Model] @Inject()(implicit ec: ExecutionContext, service: CRUDService[M]) extends JsonUtil  with I18nSupport {
////
////  def modelName:String
////  import play.api.libs.json._
////  implicit def format: OFormat[M]
////
////  // métodos agregadores
////  def requestParaModelo(request: Request[AnyContent])(implicit lang:Lang): Either[Future[Result], M] ={
////    requestParaBody(request).flatMap( bodyParaJsModel(_).flatMap( jsModelParaModel(_) ) )
////  }
////
////  def criacaoEResultado(modelo: M)(implicit lang:Lang): Future[Result]  ={
////    criarModelo(modelo).map( retornoParaResult(_) )
////  }
////
////  def edicaoEResultado(modelo: M)(implicit lang:Lang): Future[Result]  ={
////    editarModelo(modelo).map( retornoParaResult(_))
////  }
////
////  // métodos/ funções
////
////  def requestParaBody(request: Request[AnyContent])(implicit lang:Lang): Either[Future[Result], JsValue] ={
////    request.body.asJson.map( Right(_) )
////      .getOrElse{  Left(Future(Results.BadRequest(jsonErro(mensagem = messagesApi("erro.requisicao.json")))))   }
////  }
////
////  def bodyParaJsModel(json: JsValue)(implicit lang:Lang): Either[Future[Result], JsValue] ={
////    (json \ this.modelName).validate[JsValue] match {
////      case JsSuccess(value, path) => Right(value)
////      case JsError(erro) => Left(Future(Results.BadRequest( jsonErro(this.modelName + messagesApi("erro.requisicao.indefinido")) )))
////    }
////  }
////
////  def jsModelParaModel(json: JsValue)(implicit lang:Lang): Either[Future[Result], M] = {
////    Json.fromJson[M](json) match {
////      case JsSuccess(value, path) => Right(value)
////      case erro @ JsError(_) => Left(Future(Results.BadRequest(jsonJsError(erro))))
////    }
////  }
////
////  def criarModelo(modelo: M)(implicit lang:Lang): Future[Either[JsObject, M]] = {
////    service.criar(modelo)(lang)
////  }
////
////  def editarModelo(modelo: M)(implicit lang:Lang): Future[Either[JsObject, M]] = {
////    service.editar(modelo)(lang)
////  }
////
////  def retornoParaResult(retorno: Either[JsObject, M])(implicit lang:Lang): Result ={
////    retorno match {
////      case Left(erro) => Results.InternalServerError(erro)
////      case Right(modelo) => modeloParaResult(modelo)
////    }
////  }
////
////  def modeloParaResult(modelo:M)(implicit lang:Lang): Result={
////    Results.Ok( Json.obj(this.modelName ->  Json.toJson(modelo)) )
////  }
//
//}
