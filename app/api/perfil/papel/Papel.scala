package api.perfil.papel

case class Papel(
    tipo:Tipo
)

sealed trait Tipo{
  def nome:String
  def codigo: Int
}
case class Administrador(nome:String="Administrador", codigo:Int=0) extends Tipo
case class Cliente(nome:String="Cliente",codigo:Int=1) extends Tipo
case class Avaliador(nome:String="Avaliador", codigo:Int=2) extends Tipo