package api.usuario

import base.CRUDService
import javax.inject.{Inject, Singleton}
import mongoDB.MongoIndexes
import play.api.i18n.MessagesApi
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.play.json._
import play.api.libs.json.{JsPath, JsValue, Json, OFormat, OWrites, Reads}
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UsuarioService @Inject() (implicit ec: ExecutionContext,  reactiveMongoApi: ReactiveMongoApi, mongoDef: MongoIndexes, messagesApi: MessagesApi ) extends CRUDService[Usuario] {

  import reactivemongo.play.json._
  import reactivemongo.play.json.BSONFormats
  implicit def format: OFormat[Usuario] = Json.format[Usuario]

  override def collection: Future[JSONCollection] = mongoDef.usuarioCollection


}
