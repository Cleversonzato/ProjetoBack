package api.usuario

import java.time.LocalDateTime
import org.mindrot.jbcrypt.BCrypt
import base.Model
import reactivemongo.bson.BSONObjectID

case class Usuario(
                    override val id: Option[BSONObjectID],
                    idPerfil: Option[BSONObjectID],
                    email: String,
                    senha: String,
                    dtCriacao: Option[LocalDateTime]
                  )
  extends Model
{

  //para novos usuários
  def this(email: String, senha:String) = {
    this(Option(BSONObjectID.generate), Option(BSONObjectID.generate), email,  BCrypt.hashpw(senha, BCrypt.gensalt()), Option(LocalDateTime.now) )
  }

  def verificaSenha(senha:String):Boolean= {
    BCrypt.checkpw(senha, this.senha)
  }
}