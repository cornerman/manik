package spatutorial.shared

//Para establecer los criterios cuando no se sabe que criterios se van ha establecer en el futuro.
object CriteriaPedido {
	sealed trait CriteriaPedidos
	final case class CritPedido(no_pedido: String) extends CriteriaPedidos
	final case class CritEjercicio(ejercicio: Int) extends CriteriaPedidos
}

import CriteriaPedido.CriteriaPedidos

object WebSocketEvent {
  sealed trait WebSocketEvent
  final case class UserJoinChat(name: String, nameRoom: String) extends WebSocketEvent
	final case class UserLeft(name: String) extends WebSocketEvent
  final case class IncomingMessage(usuario: String, canal: String, message: String) extends WebSocketEvent
  final case class BroadCastMessage(usuario: String, canal: String, message: String) extends WebSocketEvent
	final case class IncomingFile(usuario: String, canal: String, file: Array[Byte], nameFile: String, eof: Boolean) extends WebSocketEvent
	final case class BroadCastFile(usuario: String, canal: String, file: Array[Byte], nameFile: String, eof: Boolean) extends WebSocketEvent
	final case class ChatMessage(text: String) extends WebSocketEvent

	/******** Vamos a empezar a definir las acciones que habrá sobre los reportes					**/
	//final case class GetReporte(criteria: Map[String, Any]) extends WebSocketEvent
	final case class GetReporte(criteria: Seq[CriteriaPedidos]) extends WebSocketEvent
}

case class Fechas(fecha: String)

case class CriteriaPedido(no_pedido: String, compra: String, ejercicio: Int)


sealed trait Father {
  val id: String
  val descripcion: Option[String]
}

/*object Event extends Enumeration {
	val Found, Saved, Inserted, Deleted, NotFound = Value
}*/

case class Deleted(num: Int)

case class Programa( id: String = "",
                     descripcion: Option[String] = None,
                     destino: String = "",
                     depto: String = "1221",
                     mostrar: Boolean = true,
                     rfc_dependencia: String = "SES890417TX8",
                     nivel: String = "ESTATAL",
                     encargado: Option[String] = Some("PENDIENTE"),
                     activo: Boolean = true,
                     fuente_financiamiento: String = "") extends Father


case class Oficina(	 id: String = "",
										 descripcion: Option[String] = None,
										 firma: Option[String] = None,
										 cargo: Option[String] = None
                  ) extends Father

case class Fuente(	id: String = "",
										descripcion: Option[String] = None,
										observaciones: Option[String] = None,
										rfc_dependencia: String  = "SES890417TX8",
										nivel: Option[String] = None
								 ) extends Father

case class Articulo (
						id: String = "",
	          descripcion: Option[String] = None,
						unidad: String = "",
						presentacion: Option[Int] = None,
						unid_med_pres: Option[String] = None,
						partida: String = "",
						cabms: Option[String] = None,
						cb: Option[Boolean] = None,
						iva: Option[Double] = None,
						baja: Option[Boolean] = None	) extends Father

case class Partida (	id: String = "",
                      descripcion: Option[String] = None,
											observaciones: Option[String] = None,
	                    presupuesto: Option[Double] = None,
                      activo: Option[Boolean] = None) extends Father

case class Presentacion(	id: String = "",
													descripcion: Option[String] = None,	//unidad se repite con fines practicos
                       		presentacion: Option[Double] = None,
                       		unidad_present: Option[String] = None) extends Father

case class Usuarios(
							usuario: String = "",
							contraseña: String = "",
							tipo: String = "",
							nombre: String = "",
							area: String = "",
							activo: Boolean = true,
							nivel: Int = 0)

case class Proveedor(
					id: String = "",
					descripcion: Option[String] = None,
					propietario: Option[String] = None,
					calle: Option[String] = None,
					colonia: Option[String] = None,
					delegacion: Option[String] = None,
					cp: Option[String] = None,
					ciudad: Option[String] = None,
					telefonos: Option[String] = None,
					fax: Option[String] = None,
					observaciones: Option[String] = None,
					activo: Boolean = true,
					elaboro: Option[String] = None,
					giro: Option[String] = None,
					descuento: Option[String] = None) extends Father


