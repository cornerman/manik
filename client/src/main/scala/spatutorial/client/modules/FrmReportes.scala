package spatutorial.client

import boopickle.Default._
import outwatch.dom._
import outwatch.dom.dsl._
import monix.execution.Scheduler.Implicits.global
import monix.execution.Ack._
import monix.reactive.Observable
import org.scalajs.dom.{html, window}
import spatutorial.client.modules.StoreApp._
import scala.scalajs.js.typedarray.TypedArrayBufferOps._
import com.github.marklister.base64.Base64._
import spatutorial.client.components.Components._
import spatutorial.shared.WebSocketEvent._
import spatutorial.services.WebSocketMain._

import spatutorial.shared.CriteriaPedido._

import scala.scalajs.js.typedarray.{ArrayBuffer, TypedArrayBuffer}
import outwatch.ReactiveConnectable
import spatutorial.client.components.FileSlicer

object FrmReportes {

  /****** Para empezar ha establecer la comunicación con un socket **/
  wsMain.observable.subscribe(catchEvents)
  wsMain.ws.onopen = { v => wsMain.ws.send(Pickle.intoBytes[WebSocketEvent](ChatMessage("ping")).arrayBuffer()) }

  val userJoined = UserJoinChat(name = "", nameRoom = "")

  val handlerUser = Handler.create[UserJoinChat](userJoined).unsafeRunSync()
  handlerUser.onNext ( userJoined )

  val txtUser = handlerUser.lens[String]( userJoined)( _.name )((userjoined, name) => userjoined.copy(name = name))
  val cancelableUser = txtUser.connect() // need to subscribe to this handler, because it internally needs to track the current state.

  val txtChannel = handlerUser.lens[String]( userJoined)( _.nameRoom )((userjoined, nameRoom) => userjoined.copy(nameRoom = nameRoom))
  val cancelableChannel = txtChannel.connect() // need to subscribe to this handler, because it internally needs to track the current state.


  /****************************** Vamos a manejar los criterios que filtraran los resultados. ****************/

  case class HandlerCriteriaPedido(no_pedido: String, ejercicio: Int, compra: String)

  val criteriosInitial = HandlerCriteriaPedido(no_pedido = "", ejercicio = 2019, compra = "PEDI")

  val handlerCriterio = Handler.create[HandlerCriteriaPedido](criteriosInitial).unsafeRunSync()
  handlerCriterio.onNext(criteriosInitial)

  val txtNoPedido = handlerCriterio.lens[String](criteriosInitial)( _.no_pedido )((criteriaPedido, no_pedido) => criteriaPedido.copy(no_pedido = no_pedido))
  val cancelableNoPedido = txtNoPedido.connect() // need to subscribe to this handler, because it internally needs to track the current state.

  val txtEjercicio = handlerCriterio.lens[Int](criteriosInitial)( _.ejercicio)((criteriaPedido, ejercicio) => criteriaPedido.copy(ejercicio = ejercicio))
  val cancelableEjercicio = txtEjercicio.connect() // need to subscribe to this handler, because it internally needs to track the current state.
  /*************************************************************************************************************/

  def downloadFile = Sink.create[BroadCastFile]{ bFile =>
    val b64File = bFile.file.toBase64
    val mimeType =  if (bFile.nameFile.takeRight(3) == "pdf") "data:application/pdf;base64, "
    else "data:image/jpeg;base64, "
    window.open(mimeType + b64File)
    Continue
  }

  val sendJoin = Sink.create[UserJoinChat]{ u =>
    val bytes = Pickle.intoBytes[WebSocketEvent](u).arrayBuffer()
    wsMain.ws.send(bytes)
    store.onNext(UpdateUser(u))
    Continue
  }

  val getReport = Sink.create[GetReporte]{ g =>
    val bytes = Pickle.intoBytes[WebSocketEvent](g).arrayBuffer()
    wsMain.ws.send(bytes)
    Continue
  }

  /*
  johannes karoff
  @cornerman
  Dec 18 2018 15:26
  @elyphas additional feedback to your previous example with Sink.create.
  You construction of the IO should look a bit different,
  when you want to model the side effect:
    def saveButtonSink = Sink.create[String] { x: String =>
      IO {
        println(x)
        Ack.Continue
      }
    }*/

  import cats.implicits._

  val getCriteria = (txtNoPedido: Observable[String], txtEjercicio: Observable[Int],
        ).parMapN { case (noPedido: String, ejercicio: Int) =>

          var condPedido: Seq[CriteriaPedidos] = if (noPedido != "") Seq(CritPedido(no_pedido = noPedido)) else Seq.empty[CritPedido]
          var condEjercicio: Seq[CriteriaPedidos] = if (ejercicio != 0) Seq(CritEjercicio(ejercicio = ejercicio)) else Seq.empty[CritEjercicio]

          val allCrit = condPedido ++: condEjercicio

          GetReporte(criteria = allCrit)

        }

  def render = for { s <- store } yield {
    div(id:="pageForm",
      div(id := "groupControls", clear.both,
        cmpInput("Usuario", txtUser, 50),
        cmpInput("Canal", txtChannel, 50),
        button("Unirse", marginTop:= "18px", onClick(handlerUser) --> sendJoin),
      ),

      div(id := "groupControls", clear.both,
        label("Criterios a considerar para generar el reporte"),
        cmpInput("No. Pedido.", txtNoPedido, 50),
        //cmpInput("Canal", txtChannel, 50),
        button("Generar Reporte", marginTop:= "18px", onClick(getCriteria) --> getReport),
      ),

      div( cls:="comments",
        ol(
          s._2.conversation.map {
            case comment: BroadCastMessage => li(comment.usuario + "; dice:" + comment.message)
            case comment: BroadCastFile => li( cls:="hiperlink", "descargar archivo pdf", onClick(comment) --> downloadFile)
          }
        )
      )
    )
  }
}