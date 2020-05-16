package mongoDB

import javax.inject.{Inject, Singleton}
import play.api.Logging
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.indexes.{Index, IndexType}
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class MongoIndexes @Inject()(
                              implicit ec: ExecutionContext,
                              reactiveMongoApi: ReactiveMongoApi
                            )
extends Logging
{
  val db = reactiveMongoApi.database;
  val usuarioCollection:Future[JSONCollection]  =  db.map(_.collection[JSONCollection]("usuario"))
  val perfilCollection:Future[JSONCollection]   =  db.map(_.collection[JSONCollection]("perfil"))
  val perfilPublicoCollection:Future[JSONCollection]  =  db.map(_.collection[JSONCollection]("perfilPublico"))
  val processoCollection:Future[JSONCollection]   =  db.map(_.collection[JSONCollection]("processo"))
  val faseCollection:Future[JSONCollection]   =  db.map(_.collection[JSONCollection]("fase"))
  val avaliacaoCollection:Future[JSONCollection]  =  db.map(_.collection[JSONCollection]("avaliacao"))
  val inscricaoProcessoCollection:Future[JSONCollection]  =  db.map(_.collection[JSONCollection]("inscricaoProcesso"))

  usuarioCollection.map(_.indexesManager.ensure(
    Index(Seq("email" -> IndexType.Ascending), Option("indexUsuario"), unique = true)
  ).andThen {
    case Success(s) =>  logger.info("usuarioIndex:" + s)
    case Failure(e) =>  logger.error("usuarioIndex:" + e)
  })

  perfilPublicoCollection.map(_.indexesManager.ensure(
    Index(Seq("idPerfil" -> IndexType.Ascending), Option("indexperfilPublico"))
  ).andThen {
    case Success(s) =>  logger.info("perfilPublicoIndex:" + s)
    case Failure(e) =>  logger.error("pperfilPublicoIndex:" + e)
  })

  processoCollection.map(_.indexesManager.ensure(
    Index(Seq("idCriador" -> IndexType.Ascending), Option("indexProcesso"))
  ).andThen {
    case Success(s) =>  logger.info("processoIndex:" + s)
    case Failure(e) =>  logger.error("processoIndex:" + e)
  })

  inscricaoProcessoCollection.map(_.indexesManager.ensure(
    Index(Seq("idProcesso" -> IndexType.Ascending, "idPerfil"-> IndexType.Ascending), Option("indexInscricaoProcesso"), unique = true)
  ).andThen {
    case Success(s) =>  logger.info("InscricaoProcessoIndex:" + s)
    case Failure(e) =>  logger.error("InscricaoProcessoIndex:" + e)
  })

}
