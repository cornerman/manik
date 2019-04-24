package services
import akka.actor.{ Actor, ActorLogging }
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.{ ActorMaterializer, ActorMaterializerSettings }
import akka.util.ByteString

class ActorProveedor extends Actor
  with ActorLogging {

  import akka.pattern.pipe
  import context.dispatcher
  import HttpMethods._

  final implicit val materializer: ActorMaterializer = ActorMaterializer(ActorMaterializerSettings(context.system))

  val http = Http(context.system)

  override def preStart() = {
    //val responseFuture: Future [ HttpResponse ] = Http( ).singleRequest ( HttpRequest ( uri = url + "proveedores/searchID" ) )
    val url = "http://localhost:8080/"
    println("Haciendo la peticion de proveedores")
    http.singleRequest(HttpRequest( POST, uri = url + "proveedores/searchID")).pipeTo(self)

  }

  def receive = {
    case HttpResponse(StatusCodes.OK, headers, entity, _) =>
      println("Recibiendo la información  /*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*")
      entity.dataBytes.runFold(ByteString(""))(_ ++ _).foreach { body =>
        log.info("Got response, body: " + body.utf8String)
      }
    case resp @ HttpResponse(code, _, _, _) =>
      println("Fallo al recuperar la información  /*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*")
      log.info("Request failed, response code: " + code)
      resp.discardEntityBytes()
  }

}