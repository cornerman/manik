package actorschat

import java.io.{File, FileOutputStream}

import actorschat.chatevents.ChatRoomEvents.{GetRoom, SetRoom, UserJoined}
import spatutorial.shared.WebSocketEvent._
//import spatutorial.shared.ChatEvent.UserJoinChat
import akka.actor.{Actor, ActorRef, Props}

object ChatClientActor {
  def props(out: ActorRef, factoryChatRooms: ActorRef) = Props( new ChatClientActor(out, factoryChatRooms))
}

class ChatClientActor(out: ActorRef, factoryChatRooms: ActorRef) extends Actor {
  var userName: String = ""
  //var roomName: String = ""
  var roomRef: ActorRef = _

  var seqChunks: Seq[Array[Byte]] = Seq.empty

  def receive = {
    case ChatMessage(ping) if ping == "ping" =>   out ! ChatMessage("pong")
    case user: UserJoinChat =>
      userName = user.name
      factoryChatRooms ! GetRoom(user.nameRoom, self)
    case SetRoom(room: ActorRef) =>
      roomRef = room
      room ! actorschat.chatevents.UserJoined(userName, self)
    case m: IncomingMessage => roomRef ! m
    case f: IncomingFile =>

      seqChunks = seqChunks ++: Seq(f.file)

      if (f.eof) {
        val fullFile = seqChunks.foldLeft(Array.empty[Byte])(_++_)
        seqChunks = Seq.empty
        val imgOutFile = new File("/tmp/" + f.nameFile)
        val fileOuputStream = new FileOutputStream(imgOutFile)
        fileOuputStream.write(fullFile)
        fileOuputStream.close()
      }

      roomRef ! f
    case bmsg: BroadCastMessage => out ! bmsg
    case fmsg: BroadCastFile => out ! fmsg
    case msg => println("Catch-All ***ChatClientActor***" + msg)
  }

  override def postStop() {
    println( "Closing the websocket connection changos quien sabe por que!!!!!!!!!!" )
  }

}