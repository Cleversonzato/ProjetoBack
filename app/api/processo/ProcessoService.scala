package api.processo

import base.CRUDService
import javax.inject.Inject
import mongoDB.MongoIndexes
import play.api.i18n.MessagesApi
import play.api.libs.json.{Json, OFormat}
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.{ExecutionContext, Future}

class ProcessoService @Inject()(implicit ec: ExecutionContext, reactiveMongoApi: ReactiveMongoApi, mongoDef: MongoIndexes, messagesApi: MessagesApi ) extends CRUDService[Processo] {
  import reactivemongo.play.json._
  import reactivemongo.play.json.BSONFormats
  implicit def format: OFormat[Processo] = Json.format[Processo]
  override def collection: Future[JSONCollection] = mongoDef.processoCollection

}
