package base

import mongoDB.MongoIndexes
import play.api.libs.json.{JsObject, JsValue, Json, OFormat}
import play.api.Logging
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.play.json._
import collection._
import javax.inject.Inject
import reactivemongo.api.{Cursor, ReadPreference}
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import reactivemongo.core.errors.DatabaseException
import play.api.i18n._

import scala.collection.Factory
import scala.concurrent.{ExecutionContext, Future}

abstract class CRUDService[M <: Model] @Inject()(implicit ec: ExecutionContext, reactiveMongoApi: ReactiveMongoApi, mongoDef: MongoIndexes, messagesApi: MessagesApi) extends Logging with JsonUtil  {

  import reactivemongo.play.json._
  implicit def format: OFormat[M]
  implicit def f: Factory[M,List[M]] = List

  def collection: Future[JSONCollection]

  def listar(query: JsObject, ordenacao: JsObject, maxDocs:Int = -1)(lang:Lang):Future[Either[JsValue, List[M] ]]= {
    this.collection.flatMap {
      _.find(query, Option.empty[JsObject])
        .sort(ordenacao)
        .cursor[M](ReadPreference.primary)
        .collect[List](maxDocs, Cursor.FailOnError[List[M]]())
        .map {Right(_)}
    } recover {
      case e => logger.error("erro ao buscar " + query + ". Erro: " + e)
        Left(jsonErro(  messagesApi("erro.service.listar")(lang) ))
    }
  }

  def buscar(query: JsObject)(lang:Lang): Future[Either[JsValue, M]] = {
    this.collection.flatMap {
      _.find(query, Option.empty[JsObject]).one[M]
    }.map {
      case Some(classe) => Right(classe)
      case None => Left( jsonErro( messagesApi("erro.service.buscar.vazio")(lang)  + query ))
    }.recover {
      case e => logger.error("erro ao buscar " + query + ". Erro: " + e)
        Left( jsonErro( messagesApi("erro.service.buscar")(lang) + query ))
    }
  }

  def criar(model:M)(lang: Lang):Future[Either[JsObject, M ]] = {
    this.collection.flatMap(_.insert.one(model)).map{writeResult =>
      logger.info("Criar: " + writeResult)
      Right(model)
    }.recover {
      case e: DatabaseException => {
        if (e.code.get == 11000) {
          logger.warn("criar: " + e)
          Left(jsonErro( messagesApi(this.erroCriarDuplicado)(lang) ))
        } else {
          logger.error("criar: " + e)
          Left(jsonErro( messagesApi("erro.service.criar")(lang) + e.getMessage()) )
        }
      }case ex =>
        logger.error("Criar: " + ex)
        Left( jsonErro(  Json.obj( "MongoDb" -> ex.toString, "loc"-> ex.getLocalizedMessage, "msg" -> ex.getMessage, "str"->ex.toString  )) )
    }
  }

  def editar (model:M)(lang:Lang):Future[Either[JsObject, M ]] ={
    this.collection.flatMap(_.update.one(BSONDocument("_id"-> model.id), model) )
      .map { writeResult => {
        if (writeResult.n == 0) {
          logger.warn("Nenhum documento foi atualizado. A chave de busca: " + model.id + " está correta?")
          Left(jsonErro( messagesApi("erro.service.editar")(lang) ))
        } else {
          logger.info("atualizar: id = " + model.id + " " + writeResult)
          Right(model)
        }
      }
      }.recover{
      case ex=> logger.error("erro ao atualizar o objeto com  id: " +  model.id + ". Erro: "+ ex)
        Left( jsonErro(  Json.obj( "MongoDb" -> ex.toString, "loc"-> ex.getLocalizedMessage, "msg" -> ex.getMessage, "str"->ex.toString  )) )
    }
  }

  def remover(_id:BSONObjectID)(lang:Lang):Future[Either[JsObject, JsObject ]] ={
    this.collection.flatMap(_.delete.one(BSONDocument("_id"-> _id) ) )
      .map { writeResult =>
        logger.info("excluir: id = " + _id +" " + writeResult)
        Right(jsonSucesso( messagesApi("sucesso.service.remover")(lang), "Excluir" ))
      }.recover{
      case ex=> logger.error("erro ao excluir o objeto com  id: " +  _id + ". Erro: "+ ex)
        Left( jsonErro(  Json.obj( "MongoDb" -> ex.toString, "loc"-> ex.getLocalizedMessage, "msg" -> ex.getMessage, "str"->ex.toString  )) )
    }
  }

  /*
  *Código das mensagens personalizáveis
  */
  val erroCriarDuplicado = "erro.service.criar.duplicado"

}
