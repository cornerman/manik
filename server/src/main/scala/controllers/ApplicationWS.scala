package controllers

import spatutorial.shared.WebSocketEvent._
import com.google.inject.Inject
import play.api.{Configuration, Environment}
import play.api.mvc._
import akka.stream.ActorMaterializer
import akka.actor.ActorSystem
import boopickle.Default._
import akka.stream.scaladsl.Flow

import scala.util.{Failure, Success}
import akka.util.ByteString
import play.api.http.websocket.{BinaryMessage, CloseCodes, CloseMessage, Message}
import play.api.libs.streams.{ActorFlow, AkkaStreams}
import play.api.mvc.WebSocket.MessageFlowTransformer
import akka.actor._
import actorschat._

class ApplicationWS @Inject() ( implicit val config: Configuration, val env: Environment,
                                implicit val system: ActorSystem, materializer: ActorMaterializer) {

  /**************************************   WebSocket Messages   ************************************************************/
  implicit val webSocketTransformerWebSocketEvents= new MessageFlowTransformer[WebSocketEvent, WebSocketEvent] {
    override def transform(flow: Flow[WebSocketEvent, WebSocketEvent, _]): Flow[Message, Message, _] = {
      AkkaStreams.bypassWith[Message, WebSocketEvent, Message](Flow[Message] collect {
        case BinaryMessage( data ) =>
          Unpickle[WebSocketEvent].tryFromBytes(data.asByteBuffer) match {
            case Success( msg ) => Left( msg )
            case Failure( err ) => Right( CloseMessage(CloseCodes.Unacceptable, s"Error with transfer: $err"))
          }
        case _ => Right(CloseMessage(CloseCodes.Unacceptable, "This WebSocket only accepts binary."))
      })( flow.map { msg =>
        val bytes = ByteString.fromByteBuffer( Pickle.intoBytes( msg ) )
        BinaryMessage( bytes )
      })
    }
  }

  private[this] val factoryChatRooms = system.actorOf(Props(classOf[FactoryChatRooms]))

  def socketMain = WebSocket.accept[WebSocketEvent, WebSocketEvent] { request =>
    ActorFlow.actorRef { out => Props(new ChatClientActor(out, factoryChatRooms))}
  }
  /**********************************************************************************************************************/

}


