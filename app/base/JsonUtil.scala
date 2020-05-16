package base

import play.api.libs.json._

object JsonUtil {

  def jsonErro(mensagem:String, key:String="erro"): JsObject ={
    Json.obj("erros" ->  JsArray(Seq(Json.obj(key -> mensagem))) )
  }

  def jsonErro(jErro: JsObject): JsObject ={
    Json.obj("erros" ->  JsArray(Seq(jErro)) )
  }

  def jsonAddErro(jsonErro:JsObject, mensagem:String, key:String="erro"): JsObject = {
    Json.obj("erros" -> ( (jsonErro \ "erros" ).as[JsArray] :+ Json.obj(key -> mensagem) )
    )
  }

  def jsonAddErro(jsonErro:JsObject,jNovoErro: JsObject): JsObject = {
    Json.obj("erros" -> ( (jsonErro \ "erros" ).as[JsArray] :+ jNovoErro )
    )
  }

  def jsonSucesso(mensagem:String, key:String): JsObject ={
    Json.obj("sucesso" ->  JsArray(Seq(Json.obj(key -> mensagem))) )
  }

}
