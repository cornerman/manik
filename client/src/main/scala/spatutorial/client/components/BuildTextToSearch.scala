package spatutorial.client.components

//import fastparse.utils.Generator.Callback
//import spatutorial.client.services.SPACircuit
//import spatutorial.client.actionhandlers.{SearchArticulo, SearchFuente, SearchOficina, SearchPrograma}
import spatutorial.shared._
import org.scalajs.dom.document.{getElementById => getElem}
//import scalacss.ScalaCssReact._
//import japgolly.scalajs.react.Callback

////////////******************
import akka.actor._
import akka.actor
import scalatags.JsDom._
import scalatags.JsDom.all._
import org.scalajs.dom
///////////*******************

sealed trait MsgActor
object MensagesActor {
  //case class Letter(l: String) extends MsgActor
  case class SearchText[ T <: Father ](s: String, t: T) extends MsgActor
}

import MensagesActor._

//class BuildTextToSearch[T<:Father](nameSpan: String) extends Actor {
class BuildTextToSearch[T<:Father] extends Actor {

  /*import org.scalajs.dom.raw
  val domElement: raw.Node = getElem( nameSpan )*/

  override def receive: Receive = {
    case SearchText(s, t) =>  //Aqui probablemente seria mejor pasar el objecto ya sea Articulo(descripcion="algo") o Oficina(descripcion="algo")
      println ( "Esta buscando" )
      /*t match {
        case t: Articulo if (s.length % 4) == 0 => SPACircuit.dispatch(SearchArticulo(s))
        case t: Oficina if s.length > 4 => SPACircuit.dispatch(SearchOficina(s))
        case t: Fuente if s.length > 4 => SPACircuit.dispatch(SearchFuente(s))
        case t: Programa if s.length > 4 => SPACircuit.dispatch(SearchPrograma(s))
        case _ =>
          //domElement.textContent("Faltan mas descripcion por buscar")
          println("Faltan mas descripcion por buscar")
      }*/
  }
}

class CmpCatalogs[ T <: Father ](nameDiv: String, onClick: (String, String) => Unit) extends DomActorWithParams[T, Seq] {

  override val domElement = Some( getElem( nameDiv ) )

  override val initValue: Seq[ T ] = Seq.empty[ T ]

  override def template( items: Seq[ T ], pos: ( Int,Int ) ) = {
    def renderItem( id: String, descripcion: String ) = li(
      div( border := "1px solid", borderColor := "black", width := "500", background:="#3dbaf9",
          position := "absolute", zIndex:="auto",
          id + " : " + descripcion.substring(0, 100),
          onclick := { e: dom.Event =>
            self ! UpdateValue( Seq.empty, ( 0, 0 ) )
            onClick( id, descripcion )
          }
      ))

    if ( items.isEmpty ) div( id := "actorDiv", visibility := "hidden" )

    else div( id := "actorDiv", border := "1px solid", width := "500px", overflow := "scroll", position := "absolute",
        zIndex := "10001", top := pos._2.toString + "px", left := pos._1.toString + "px",
        ul(items.map( i => renderItem( i.id , i.descripcion.getOrElse( "" ) ) ) )
      )
  }
}

