package api.perfil.publico

import base.CRUDService
import javax.inject.Inject
import mongoDB.MongoIndexes
import play.api.i18n.MessagesApi
import play.api.libs.json.{Json, OFormat}
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.{ExecutionContext, Future}

class PerfilPublicoService @Inject()(implicit ec: ExecutionContext, reactiveMongoApi: ReactiveMongoApi, mongoDef: MongoIndexes, messagesApi: MessagesApi ) extends CRUDService[PerfilPublico] {
  import reactivemongo.play.json._
  import reactivemongo.play.json.BSONFormats
  implicit def formatFormacao: OFormat[Formacao] = Json.format[Formacao]
  implicit def formatExperiencia: OFormat[Experiencia] = Json.format[Experiencia]
  implicit def format: OFormat[PerfilPublico] = Json.format[PerfilPublico]

  override def collection: Future[JSONCollection] = mongoDef.perfilPublicoCollection

}
