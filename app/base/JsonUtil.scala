package base

import play.api.libs.json._

trait JsonUtil {

  def jsonErro(mensagem:String, key:String="erro"): JsObject ={
    Json.obj("erros" ->  JsArray(Seq(Json.obj(key -> mensagem))) )
  }

  def jsonErro(jErro: JsObject): JsObject ={
    Json.obj("erros" ->  JsArray(Seq(jErro)) )
  }

  def jsonAddErro(jsonErro:JsObject, key:String="erro", mensagem:String): JsObject = {
    if(jsonErro != null){
      Json.obj("erros" -> ( (jsonErro \ "erros" ).as[JsArray] :+ Json.obj(key -> mensagem) ))
    }else{
      this.jsonErro(mensagem, key)
    }
  }

  def jsonAddErro(jsonErro:JsObject,jNovoErro: JsObject): JsObject = {
    if(jsonErro != null){
      Json.obj("erros" -> ( (jsonErro \ "erros" ).as[JsArray] :+ jNovoErro ))
    }else{
      this.jsonErro(jNovoErro)
    }
  }

  def jsonSucesso(mensagem:String, key:String): JsObject ={
    Json.obj("sucesso" ->  JsArray(Seq(Json.obj(key -> mensagem))) )
  }

  def jsonJsError(erro:JsError): JsObject = {
    Json.obj(
      "erro" -> erro.errors.map( e =>{
        s"${e._1}: ${e._2.reduce( (a, b)=> JsonValidationError(a.messages.toString() + b.messages.toString())  ).messages.reduce(_+_ )}"
      }).mkString("; ")
    )
  }

}
