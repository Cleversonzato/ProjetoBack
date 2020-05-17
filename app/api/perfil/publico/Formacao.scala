package api.perfil.publico

import java.time.LocalDate

case class Formacao (
                      nome:String,
                      inicio: LocalDate,
                      fim: Option[LocalDate],
                      titulo:String,
                      local:String,
                      observacoes:Option[String]
                    )