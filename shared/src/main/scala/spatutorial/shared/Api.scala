package spatutorial.shared

import scala.concurrent.Future

trait Api {

  /*************************   Usuarios       ****************************************************/
  def getUsuario(user: String): Future[Seq[Usuarios]]
  def logear(item: Usuarios): Future[(Usuarios, String)]
  /***********************************************************************************************/

}