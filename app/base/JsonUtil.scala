package base

import play.api.libs.json.{JsObject, Json}

object JsonUtil {


  def jsonErro(mensagem:String): JsObject ={
    Json.obj("erro" -> mensagem )
  }

}
