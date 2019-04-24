package actorschat

import akka.actor.{Actor, ActorRef}
import akka.actor._
import spatutorial.shared.WebSocketEvent._

class ChatRoomActor(nameRoom: String) extends Actor {
  var participants: Map[String, ActorRef] = Map.empty[String, ActorRef]
  def receive: Receive = {
    case userJoined: actorschat.chatevents.UserJoined => // userActorRef es el actorRef que obtenemos cuando se conecta "out: ActorRef".
      participants += userJoined.name -> userJoined.userActor
      val msg = "Usuario " + userJoined.name + " se unio al canal..."
      broadcast(BroadCastMessage(usuario = "", canal = nameRoom, message = msg))
    case UserLeft(name) =>
      broadcast(BroadCastMessage(usuario = "", canal = nameRoom, message = s"Usuario $name dejo al canal..."))
      participants -= name
    case msg: IncomingMessage =>
      broadcast(BroadCastMessage(usuario = msg.usuario, canal = msg.canal, message = msg.message))
    case msg: IncomingFile =>
      broadcast(BroadCastFile(usuario = msg.usuario, canal = msg.canal, file = msg.file, nameFile = msg.nameFile,  eof = msg.eof))
    case v =>
      println("Problemas Vamos a ver que msg le tamos mandando: " + v)
  }

  def broadcast(msg: BroadCastMessage): Unit =
    participants.map { case (k, v) =>
      v ! msg
      Unit
    }

  def broadcast(msg: BroadCastFile): Unit =
    participants.map { case (k, v) =>
      v ! msg
      Unit
    }

}