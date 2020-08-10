package base.mensagens;



sealed trait TipoMensagem{
        def nome:String
        def raiz: String
}

case class Sucesso(nome:String="sucesso", raiz:String="Sucesso") extends TipoMensagem
case class Erro(nome:String="erro", raiz:String="Erros") extends TipoMensagem