case class IdPedido(ejercicio: Int, compra: String = "PEDI", no_pedido: String)

case class ReportePedidoDetalles (
										 	no_pedido: String,             		//no_pedido             | character varying(6)
											compra: String,                		//compra                | character varying(5)
											tipo_compra: String,							//tipo_compra           | character varying(50)
											no_sol_compra: String,						//no_sol_compra         | character varying(50)
											fecha_pedido: Fechas,							//fecha_pedido          | date
											fecha_entrega: String,						//fecha_entrega         | character varying(35)
											elaboro: String,									//elaboro               | character varying(10)
											cve_depto: String,								//cve_depto             | character varying(4)
											destino: String,									//destino               | character varying(1000)
											cve_provedor: String,							//cve_provedor          | character varying(13)
											cancelado: Boolean,								//cancelado             | boolean
											razon_social: String,							//razon_social          | character varying(250)
											cve_presup: String,								//cve_presup            | character varying(13)
											cve_articulo: String,							//cve_articulo          | character varying(12)
											descripcion: String,							//descripcion           | character varying(250000)
											unidad: String,										//unidad                | character varying(15)
											presentacion: Int,								//presentacion          | integer
											unid_med_pres: String,						//unid_med_pres         | character varying(15)
											cantidad: Int,										//cantidad              | integer
											precio: Double,										//precio                | double precision
											partida: String,									//partida               | character varying(6)
											descripcion_partida: String,			//descripcion_partida   | character varying(5000)
											tipo_partida: String,							//tipo_partida          | text
											subtotal: Double,              		//| double precision          |           |          |
											iva: Double,											//iva                   | double precision
											total: Double,										//total                 | double precision
											prog: String,											//prog                  | text
											ejercicio: Int,										//ejercicio             | integer
											id_comparativo: String,						//id_comparativo        | character varying(50)
											ejercicio_presupuesto: Int,				//ejercicio_presupuesto | integer
											fuente: String,										//fuente                | character varying(50)
											descripcion_fuente: String,				//descripcion_fuente    | character varying(1000)
											programa: String,									//programa              | character varying(13)
											descripcion_programa: String			//descripcion_programa  | character varying(1000)
)

case class DatosGralesPedido(	//zickaprpts.qry_datosgrales_pedidos2
								no_pedido: String,							//no_pedido             | character varying(6)    |           |          |
								compra: String,									//compra                | character varying(5)    |           |          |
								tipo_compra: String,						//tipo_compra           | character varying(50)   |           |          |
								no_sol_compra: String,					//no_sol_compra         | character varying(50)   |           |          |
								fecha_pedido: Fechas,						//fecha_pedido          | date                    |           |          |
								fecha_entrega: String,					//fecha_entrega         | character varying(35)   |           |          |
								elaboro: String,								//elaboro               | character varying(10)   |           |          |
								capturo: String,								//capturo               | character varying(15)   |           |          |
								requerimiento: String,					//requerimiento         | character varying(50)   |           |          |
								area: String,										//area                  | text                    |           |          |
								destino: String,								//destino               | character varying(1000) |           |          |
								proveedor: String,							//proveedor             | text                    |           |          |
								programa: String,								//programa              | text                    |           |          |
								ejercicio: Int,									//ejercicio             | integer                 |           |          |
								id_comparativo: String,					//id_comparativo        | character varying(50)   |           |          |
								cancelado: Boolean, 						//cancelado             | boolean                 |           |          |
								obsercancel: String,						//obsercancel           | character varying(100)  |           |          |
								ejercicio_presupuesto: String,	//ejercicio_presupuesto | integer                 |           |          |
								fuente: String,									//fuente                | text                    |           |
)

case class Reporte(items: Seq[DatosGralesPedido] = Seq.empty[DatosGralesPedido])
