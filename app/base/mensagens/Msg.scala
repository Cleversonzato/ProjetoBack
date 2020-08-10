package base.mensagens

import api.perfil.papel.Tipo
import play.api.libs.json._

trait Msg[A] {
  def apply(tipo: TipoMensagem, a: A): JsObject
}

// Definição do objeto

object JsonMsg {

  def apply[A](tipo: TipoMensagem, a: A)(implicit msg: Msg[A]) = msg(tipo, a) //nota para o futuro, este appĺy NÂO sobrecarrega o do "trait" e

  // Funcões para não repetir código

  def instanciaJsArray[A](transformador: A => JsArray): Msg[A] = {
    new Msg[A] {
      override def apply(tipo: TipoMensagem, a: A): JsObject = {
        Json.obj(tipo.raiz -> transformador(a))
      }
    }
  }

  def instanciaString[A](transformador: A => String): Msg[A] = {
    new Msg[A] {
      override def apply(tipo: TipoMensagem, a: A): JsObject = {
        Json.obj(tipo.raiz -> JsArray(Seq( Json.obj(tipo.nome -> transformador(a)) )))
      }
    }
  }

  // cada caso

  implicit val deMsg: Msg[String] = {
    instanciaString(string => string)
  }

  implicit val deChaveMsg: Msg[(String, String)] = {
    instanciaJsArray(tupla => JsArray(Seq(Json.obj(tupla._1 -> tupla._2))))
  }

  implicit val deJsObject: Msg[JsObject] = {
    instanciaJsArray(json => JsArray(Seq(json)))
  }

  implicit val deSeqErroMsg: Msg[(Option[JsObject], String)] = {
    new Msg[(Option[JsObject], String)] {
      override def apply(tipo: TipoMensagem, a: (Option[JsObject], String)): JsObject = {
        Json.obj(tipo.raiz ->{
          a._1 match {
            case Some(value) => (value \ tipo.raiz).as[JsArray] :+ Json.obj(tipo.nome -> a._2)
            case None => JsArray(Seq(Json.obj(tipo.nome -> a._2)))
          }
        })
      }
    }
  }

  implicit val deSeqChaveMsg: Msg[(Option[JsObject], String, String)] = {
    instanciaJsArray(
      tupla =>
        tupla._1 match {
          case Some(value) => (value \ "erros").as[JsArray] :+ Json.obj(tupla._2 -> tupla._3)
          case None => JsArray(Seq(Json.obj(tupla._2 -> tupla._3)))
        }
    )
  }

  implicit val deJsError: Msg[JsError] = {
    instanciaJsArray(
      erro => {
        JsArray(Seq(Json.obj(
          "erro" -> erro.errors.map(e => {
            s"${e._1}: ${e._2.reduce((a, b) => JsonValidationError(a.messages.toString() + b.messages.toString())).messages.reduce(_ + _)}"
          }).mkString("; ")
        )))
      }
    )
  }

}


//  //Sucesso (único)
//
//  def sucesso(mensagem:String, key:String): JsObject =
//    Json.obj("sucesso" ->  JsArray(Seq(Json.obj(key -> mensagem))) )
//
//  // Erros
//
//  def erro[A](implicit jsErro: JsonMsg[A]): JsonMsg[A] = jsErro
//
//  // "construtor"
//

//
//  // cada caso
//
//  implicit val deMsg: JsonMsg[String] =
//    encapsulaErros(string => JsArray(Seq(Json.obj("erro" -> string))))
//
//  implicit val deChaveMsg: JsonMsg[(String, String)] =
//    encapsulaErros(tupla => JsArray(Seq(Json.obj(tupla._1 -> tupla._2))))
//
//  implicit val deJsObject: JsonMsg[JsObject] =
//    encapsulaErros(json => JsArray(Seq(json)))
//
//  implicit val deSeqErroMsg: JsonMsg[(Option[JsObject], String)] = {
//    encapuslaErros(
//      tupla =>
//        tupla._1 match {
//          case Some(value) => (value \ "erros").as[JsArray] :+ Json.obj("erro" -> tupla._2)
//          case None => JsArray(Seq(Json.obj("erro" -> tupla._2)))
//        }
//      )
//    }
//
//    implicit val deSeqErroChaveMsg: JsonMsg[(Option[JsObject], String, String)] = {
//      encapuslaErros(
//        tupla =>
//          tupla._1 match {
//            case Some(value) => (value \ "erros").as[JsArray] :+ Json.obj(tupla._2 -> tupla._3)
//            case None => JsArray(Seq(Json.obj(tupla._2 -> tupla._3)))
//          }
//      )
//    }
//
//
//
//
//  def jsonAddErro(jsonErro:JsObject, key:String="erro", mensagem:String): JsObject = {
//    if(jsonErro != null){
//      Json.obj("erros" -> ( (jsonErro \ "erros" ).as[JsArray] :+ Json.obj(key -> mensagem) ))
//    }else{
//      this.jsonErro(mensagem, key)
//    }
//  }
//
//  def jsonAddErro(jsonErro:JsObject,jNovoErro: JsObject): JsObject = {
//    if(jsonErro != null){
//      Json.obj("erros" -> ( (jsonErro \ "erros" ).as[JsArray] :+ jNovoErro ))
//    }else{
//      this.jsonErro(jNovoErro)
//    }
//  }
//
//  def jsonSucesso(mensagem:String, key:String): JsObject ={
//    Json.obj("sucesso" ->  JsArray(Seq(Json.obj(key -> mensagem))) )
//  }
//
//  def jsonJsError(erro:JsError): JsObject = {
//    Json.obj(
//      "erro" -> erro.errors.map( e =>{
//        s"${e._1}: ${e._2.reduce( (a, b)=> JsonValidationError(a.messages.toString() + b.messages.toString())  ).messages.reduce(_+_ )}"
//      }).mkString("; ")
//    )
//  }
//
//}
