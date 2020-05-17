package api.processo

import java.time.{LocalDate, LocalDateTime}

import base.Model
import reactivemongo.bson.BSONObjectID

case class Processo(
                    override val _id: BSONObjectID,
                    titulo: String,
                    descricao:Option[String],
                    criacao: LocalDateTime,
                    inicio: Option[LocalDate],
                    termino: Option[LocalDateTime],
                    idProprietarios:List[BSONObjectID],

                    //info inscricao
                    anuncio:Option[String],
                    descricaoCompleta:Option[String],
                    //             inscricoes:List[Inscricao]

                    //resultado:Option[Resultado]

                    //   inscricoes:List[Incricao]
                    //
                    //infoInscricao
                    //  inscricaoPublica: Boolean,
                    //  senhaInscricao:String

                    //fases List[Fases],
                    //resultado:Resultado

                   ) extends Model
