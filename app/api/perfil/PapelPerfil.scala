package api.perfil

case class PapelPerfil(nome:String, sigla:String, codigo: Int){
  def this(papel:Papel) = {
    this(papel.nome, papel.sigla, papel.codigo)
  }

  def this(codigo:String) = {
    this(codigo match {
      case Administrador.sigla => Administrador
      case Cliente.sigla => Cliente
      case Avaliador.sigla => Avaliador
    })
  }

  def getPapeis = Set(Administrador, Avaliador, Cliente)

  def getUsuarios = {
    getPapeis.filter(_.codigo > 0)
  }

}

/*
    Definição de cada papel
   */

sealed abstract trait Papel{
  def nome:String
  def sigla:String
  def codigo: Int
}

//códigos 0 e negativos
case object Administrador extends Papel {
  override val nome: String = "Administrador"
  override val sigla: String = "ADMIN"
  override val codigo: Int = -1
}

//códigos de 1 a 999
case object Avaliador extends Papel {
  override val nome: String = "Avaliador"
  override val sigla: String = "AVAL"
  override val codigo: Int = 1
}

//códigos acima de 1000
case object Cliente extends Papel {
  override val nome: String = "Cliente"
  override val sigla: String = "CLI"
  override val codigo: Int = 1000
}