package usuario

import java.time.LocalDateTime
import org.mindrot.jbcrypt.BCrypt
import base.Model
import reactivemongo.bson.BSONObjectID

case class Usuario(override val _id: BSONObjectID,
                   idPerfil: BSONObjectID,
                   email: String,
                   senha:String,
                   dtCriacao:Option[LocalDateTime]
                  )
  extends Model
{

  //para novos usuários
  def this(email: String, senha:String, dtCriacao:Option[LocalDateTime]) = {
    this(BSONObjectID.generate, BSONObjectID.generate, email,  BCrypt.hashpw(senha, BCrypt.gensalt()), dtCriacao )
  }

  def verificaSenha(senha:String):Boolean= {
    BCrypt.checkpw(senha, this.senha)
  }
}