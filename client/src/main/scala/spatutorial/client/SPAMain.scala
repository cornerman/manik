package spatutorial.client

import cats.effect.IO
import monix.execution.Scheduler
import outwatch.dom._
import outwatch.dom.dsl._
import outwatch.router._
import outwatch.router.dsl.C
import monix.reactive.Observable

sealed trait Page
final case object Reportes extends Page
final case object NotFound extends Page
final case object Home extends Page
final case object Help extends Page

object Menu {

  def router: AppRouter[Page] = AppRouter.create[Page](NotFound){
    case Root / "reportes" => Reportes
    case Root / "home" => Home
    case Root / "help" => Help
    case _ => NotFound
  }

  def pageContainer()(implicit S: Scheduler, router: RouterStore[Page]): IO[Observable[VDomModifier]] =
    IO(
      AppRouter.render[Page]{
        case Reportes => FrmReportes.render
        case Help => FrmHelp.render
        case Home =>
         div( clear.both,
           h3( "Inicio" ),
           p( "Bienvenido al sitio de reportes! :)" )
         )
        case NotFound => div( )
      }
    )

  val colorMenu = "#f7927c"
  val azulito = "#94f4e8"

  val styleMenu: List[VDomModifier] = List( display.inline, display.inline, float.left, padding := "5px",
    backgroundColor := colorMenu, border := "1px solid", borderColor := "#919393" )

  val mouseOver = Handler.create[String]("" ) //.unsafeRunSync( )

  def itemMenu(title: String, path: String)(implicit routerStore: RouterStore[Page]) =
    mouseOver.map { m =>
      li( styleMenu,
        onMouseOver.mapTo(title) --> m,
        onMouseOut.mapTo("") --> m,
        backgroundColor <-- m.map( r => if ( r == title ) azulito else colorMenu ),
        C.a[Page]( "/" + path )( title )
      )
    }

  def render()(implicit scheduler: Scheduler, router: RouterStore[Page]): VDomModifier =
    pageContainer().map { pc =>
      div( id:="mainMenu", cls := "ui two column grid",
        div( width := "1000px", //cls := "four wide column",
          ul(
            itemMenu("Home","home"),
            itemMenu("Reportes","reportes"),
            itemMenu("Ayuda","help")
          ),
        ),
        div( cls := "twelve wide fluid column",
          pc
        )
      )
    }
}



object HelloWoutWatch {
  import monix.execution.Scheduler.Implicits.global
  def main(args: Array[String]): Unit = {

    val program = for {
      implicit0(exRouterStore: RouterStore[Page]) <- Menu.router.store
      program <- OutWatch.renderInto("#root", div( Menu.render ) )

    } yield program

    //program.unsafeRunAsyncAndForget()

    program.unsafeRunSync()

  }

}
