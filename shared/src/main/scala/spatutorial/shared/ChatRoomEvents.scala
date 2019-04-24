package actorschat.chatevents

import akka.actor.ActorRef

object ChatRoomEvents {

  sealed trait ChatRoomEvents
  //sealed trait ChatRoomEvents
  final case class GetRoom(nameRoom: String, userActor: ActorRef) extends ChatRoomEvents
  final case class UserJoined(name: String, userActor: ActorRef) extends ChatRoomEvents
  final case class UserLeft(name: String) extends ChatRoomEvents
  //case class IncomingMessage(sender: String, message: String) extends ChatRoomEvents
  final case class SetRoom(room: ActorRef) extends ChatRoomEvents

  case class ChatMessage(sender: String, text: String) extends ChatRoomEvents

  object SystemMessage {
    def apply(text: String) = ChatMessage("System", text)
  }

}