package services
import spatutorial.shared._

import akka.http.scaladsl.unmarshalling.Unmarshaller
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, JsBoolean, JsNull, JsNumber, JsObject, JsString, JsValue, NullOptions, RootJsonFormat}

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol with NullOptions {
  //import rest.util.DateMarshalling._
  case class Id( id: String )
  case class Search( search: String )

  case class ItemArticulo( item: Articulo )
  case class ItemProveedor( item: Proveedor )

  implicit val idProtocol = jsonFormat1(Id)
  implicit val searchProtocol = jsonFormat1(Search)

  implicit val numFormat = jsonFormat1(Deleted)

  implicit object articuloFormat extends RootJsonFormat[Articulo] {
    override def read(value: JsValue) = {
      val id = fromField[String](value, "id")
      val descripcion = fromField[Option[String]](value, "descripcion")
      val unidad = fromField[String](value, "unidad")
      val presentacion = fromField[Option[Int]](value, "presentacion")
      val unid_med_pres = fromField[Option[String]](value, "unid_med_pres")
      val partida = fromField[String](value, "partida")
      val cabms = fromField[Option[String]](value, "cabms")
      val cb = fromField[Option[Boolean]](value, "cb")
      val iva = fromField[Option[Double]](value, "iva")
      val baja = fromField[Option[Boolean]](value, "baja")
      Articulo(id, descripcion, unidad, presentacion, unid_med_pres, partida, cabms, cb, iva, baja)
    }

    override def write( obj: Articulo ): JsValue = {
      val unidMedPres = if ( obj.unid_med_pres == None ) JsNull else JsString( obj.unid_med_pres.getOrElse( "" ) )

      JsObject (
        "id" -> JsString( obj.id ),
        "descripcion" -> JsString( obj.descripcion.getOrElse( "" ) ),
        "unidad" -> JsString(obj.unidad),
        "presentacion" -> JsNumber(obj.presentacion.getOrElse( 0 ) ),
        "unid_med_pres" -> unidMedPres,
        "partida" -> JsString( obj.partida ),
        "cabms" -> JsString( obj.cabms.getOrElse( "" ) ),
        "cb" -> JsBoolean(obj.cb.getOrElse(false)),
        "iva" -> JsNumber(obj.iva.getOrElse(0.0)),
        "baja" -> JsBoolean(obj.baja.getOrElse(false))
      )
    }

    /*override def write(obj: Articulo): JsValue = {
      val unidMedPres = if(obj.unid_med_pres == None) JsNull else JsString(obj.unid_med_pres.getOrElse(""))
      JsObject(
        "id" -> JsString( obj.id ),
        "descripcion" -> JsString( obj.descripcion.getOrElse( "" ) ),
        "unidad" -> JsString(obj.unidad),
        "presentacion" -> JsNumber(obj.presentacion.getOrElse( 0 ) ),
        "unid_med_pres" -> unidMedPres,
        "partida" -> JsString( obj.partida ),
        "cabms" -> JsString( obj.clave_cabms.getOrElse("") ),
        "cb" -> JsBoolean(obj.cb.getOrElse(false)),
        "iva" -> JsNumber(obj.iva.getOrElse(0.0)),
        "baja" -> JsBoolean(obj.baja.getOrElse(false))
      )
    }*/
  }

  implicit object oficinaFormat extends RootJsonFormat[Oficina] {
    override def read(value: JsValue) = {
      val id = fromField[String](value, "cve_depto")
      val descripcion = fromField[Option[String]](value, "descripcion")
      val firma = fromField[Option[String]](value, "firma")
      val cargo = fromField[Option[String]](value, "cargo")
      Oficina ( id, descripcion, firma, cargo )
    }

    override def write(obj: Oficina): JsValue = {
      val id = if(obj.id.isEmpty) JsNull else JsString(obj.id)
      val descripcion = if(obj.descripcion.isEmpty) JsNull else JsString(obj.descripcion.getOrElse(""))
      val firma = if(obj.firma == None) JsNull else JsString(obj.firma.getOrElse(""))
      val cargo = if(obj.cargo == None) JsNull else JsString(obj.cargo.getOrElse(""))

      JsObject (
        "id" -> id,
        "descripcion" -> descripcion,
        "firma" -> firma,
        "cargo" -> cargo
      )
    }
  }

  implicit object programaFormat extends RootJsonFormat[Programa] {
    override def read(value: JsValue) = {
      val id = fromField[String](value, "id")
      val descripcion = fromField[Option[String]](value, "descripcion")
      val destino = fromField[String](value, "destino")
      val depto = fromField[String](value, "depto")
      val mostrar = fromField[Boolean](value, "mostrar")
      val rfc_dependencia = fromField[String](value, "rfc_dependencia")
      val nivel = fromField[String](value, "nivel")
      val encargado = fromField[Option[String]](value, "encargado")
      val activo = fromField[Boolean](value, "activo")
      val fuente_financiamiento = fromField[String](value, "fuente_financiamiento")

      Programa(id, descripcion, destino, depto, mostrar, rfc_dependencia, nivel, encargado, activo, fuente_financiamiento)
    }

    override def write(obj: Programa): JsValue = {
      val id = if(obj.id.isEmpty) JsNull else JsString(obj.id)
      val descripcion = if(obj.descripcion == None) JsNull else JsString(obj.descripcion.getOrElse(""))
      val destino = if(obj.destino == None) JsNull else JsString(obj.destino)
      val encargado = if(obj.encargado == None) JsNull else JsString(obj.encargado.getOrElse(""))

      JsObject(
        "id" -> id,
        "descripcion" -> descripcion,
        "destino" -> destino,
        "depto" -> JsString(obj.depto),
        "mostrar" -> JsBoolean(obj.mostrar),
        "rfc_dependencia" -> JsString(obj.rfc_dependencia),
        "nivel" -> JsString(obj.nivel),
        "encargado" -> encargado,
        "activo" -> JsBoolean(obj.activo),
        "fuente_financiamiento" -> JsString(obj.fuente_financiamiento)
      )

    }
  }

  implicit object proveedorFormat extends RootJsonFormat[Proveedor] {

    override def read(value: JsValue) = {
      val id = fromField[String](value, "id")
      val descripcion = fromField[Option[String]](value, "descripcion")
      val propietario = fromField[Option[String]](value, "propietario")
      val calle = fromField[Option[String]](value, "calle")
      val colonia = fromField[Option[String]](value, "colonia")
      val delegacion = fromField[Option[String]](value, "delegacion")
      val cp = fromField[Option[String]](value, "cp")
      val ciudad = fromField[Option[String]](value, "ciudad")
      val telefonos = fromField[Option[String]](value, "telefonos")
      val fax = fromField[Option[String]](value, "fax")
      val observaciones = fromField[Option[String]](value, "observaciones")
      val activo = fromField[Boolean](value, "activo")
      val elaboro = fromField[Option[String]](value, "elaboro")
      val giro = fromField[Option[String]](value, "giro")
      val descuento = fromField[Option[String]](value, "descuento")
      Proveedor(id, descripcion, propietario, calle, colonia, delegacion, cp, ciudad, telefonos, fax, observaciones, activo, elaboro, giro, descuento )
    }

    override def write( obj: Proveedor )  = {

      val descripcion = if(obj.descripcion == None) JsNull else JsString(obj.descripcion.getOrElse(""))
      val propietario = if(obj.propietario == None) JsNull else JsString(obj.propietario.getOrElse(""))
      val calle = if(obj.calle == None) JsNull else JsString(obj.calle.getOrElse(""))
      val colonia = if(obj.colonia == None) JsNull else JsString(obj.colonia.getOrElse(""))
      val delegacion = if(obj.delegacion == None) JsNull else JsString(obj.delegacion.getOrElse(""))
      val cp = if(obj.cp == None) JsNull else JsString(obj.cp.getOrElse(""))
      val ciudad = if(obj.ciudad == None) JsNull else JsString(obj.ciudad.getOrElse(""))
      val telefonos = if(obj.telefonos == None) JsNull else JsString(obj.telefonos.getOrElse(""))
      val fax = if(obj.fax == None) JsNull else JsString(obj.fax.getOrElse(""))
      val observaciones = if(obj.observaciones == None) JsNull else JsString(obj.observaciones.getOrElse(""))
      val elaboro = if(obj.elaboro == None) JsNull else JsString(obj.elaboro.getOrElse(""))
      val giro = if(obj.giro == None) JsNull else JsString(obj.giro.getOrElse(""))
      val descuento = if(obj.descuento == None) JsNull else JsString(obj.descuento.getOrElse(""))

      JsObject (
        "id" -> JsString( obj.id ),
        "descripcion" -> descripcion,
        "propietario" -> propietario,
        "calle" -> calle,
        "colonia" -> colonia,
        "delegacion" -> delegacion,
        "cp" -> cp,
        "ciudad" -> ciudad,
        "telefonos" -> telefonos,
        "fax" -> fax,
        "observaciones" -> observaciones,
        "elaboro" -> elaboro,
        "activo" -> JsBoolean( obj.activo ),
        "descuento" -> descuento,
      )
    }
  }

  implicit val presentacionFormat = jsonFormat4(Presentacion)
  implicit val partidaFormat = jsonFormat5(Partida)
  implicit val fuenteFormat = jsonFormat5(Fuente)

}
