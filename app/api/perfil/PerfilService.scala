package api.perfil

import api.perfil.papel.{Administrador, Avaliador, Cliente, Papel, Tipo}
import base.CRUDService
import javax.inject.Inject
import mongoDB.MongoIndexes
import play.api.i18n.MessagesApi
import play.api.libs.json.{Json, OFormat}
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.{ExecutionContext, Future}

class PerfilService @Inject() (implicit ec: ExecutionContext,  reactiveMongoApi: ReactiveMongoApi, mongoDef: MongoIndexes, messagesApi: MessagesApi ) extends CRUDService[Perfil] {

  import reactivemongo.play.json._
  import reactivemongo.play.json.BSONFormats
  import reactivemongo.play.json.BSONFormats
  implicit def formatAdministrador: OFormat[Administrador] = Json.format[Administrador]
  implicit def formatAvaliador: OFormat[Avaliador] = Json.format[Avaliador]
  implicit def formatCliente: OFormat[Cliente] = Json.format[Cliente]
  implicit def formatTipoPapel: OFormat[Tipo] = Json.format[Tipo]
  implicit def formatPapel: OFormat[Papel] = Json.format[Papel]
  implicit def format: OFormat[Perfil] = Json.format[Perfil]

  override def collection: Future[JSONCollection] = mongoDef.perfilCollection

}
