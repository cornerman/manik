package spatutorial.client.components

import outwatch.dom.Handler
import outwatch.dom.dsl._
import spatutorial.shared.Fechas

object Components {

  def cmpInput(lbl: String, hdl: Handler[String], w: Int) =
    div( id := "control",
      float.left, width := (w + 35).toString  + "px", label( lbl, width := "60px" ),
      input (
        value <-- hdl.map( r => r ),
        onChange.target.value --> hdl,
        width := w.toString + "px"
      )
    )

  /*def cmpInputInt( lbl: String, hdl: Handler[Int], w: Int ) =
    div( float.left, width := (w + 35).toString  + "px",
      label( lbl, width := "60px" ),
      input (
        value <-- hdl.map( r => r.toString ),
        onChange.target.value.map(r=> r.toInt) --> hdl,
        width := w.toString + "px"
      )
    )*/

  def cmpInputFechas( lbl: String, hdl: Handler[Fechas], w: Int ) =
    div( float.left, width := (w + 35).toString  + "px",
      label( lbl, width := "60px" ),
      input (
        value <-- hdl.map( r => r.fecha ),
        onChange.target.value.map(r=> Fechas(r)) --> hdl,
        width := w.toString + "px"
      )
    )

  def cmpInputSearch( lbl: String, hdl: Handler[String], w: Int ) =
    div( width := w.toString + "px", label(lbl),
      input (
        value <-- hdl.map( r => r ),
        onChange.target.value --> hdl,
        width := "60px"
      )
    )


  //val handler: Handler[Int] = ???
  //cmdInput("Some Label", handler.mapHandler[String](_.toInt)(_.toString), 100)

  /*

you could redirect your handler when calling the method with other type and parse the value to and from string. For example for the int case:

val handler: Handler[Int] = ???
cmdInput("Some Label", handler.mapHandler[String](_.toInt)(_.toString), 100)

or have have some error handling for the int<=>string conversion (i should add that method to outwatch):

implicit class HandlerWithMaybe[T](val self: Handler[T]) extends AnyVal {
  def mapHandlerMaybe[T2](write: T2 => Option[T])(read: T => T2): Handler[T2] = outwatch.ProHandler(self.redirectMapMaybe(write), self.map(read))
}

val handler: Handler[Int] = ???
val strHandler: Handler[String] = handler.mapHandlerMaybe[String](s => Try(s.toInt).toOption)(_.toString)


  * */

}
