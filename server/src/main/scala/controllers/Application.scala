package controllers

import java.nio.ByteBuffer
import javax.inject.Inject
import play.api.{Configuration, Environment}
import play.api.mvc._
import services.ApiService
import spatutorial.shared.Api
import postg.CUsuarios
import scala.concurrent.ExecutionContext.Implicits.global
import boopickle.Default._

object Router extends autowire.Server[ByteBuffer, Pickler, Pickler] {
  override def read[R: Pickler](p: ByteBuffer) = Unpickle[R].fromBytes(p)
  override def write[R: Pickler](r: R) = Pickle.intoBytes(r)
}

class Application @Inject() ( val components: ControllerComponents,
                              implicit val config: Configuration,
                              val env: Environment,
                              protected val apiService: ApiService,
                              protected val usuarios: CUsuarios
                            ) extends AbstractController(components) {


  def index = Action(parse.json) { Ok(views.html.index("Supplier")) }

  /*def logear = Action { implicit request =>
    if (request.remoteAddress == "127.0.0.1" || request.remoteAddress == "10.51.253.48" )
      Ok(views.html.index("Sicap"))
    else
      Ok(views.html.logear("Logear"))
  }*/

  def validate = Action.async { implicit request =>

    val user = request.body.asFormUrlEncoded.get("username")(0)
    val password = request.body.asFormUrlEncoded.get("password")(0)

    val result = usuarios.ById(user)

    result.map( u =>
      if (u.head.contraseña == password) {
        Ok( views.html.index("Zyckap")) withSession("sess-zyckap" -> "true")
      } else {
        //Ok( views.html.index("Zyckap")) withSession("sess-zyckap" -> "false")
        Ok(views.html.logear("Datos no validos"))
      }
    )

  }

  def autowireApi(path: String) = Action.async(parse.raw) {
    implicit request =>

      // get the request body as ByteString
      val b = request.body.asBytes(parse.UNLIMITED).get

      // call Autowire route
      Router.route[Api](apiService)(
        autowire.Core.Request(path.split("/"), Unpickle[Map[String, ByteBuffer]].fromBytes(b.asByteBuffer))
      ).map(buffer => {
        val data = Array.ofDim[Byte](buffer.remaining())
        buffer.get(data)
        Ok(data)
      })
  }

  def logging = Action(parse.anyContent) {
    implicit request =>
      request.body.asJson.foreach { msg =>
        println(s"CLIENT - $msg")
      }
      Ok("")
  }

}


