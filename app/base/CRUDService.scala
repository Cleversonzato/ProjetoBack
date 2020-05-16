package base

import mongoDB.MongoIndexes
import play.api.Logging
import play.api.libs.json.{JsObject, JsValue, Json, OFormat}
import play.api.Logging
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.play.json._
import collection._
import play.api.libs.json.Json.JsValueWrapper
import reactivemongo.api.{Cursor, ReadPreference}
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import reactivemongo.core.errors.DatabaseException

import scala.collection.Factory
import scala.concurrent.{ExecutionContext, Future}

abstract class CRUDService[M <: Model]( implicit ec: ExecutionContext,reactiveMongoApi: ReactiveMongoApi,mongoDef: MongoIndexes) extends Logging {

  import reactivemongo.play.json._
  implicit def format: OFormat[M]
  implicit def f: Factory[M,List[M]] = List

  def collection: Future[JSONCollection]

  def listar(query: JsObject, ordenacao: JsObject, maxDocs:Int = -1):Future[Either[JsValue, List[M] ]]= {
    this.collection.flatMap {
      _.find(query, Option.empty[JsObject])
        .sort(ordenacao)
        .cursor[M](ReadPreference.primary)
        .collect[List](maxDocs, Cursor.FailOnError[List[M]]())
        .map {Right(_)}
    } recover {
      case e => logger.error("erro ao buscar " + query + ". Erro: " + e)
        Left(JsonUtil.jsonErro( "Não foi possível acessar seus dados. Contate o administrador para maiores informações." ) )
    }
  }

  def buscar(query: JsObject): Future[Either[JsValue, M]] = {
    this.collection.flatMap {
      _.find(query, Option.empty[JsObject]).one[M]
    }.map {
      case Some(classe) => Right(classe)
      case None => Left( JsonUtil.jsonErro("Dados não encontrados: " + query ) )
    }.recover {
      case e => logger.error("erro ao buscar " + query + ". Erro: " + e)
        Left( JsonUtil.jsonErro("Não foi possível acessar seus dados. Contate o administrador para maiores informações. " + query) )
    }
  }

  def criar(model:M):Future[Either[JsObject, M ]] = {
    this.collection.flatMap(_.insert.one(model)).map{writeResult =>
      logger.info("Criar: " + writeResult)
      Right(model)
    }.recover {
      case e: DatabaseException => {
        if (e.code.get == 11000) {
          logger.warn("criar: " + e)
          Left(JsonUtil.jsonErro("Falha ao criar! Este item já está cadastrado!"))
        } else {
          logger.error("criar: " + e)
          Left(JsonUtil.jsonErro("Não foi possível criar este item!" + e.getMessage()) )
        }
      }case ex =>
        logger.error("Criar: " + ex)
        Left( JsonUtil.jsonErro(  Json.obj( "MongoDb" -> ex.toString, "loc"-> ex.getLocalizedMessage, "msg" -> ex.getMessage, "str"->ex.toString  )) )
    }
  }

  def atualizar (model:M):Future[Either[JsObject, M ]] ={
    this.collection.flatMap(_.update.one(BSONDocument("_id"-> model._id), model) )
      .map { writeResult => {
        if (writeResult.n == 0) {
          logger.warn("Nenhum documento foi atualizado. A chave de busca: " + model._id + " está correta?")
          Left(JsonUtil.jsonErro("Erro na atualização! Tente novamente e, o erro persistir, contate o administrador."))
        } else {
          logger.info("atualizar: id = " + model._id + " " + writeResult)
          Right(model)
        }
      }
      }.recover{
      case ex=> logger.error("erro ao atualizar o objeto com  id: " +  model._id + ". Erro: "+ ex)
        Left( JsonUtil.jsonErro(  Json.obj( "MongoDb" -> ex.toString, "loc"-> ex.getLocalizedMessage, "msg" -> ex.getMessage, "str"->ex.toString  )) )
    }
  }

  def excluir(_id:BSONObjectID):Future[Either[JsObject, JsObject ]] ={
    this.collection.flatMap(_.delete.one(BSONDocument("_id"-> _id) ) )
      .map { writeResult =>
        logger.info("excluir: id = " + _id +" " + writeResult)
        Right(JsonUtil.jsonSucesso("Item removido", "Excluir") )
      }.recover{
      case ex=> logger.error("erro ao excluir o objeto com  id: " +  _id + ". Erro: "+ ex)
        Left( JsonUtil.jsonErro(  Json.obj( "MongoDb" -> ex.toString, "loc"-> ex.getLocalizedMessage, "msg" -> ex.getMessage, "str"->ex.toString  )) )
    }
  }

}
