package postg

import java.text.SimpleDateFormat

import akka.japi.Option.Some
import org.postgresql.util.{PSQLException}
import spatutorial.shared._
import play.api.mvc.Results._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

import spatutorial.shared.CriteriaPedido._

class CReportePedido extends CMyDataBase {

	import jdbcProfile.api._
	private val query = TableQuery[Tabla]

	implicit val fechasColumnType = MappedColumnType.base[Fechas, java.sql.Date](
		{fecha =>
			val format = new SimpleDateFormat("dd/MM/yyyy")
			val parsed = format.parse(fecha.fecha)
			new java.sql.Date(parsed.getTime());
		},
		{sql =>
			val df = new SimpleDateFormat("dd/MM/yyyy")
			val text = df.format(sql)
			Fechas(fecha = text)
		}
	)

	private class Tabla(tag: Tag) extends Table[DatosGralesPedido](tag, Some("zickaprpts"),"qry_datosgrales_pedidos2") {

		def no_pedido			= column[String]("no_pedido")
		def compra				= column[String]("compra")
		def tipo_compra		=	column[String]("tipo_compra")
		def no_sol_compra	= column[String]("no_sol_compra")
		def fecha_pedido	= column[Fechas]("fecha_pedido")
		def fecha_entrega	= column[String]("fecha_entrega")
		def elaboro				= column[String]("elaboro")
		def capturo				= column[String]("capturo")
		def requerimiento	= column[String]("requerimiento")
		def area					= column[String]("area")
		def destino				= column[String]("destino")
		def proveedor			= column[String]("proveedor")
		def programa			= column[String]("programa")
		def ejercicio				= column[Int]("ejercicio")
		def id_comparativo					= column[String]("id_comparativo")
		def cancelado								= column[Boolean]("cancelado")
		def obsercancel							= column[String]("obsercancel")
		def ejercicio_presupuesto		= column[String]("ejercicio_presupuesto")
		def fuente									= column[String]("fuente")

		def * = (no_pedido, compra, tipo_compra, no_sol_compra, fecha_pedido, fecha_entrega,	elaboro,	capturo, requerimiento,	area,	destino,	proveedor,	programa,	ejercicio, id_comparativo,	cancelado, obsercancel,	ejercicio_presupuesto,	fuente) <> (DatosGralesPedido.tupled, DatosGralesPedido.unapply)
	}

	//def getCondition(criteria: Map[String, Any])(implicit ec: ExecutionContext): Future[Either[String, DatosGralesPedido]] = {
	def getCondition(criteria: Seq[CriteriaPedidos])(implicit ec: ExecutionContext): Future[Either[String, DatosGralesPedido]] = {
		db.run( query.filter { r =>

			r.no_pedido === "10001"

			/*val crit: Seq[Boolean] = criteria.map {
				case pedido: CritPedido => pedido.no_pedido match {
						case "" => true
						case p => r.no_pedido == pedido
					}
				case ejercicio: CritEjercicio => ejercicio.ejercicio match {
						case 0 => true
						case e => r.ejercicio == ejercicio
					}
				case _ => true
			}

			crit.forall(identity)*/
			//almost the same crit.foldLeft(true)(_&&_)

			//val noPedido: String = criteria.getOrElse("no_pedido", "").toString
			//val ejercicio: Int = criteria.getOrElse("ejercicio", 0).toString.toInt
		/*
			val condPedido: slick.lifted.Rep[Boolean] = noPedido match {
																										case "" => true
																										case num => r.no_pedido === noPedido
																									}
			val condEjercicio: slick.lifted.Rep[Boolean] = ejercicio match {
																												case 0 => true
																												case ejercicio => r.ejercicio === ejercicio
																											}*/
		}.result.asTry ).map {
			case Success(value) =>
				if (value.nonEmpty) Right(value.head)
				else Left("Hubo un error")
			case Failure( e: PSQLException ) if e.getSQLState == "23505" =>
				InternalServerError( "Some sort of unique key violation.." )
				Left("La clave ya existe")
			case Failure(e: PSQLException) =>
				InternalServerError( "Some sort of psql error.." )
				Left("Some sort of psql error...")
			case Failure(_) =>
				InternalServerError( "Something else happened.. it was bad.." )
				Left("Quien sabe que paso...")
			case _ =>
				InternalServerError( "Quien sabe que paso ******************************************" )
				Left("Quien sabe que paso...")
		}
	}

	def ById( id: IdPedido)(implicit ec: ExecutionContext): Future[Either[String, DatosGralesPedido]] = {
		db.run ( query.filter { r => r.no_pedido === id.no_pedido && r.compra === id.compra && r.ejercicio === id.ejercicio }
			.result.asTry ).map {
			case Success(value) =>
				if (value.nonEmpty) Right(value.head)
				else Left("Hubo un error")
			case Failure( e: PSQLException ) if e.getSQLState == "23505" =>
				InternalServerError( "Some sort of unique key violation.." )
				Left("La clave ya existe")
			case Failure(e: PSQLException) =>
				InternalServerError( "Some sort of psql error.." )
				Left("Some sort of psql error...")
			case Failure(_) =>
				InternalServerError( "Something else happened.. it was bad.." )
				Left("Quien sabe que paso...")
			case _ =>
				InternalServerError( "Quien sabe que paso ******************************************" )
				Left("Quien sabe que paso...")
		}
	}

}