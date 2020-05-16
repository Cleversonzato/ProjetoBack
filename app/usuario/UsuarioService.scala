package usuario

import base.CRUDService
import javax.inject.{Inject, Singleton}
import mongoDB.MongoIndexes
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.play.json._
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UsuarioService @Inject() (implicit ec: ExecutionContext,  reactiveMongoApi: ReactiveMongoApi, mongoDef: MongoIndexes ) extends CRUDService[Usuario] {

  import play.api.libs.json._
  implicit def format: OFormat[Usuario] = Json.format[Usuario];

  override def collection: Future[JSONCollection] = mongoDef.usuarioCollection


}
