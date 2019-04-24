package postg

import scala.util.Success
import slick.jdbc.PostgresProfile

import com.typesafe.config.ConfigFactory

class CMyDataBase {

	val password = ConfigFactory.load().getString("password")
	val usuario = ConfigFactory.load().getString("usuario")

	val jdbcProfile = PostgresProfile

	import jdbcProfile.api._

	val connectionUrl = s"jdbc:postgresql://localhost/sicap?user=$usuario&password=$password"

	val db = Database.forURL(connectionUrl, driver = "org.postgresql.Driver")

	import scala.util.Try

	Try(db.createSession.conn) match {
		case Success(con) =>	con.close
		case fail         => 	println("Didn't connect")
	}

}