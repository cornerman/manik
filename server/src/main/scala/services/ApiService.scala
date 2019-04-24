package services

import scala.language.postfixOps
import spatutorial.shared._
import javax.inject.Inject
import play.api.libs.ws._
import postg.CUsuarios


///Para el web service
import akka.actor.ActorSystem
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import scala.concurrent.Future

//import akka.stream.{Materializer}

/*import HttpMethods._
import akka.actor
import akka.http.scaladsl.unmarshalling.Unmarshal
import play.api.libs.json.Json
import play.api.libs.json._
import play.api.libs.functional.syntax._
import services.JsonEitherSpec.Data.FailureJson
import spray.json._
import services.JsonSupport
import akka.http.scaladsl.Http
import scala.concurrent.ExecutionContext.Implicits.global
import MediaTypes._
import spray.json._
import services.jsontypes.JsonTypes._
import services.JsonEitherSpec.Data._
*/

class ApiService @Inject()( protected val ws: WSClient) extends Api with JsonSupport {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  private val url = "http://localhost:8080/"

  /************************* Usuarios   **********************************************/
  override def getUsuario(user: String): Future[Seq[Usuarios]] = {
    val usuario = new CUsuarios()
    usuario.ById(user)
  }
  override def logear(item: Usuarios): Future[(Usuarios, String)] = {
    val usuario = new CUsuarios()
    usuario.logear(item)
  }
  /**################################################################################################*/

}