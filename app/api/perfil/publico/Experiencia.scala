package api.perfil.publico

import java.time.LocalDate

case class Experiencia(
                        nome:String,
                        inicio: LocalDate,
                        fim: Option[LocalDate],
                        local:String,
                        funcao:String,
                        observacoes:Option[String]
                      )
