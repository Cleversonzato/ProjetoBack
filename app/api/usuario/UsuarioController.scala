package api.usuario

import api.perfil.papel.{Administrador, Avaliador, Cliente, Papel, Tipo}
import api.perfil.{Perfil, PerfilController}
import base.{CRUDController, JsonUtil}
import javax.inject.{Inject, Singleton}
import play.api.i18n.Lang
import play.api.libs.json.{JsError, JsObject, JsPath, JsResult, JsSuccess, JsValue, Json, OFormat, OWrites, Reads}
import play.api.mvc.{AnyContent, ControllerComponents, Request, Result}

import scala.concurrent.impl.Promise
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UsuarioController  @Inject()(implicit ec: ExecutionContext, cc: ControllerComponents, service:UsuarioService, perfilController: PerfilController) extends CRUDController[Usuario]{

  import reactivemongo.play.json._
  import reactivemongo.play.json.BSONFormats
  implicit def format: OFormat[Usuario] = Json.format[Usuario]
  implicit def formatPerfil: OFormat[Perfil] = Json.format[Perfil]
  implicit def formatTipoPapel: OFormat[Tipo] = Json.format[Tipo]
  implicit def formatPapel: OFormat[Papel] = Json.format[Papel]
  implicit def formatAdministrador: OFormat[Administrador] = Json.format[Administrador]
  implicit def formatAvaliador: OFormat[Avaliador] = Json.format[Avaliador]
  implicit def formatCliente: OFormat[Cliente] = Json.format[Cliente]

  override def modelName: String = "usuario"

  override def criacaoEResultado(usuario: Usuario)(implicit lang: Lang): Future[Result] = {
    criarModelo(usuario).flatMap(_ match {
      case Left(erro) => Future(InternalServerError(erro))
      case Right(usuario) =>
        perfilController.criarModelo( Perfil(usuario.idPerfil, List(Papel(Cliente())), None, None, None) )
        .map( perfilController.retornoParaResult(_) )
    })

  }

}