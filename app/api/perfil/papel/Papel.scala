package api.perfil.papel

case class Papel(
    tipo:Tipo
)

sealed trait Tipo{
  def nome:String
}
case class Administrador(nome:String="Administrador") extends Tipo
case class Cliente(nome:String="Cliente") extends Tipo
case class Avaliador(nome:String="Avaliador") extends Tipo