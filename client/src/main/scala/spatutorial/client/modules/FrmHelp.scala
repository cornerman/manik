package spatutorial.client

import outwatch.dom._
import outwatch.dom.dsl._

object FrmHelp {

  def render =
    div( id := "pageForm",
      h5("Ayuda para generar reportes"),
      p(
        "Introducir en los cuadros correspondientes los datos que desee consultar."
      )
    )
}