package spatutorial.client.components

import akka.actor.Actor
import org.scalajs.dom.raw
import scalatags.JsDom._
import spatutorial.shared.Father
import scala.language.higherKinds

object DomMsgs {
  case object NodeAsk
  case class Parent(node: raw.Node)
  case class Remove(node: raw.Node)
}

import DomMsgs._

trait DomActor extends Actor {

  case object Update
  val domElement: Option[raw.Node] = None

  def template: TypedTag[_ <: raw.Element]

  protected var thisNode: raw.Node = _

  def receive = domRendering

  protected def initDom(p: raw.Node): Unit = {
    thisNode = template().render
    p.appendChild(thisNode)
  }

  private def domRendering: Receive = {
    domElement match {
      case Some( de ) =>
        val parent = de.parentNode
        parent.removeChild( de )
        initDom( parent )
        operative
      case _ =>
        context.parent ! NodeAsk
        domManagement orElse {
          case Parent( node ) =>
            initDom( node )
            context.become( operative )
        }
    }
  }

  def domManagement: Receive = updateManagement orElse {
      case NodeAsk => sender ! Parent(thisNode)
      case Remove(child) => thisNode.removeChild(child)
    }

  def updateManagement: Receive = {
    case Update =>
      val p = thisNode.parentNode
      val oldNode = thisNode
      thisNode = template().render
      p.replaceChild(thisNode, oldNode)
  }

  def operative: Receive = domManagement

  override def postStop() = {
    context.parent ! Remove(thisNode)
  }
}

import scala.reflect.ClassTag

case class UpdateValue[T<:Father: ClassTag, F[_]](value: F[T], position:(Int,Int))
case object CleanCatalog

trait DomActorWithParams[T<:Father, F[_]] extends DomActor {

  val initValue: F[T]

  def template(): TypedTag[_ <: raw.Element] = null
  def template(value: F[T], position: (Int, Int)): TypedTag[_ <: raw.Element]

  override protected def initDom( p: raw.Node ) = {
      thisNode = template( initValue, (0, 0) ).render
      p.appendChild(thisNode)
  }

  override def updateManagement: Receive = {
    case UpdateValue(newValue: F[T], position) => //here we can use a virtual dom ...
      val p = thisNode.parentNode
      val oldNode = thisNode
      thisNode = template(newValue, position).render
      p.replaceChild(thisNode, oldNode)
    case _ =>
      println ( "Paso por aqui no deberia ·····················" )

  }

}