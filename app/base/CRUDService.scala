package base

import mongoDB.MongoIndexes
import play.api.Logging
import play.api.libs.json.{JsObject, Json, OFormat}
import play.api.Logging
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.play.json._
import collection._

import scala.concurrent.{ExecutionContext, Future}

abstract class CRUDService[M <: Model]( implicit ec: ExecutionContext,reactiveMongoApi: ReactiveMongoApi,mongoDef: MongoIndexes) extends Logging {

  import reactivemongo.play.json._
  implicit def format: OFormat[M]

  def collection: Future[JSONCollection]

  def buscar(query: JsObject): Future[Either[JsObject, M]] = {
    this.collection.flatMap {
      _.find(query, Option.empty[JsObject]).one[M]
    }.map {
      case Some(classe) => Right(classe)
      case None => Left( JsonUtil.jsonErro("Dados não encontrados " + query.keys ) )
    }.recover {
      case e => logger.error("erro ao buscar " + query + ". Erro: " + e)
        Left( JsonUtil.jsonErro("Não foi possível acessar seus dados. Contate o administrador para maiores informações. " + query) )
    }
  }

}
