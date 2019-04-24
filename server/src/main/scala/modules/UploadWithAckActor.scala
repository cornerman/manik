package modules
import java.io.{File, FileOutputStream}

import akka.actor._

import play.api.http.websocket.BinaryMessage


class UploadWithAckActor (out: ActorRef) extends Actor {

  def receive = {
    case BinaryMessage(data) =>

      val decoded: Array[Byte] = data.toArray
      val imgOutFile = new File("/tmp/" + "Spitting_Hydra.jpg")
      val fileOuputStream = new FileOutputStream(imgOutFile)

      fileOuputStream.write(decoded)
      fileOuputStream.close()
  }


}
