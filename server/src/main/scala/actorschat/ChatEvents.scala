package actorschat.chatevents

import akka.actor.ActorRef

sealed trait ChatEvent
final case class UserJoined(name: String, userActor: ActorRef) extends ChatEvent
final case class UserLeft(name: String) extends ChatEvent
final case class ChatMessage(sender: String, text: String) extends ChatEvent

object SystemMessage {
  def apply(text: String) = ChatMessage("System", text)
}
