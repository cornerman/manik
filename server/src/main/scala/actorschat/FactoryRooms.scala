package actorschat
//import actorschat.ChatRoom
import actorschat.chatevents.ChatRoomEvents.{GetRoom, SetRoom, UserJoined}
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
//import spatutorial.shared.ChatEvent.{ChatMessage, IncomingMessage}
//object ChatRooms {

import actorschat.chatevents._

object FactoryChatRooms {
  def props = Props(new FactoryChatRooms())
}

class FactoryChatRooms extends Actor {
  //(out: ActorRef)
  implicit val actorSystem: ActorSystem = ActorSystem("chatRooms")
  //var chatRooms: Map[String, ChatRoom] = Map.empty[String, ChatRoom]
  var chatRooms: Map[String, ActorRef] = Map.empty[String, ActorRef]
  def receive: Receive = {
    /*case UserJoined(userName, nameRoom) =>
      println( s"Uniendose al canal $nameRoom el usuario: " + userName)
      val newRoom = findOrCreate(nameRoom)
      newRoom.sendMessageJoin(UserJoined2(userName, out))*/
    case GetRoom(nameRoom, sender) =>
      val newRoom = findOrCreate(nameRoom)
      //newRoom.sendMessageJoin(UserJoined2(nameRoom, sender))
      chatRooms += nameRoom -> newRoom
      sender ! SetRoom(newRoom)
    //case msg: IncomingMessage =>
      //val room = chatRooms.getOrElse(msg.canal, createNewChatRoom(msg.canal))
      //room.sendMessage(msg)
    //case msg: ChatMessage => out ! msg
    //case msg: String => out ! msg
    case _ => println("Otra cosa")
  }
  def findOrCreate(nameRoom: String)(implicit actorSystem: ActorSystem): ActorRef = {
      chatRooms.getOrElse(nameRoom, createNewChatRoom(nameRoom))
  }

  private def createNewChatRoom(nameRoom: String)(implicit actorSystem: ActorSystem): ActorRef = {   //ChatRoom = {
    //val chatroom = ChatRoom(nameRoom)
    println ( "Vamos a crear un nuevo ChatRoomActor si aparece dos veces ta mal ***************** " )
    val chatroom = actorSystem.actorOf(Props(classOf[ChatRoomActor], nameRoom))
    chatRooms += nameRoom -> chatroom
    chatroom
  }
}