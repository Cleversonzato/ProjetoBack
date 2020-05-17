package api.processo.inscricao

import base.CRUDService
import javax.inject.Inject
import mongoDB.MongoIndexes
import play.api.libs.json.{Json, OFormat}
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.{ExecutionContext, Future}

class InscricaoProcessoService @Inject()(implicit ec: ExecutionContext, reactiveMongoApi: ReactiveMongoApi, mongoDef: MongoIndexes ) extends CRUDService[InscricaoProcesso] {

  import reactivemongo.play.json._
  import reactivemongo.play.json.BSONFormats
  implicit def format: OFormat[InscricaoProcesso] = Json.format[InscricaoProcesso];

  override def collection: Future[JSONCollection] = mongoDef.inscricaoProcessoCollection

}
