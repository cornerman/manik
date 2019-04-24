package postg

import spatutorial.shared._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
//import play.api.libs.concurrent.Execution.Implicits.defaultContext


import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider

import play.db.NamedDatabase
//import slick.driver.JdbcProfile
import slick.jdbc.JdbcProfile

//import java.text.SimpleDateFormat;
//import java.sql.Date
//import java.util._
//import boopickle.Default._

//import spatutorial.shared.Fechas

class CUsuarios  extends CMyDataBase
  //@Inject()( @NamedDatabase("sicap") protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile]
{

  import jdbcProfile.api._
  //import profile.api._  //original

  private val query = TableQuery[tabla]

  private class tabla(tag: Tag) extends Table[Usuarios](tag, "cuentas_de_usuarios") {
    def usuario               = column[String]("usuario")
    def contraseña            = column[String]("contraseña")
    def tipo                  = column[String]("tipo")
    def nombre                = column[String]("nombre")
    def area                  = column[String]("area")
    def activo                = column[Boolean]("activo")
    def nivel                 = column[Int]("nivel")

    override def * = (usuario, contraseña, tipo, nombre, area, activo, nivel)<> (Usuarios.tupled, Usuarios.unapply)
  }

  def ListAll: Future[Seq[Usuarios]] = db.run(query.result)

  def ById(user: String): Future[Seq[Usuarios]] = {
    val Search = query.filter(_.usuario === user)
    db.run( Search.result )
  }

  def logear(item: Usuarios): Future[(Usuarios, String)] =
    {
      db.run(query.filter(it =>
        it.usuario === item.usuario && it.contraseña === item.contraseña).result.map((p =>
          if(p.isEmpty) (Usuarios(), "not found")
          else (p.head, "found")
        )))
    }

    /*db.run(query.filter( it =>
      it.usuario === item.usuario
        &&
        it.contraseña === item.contraseña).result.map( p => (p, "found")))*/

}