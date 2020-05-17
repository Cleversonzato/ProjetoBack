package api.perfil

import base.CRUDService
import javax.inject.Inject
import mongoDB.MongoIndexes
import play.api.libs.json.{Json, OFormat}
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.{ExecutionContext, Future}

class PerfilService @Inject() (implicit ec: ExecutionContext,  reactiveMongoApi: ReactiveMongoApi, mongoDef: MongoIndexes ) extends CRUDService[Perfil] {

  import reactivemongo.play.json._
  import reactivemongo.play.json.BSONFormats
  implicit def formatPapel: OFormat[PapelPerfil] = Json.format[PapelPerfil];
  implicit def format: OFormat[Perfil] = Json.format[Perfil];

  override def collection: Future[JSONCollection] = mongoDef.perfilCollection

}
