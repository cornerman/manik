package services.jsontypes

import scala.language.postfixOps
import spatutorial.shared._
import play.api.libs.json._
import play.api.libs.functional.syntax._

object JsonTypes {
  implicit val presentacionWrites: Writes[Presentacion] = (
    (JsPath \ "id").write[String] and
    (JsPath \ "descripcion").writeNullable[String] and
    (JsPath \ "presentacion").writeNullable[Double] and
    (JsPath \ "unidad_present").writeNullable[String]
  )( unlift(Presentacion.unapply ) )

  implicit val presentacionReads: Reads[Presentacion] = (
    (JsPath \ "id").read[String] and
    (JsPath \ "descripcion").readNullable[String] and
    (JsPath \ "presentacion").readNullable[Double] and
    (JsPath \ "unidad_present").readNullable[String]
  )(Presentacion.apply _)

  implicit val partidaWrites: Writes[Partida] = (
    (JsPath \ "id").write[String] and
    (JsPath \ "descripcion").writeNullable[String] and
    (JsPath \ "observaciones").writeNullable[String] and
    (JsPath \ "presupuesto").writeNullable[Double] and
    (JsPath \ "activo").writeNullable[Boolean]
  )( unlift(Partida.unapply ) )

  implicit val partidaReads: Reads[Partida] = (
    (JsPath \ "id").read[String] and
    (JsPath \ "descripcion").readNullable[String] and
    (JsPath \ "observaciones").readNullable[String] and
    (JsPath \ "presupuesto").readNullable[Double] and
    (JsPath \ "activo").readNullable[Boolean]
  )(Partida.apply _)

  implicit val oficinaWrites: Writes[Oficina] = (
    (JsPath \ "id").write[String] and
    (JsPath \ "descripcion").writeNullable[String] and
    (JsPath \ "firma").writeNullable[String] and
    (JsPath \ "cargo").writeNullable[String]
  )( unlift(Oficina.unapply ) )

  implicit val oficinaReads: Reads[Oficina] = (
    (JsPath \ "id").read[String] and
    (JsPath \ "descripcion").readNullable[String] and
    (JsPath \ "firma").readNullable[String] and
    (JsPath \ "cargo").readNullable[String]
  )( Oficina.apply _ )

  implicit val articuloWrites: Writes[Articulo] = (
    (JsPath \ "id").write[String] and
      (JsPath \ "descripcion").writeNullable[String] and
      (JsPath \ "unidad").write[String] and
      (JsPath \ "presentacion").writeNullable[Int] and
      (JsPath \ "unid_med_pres").writeNullable[String] and
      (JsPath \ "partida").write[String] and
      (JsPath \ "cabms").writeNullable[String] and
      (JsPath \ "cb").writeNullable[Boolean] and
      (JsPath \ "iva").writeNullable[Double] and
      (JsPath \ "baja").writeNullable[Boolean] )( unlift(Articulo.unapply ) )

  implicit val articuloReads: Reads[Articulo] = (
    (JsPath \ "id").read[String] and
      (JsPath \ "descripcion").readNullable[String] and
      (JsPath \ "unidad").read[String] and
      (JsPath \ "presentacion").readNullable[Int] and
      (JsPath \ "unid_med_pres").readNullable[String] and
      (JsPath \ "partida").read[String] and
      (JsPath \ "cabms").readNullable[String] and
      (JsPath \ "cb").readNullable[Boolean] and
      (JsPath \ "iva").readNullable[Double] and
      (JsPath \ "baja").readNullable[Boolean] )( Articulo.apply _ )

  implicit val proveedorWrites: Writes[Proveedor] = (
    (JsPath \ "id").write[String] and
      (JsPath \ "descripcion").writeNullable[String] and
      (JsPath \ "propietario").writeNullable[String] and
      (JsPath \ "calle").writeNullable[String] and
      (JsPath \ "colonia").writeNullable[String] and
      (JsPath \ "delegacion").writeNullable[String] and
      (JsPath \ "cp").writeNullable[String] and
      (JsPath \ "ciudad").writeNullable[String] and
      (JsPath \ "telefonos").writeNullable[String] and
      (JsPath \ "fax").writeNullable[String] and
      (JsPath \ "observaciones").writeNullable[String] and
      (JsPath \ "activo").write[Boolean] and
      (JsPath \ "elaboro").writeNullable[String] and
      (JsPath \ "giro").writeNullable[String] and
      (JsPath \ "descuento").writeNullable[String] )( unlift( Proveedor.unapply ) )

  implicit val proveedorReads: Reads[Proveedor] = (
    (JsPath \ "id").read[String] and
      (JsPath \ "descripcion").readNullable[String] and
      (JsPath \ "propietario").readNullable[String] and
      (JsPath \ "calle").readNullable[String] and
      (JsPath \ "colonia").readNullable[String] and
      (JsPath \ "delegacion").readNullable[String] and
      (JsPath \ "cp").readNullable[String] and
      (JsPath \ "ciudad").readNullable[String] and
      (JsPath \ "telefonos").readNullable[String] and
      (JsPath \ "fax").readNullable[String] and
      (JsPath \ "observaciones").readNullable[String] and
      (JsPath \ "activo").read[Boolean] and
      (JsPath \ "elaboro").readNullable[String] and
      (JsPath \ "giro").readNullable[String] and
      (JsPath \ "descuento").readNullable[String]
    )( Proveedor.apply _ )
  /******************************************************************/


  /***** Fuente de Financiamiento **************/
  implicit val fuenteFinanciamientoWrites: Writes[Fuente] = (
    (JsPath \ "id").write[String] and
      (JsPath \ "descripcion").writeNullable[String] and
      (JsPath \ "observaciones").writeNullable[String] and
      (JsPath \ "rfc_dependencia").write[String] and
      (JsPath \ "nivel").writeNullable[String]
    )( unlift( Fuente.unapply ) )

  implicit val fuenteFinanciamientoReads: Reads[Fuente] = (
    (JsPath \ "id").read[String] and
      (JsPath \ "descripcion").readNullable[String] and
      (JsPath \ "observaciones").readNullable[String] and
      (JsPath \ "rfc_dependencia").read[String] and
      (JsPath \ "nivel").readNullable[String]
    )( Fuente.apply _ )
  /*****************************************/

  /***** Programa presupuestal **************/
  implicit val programaWrites: Writes[Programa] = (
    (JsPath \ "id").write[String] and
      (JsPath \ "descripcion").writeNullable[String] and
      (JsPath \ "destino").write[String] and
      (JsPath \ "depto").write[String] and
      (JsPath \ "mostrar").write[Boolean] and
      (JsPath \ "rfc_dependencia").write[String] and
      (JsPath \ "nivel").write[String] and
      (JsPath \ "encargado").writeNullable[String] and
      (JsPath \ "activo").write[Boolean] and
      (JsPath \ "fuente_financiamiento").write[String]
    )( unlift( Programa.unapply ) )

  implicit val programaReads: Reads[Programa] = (
    (JsPath \ "id").read[String] and
      (JsPath \ "descripcion").readNullable[String] and
      (JsPath \ "destino").read[String] and
      (JsPath \ "depto").read[String] and
      (JsPath \ "mostrar").read[Boolean] and
      (JsPath \ "rfc_dependencia").read[String] and
      (JsPath \ "nivel").read[String] and
      (JsPath \ "encargado").readNullable[String] and
      (JsPath \ "activo").read[Boolean] and
      (JsPath \ "fuente_financiamiento").read[String]
    )( Programa.apply _ )
  /*****************************************/

}