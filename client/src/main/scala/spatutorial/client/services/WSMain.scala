package spatutorial.services

import spatutorial.client.modules.StoreApp._
import outwatch.util.WebSocket
import monix.execution.Ack._
import scala.scalajs.js.typedarray.{ArrayBuffer, TypedArrayBuffer, Uint16Array, Uint8Array}
import scala.scalajs.js.typedarray.TypedArrayBufferOps._
import org.scalajs.dom
import org.scalajs.dom._
import monix.reactive.Observer
import org.scalajs.dom.{Event, MessageEvent}
import monix.execution.Scheduler.Implicits.global
import boopickle.Default._
import monix.execution.Ack
import scala.concurrent.Future
import spatutorial.shared.WebSocketEvent._

object WebSocketMain {

  val host = "192.168.0.5"

  val port = "9000"
  val path = "ws"
  val url = s"ws://$host:$port/$path"

  var wsMain = WebSocket(url)

  wsMain.ws.onopen = { v =>
    val bytes = Pickle.intoBytes[WebSocketEvent](ChatMessage("ping")).arrayBuffer()
    wsMain.ws.send(bytes)
  }

  wsMain.ws.binaryType = "arraybuffer"

  val blobReader: FileReader = {  //Este es el FileReader, andas perdido.
    val reader = new FileReader()
    reader.onerror = (e: Event) => { dom.console.log(s"Error in blobReader: ${reader.error}") }
    reader.onload = (e: UIEvent) => {
      reader.result match {
        case buf: ArrayBuffer => val intP = dom.document.getElementById("int-value")
        case otracosa => println(otracosa)
      }
    }
    reader
  }

  var seqChunks: Seq[Array[Byte]] = Seq.empty

  def arrayToArrayBuffer( array: Array[Byte] ) = {
    val length = array.length
    val buffer = new ArrayBuffer( length * 2 )
    var view = new Uint16Array(buffer)
    (0 until (length - 1)).by(1).toList.foreach ( i => view(i) = array(i) )
    buffer
  }

  def onMoveNext(elem: MessageEvent) = {
    elem.data match {
      case buf: ArrayBuffer =>
        Unpickle[WebSocketEvent].fromBytes(TypedArrayBuffer.wrap(buf)) match {
          case ChatMessage(pong) if pong == "pong" =>     //Para establecer la comunicación.
            store.headL.runAsync {
              case Right(value) =>  wsMain.ws.send( Pickle.intoBytes[WebSocketEvent]( value._2.user ).arrayBuffer( ) )
              case Left(ex) =>      System.out.println( s"ERROR: ${ex.getMessage}" )
            }
          case b: BroadCastMessage =>
            store.onNext(AddComment(b))
          case bFile: BroadCastFile =>
            seqChunks = seqChunks ++: Seq(bFile.file)

            if (bFile.eof){
              val fullFile = seqChunks.foldLeft(Array.empty[Byte])(_++_)
              store.onNext(AddComment(bFile.copy( file = fullFile )))
              seqChunks = Seq.empty
            }

          case m => println("Otro mensage que regresa del servidor!!!!!!!!!!!!!")
        }
      case blob: Blob =>
        dom.console.log("Error on receive, should be a blob.")
        blobReader.readAsArrayBuffer(blob)
      case msg: String =>
        val msgBack = dom.document.getElementById("msgBack").asInstanceOf[html.Span]
        msgBack.innerHTML = msg
      case _ => dom.console.log("Error on receive, should be a blob." )
    }
    Continue
  }


  val catchEvents = new Observer[MessageEvent] {
      def onNext(elem: MessageEvent): Future[Ack] =  onMoveNext(elem)
      def onError(ex: Throwable): Unit = {
        //ex.printStackTrace()
        getBackFromDeath()
      }
      def onComplete(): Unit = println( "O completed" )
    }

  def getBackFromDeath():Unit = {
    if (wsMain.ws.readyState == 3) {
      wsMain = null
      wsMain = WebSocket(url)
      wsMain.ws.binaryType = "arraybuffer"
      wsMain.observable.subscribe(catchEvents)
      wsMain.ws.onopen = ( v => wsMain.ws.send(Pickle.intoBytes[WebSocketEvent](ChatMessage("ping")).arrayBuffer()))
    } else wsMain.ws.close()
    ()
  }

  wsMain.observable.subscribe(catchEvents)

}
