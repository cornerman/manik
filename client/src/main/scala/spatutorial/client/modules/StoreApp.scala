package spatutorial.client.modules

import outwatch.util.Store
import spatutorial.shared.WebSocketEvent._
import monix.execution.Scheduler.Implicits.global

object StoreApp {

  sealed trait ActionsApp
  case object Search extends ActionsApp
  case class AddComment(msg: WebSocketEvent) extends ActionsApp
  case class UpdateUser(user: UserJoinChat) extends ActionsApp
  case object Clean extends ActionsApp

  case class AppState( conversation: Seq[WebSocketEvent],
                       user: UserJoinChat = UserJoinChat(name = "", nameRoom = ""))

  val reduce: (AppState, ActionsApp) => AppState = (s, a) => a match {
    case Clean => s.copy(conversation = Seq.empty[BroadCastMessage])
    case AddComment(msg) => s.copy(conversation = s.conversation :+ msg)
    case UpdateUser(u) => s.copy(user = s.user.copy( name = u.name, nameRoom = u.nameRoom))
  }

  val initialState = AppState(conversation = Seq.empty[BroadCastMessage])
  val store = Store.create[ActionsApp, AppState]( Clean, initialState, reduce ).unsafeRunSync()

}