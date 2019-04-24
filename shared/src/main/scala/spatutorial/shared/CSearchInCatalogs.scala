package spatutorial.shared

class CSearchInCatalogs {

  def parts(s: String) = s.split("\\s").toList

  def isContained(as: List[String], bs: List[String]): Boolean = as match {
    case Nil => true
    case a :: atail => bs match {
      case Nil => false
      case b :: btail =>
        if ( (b startsWith a) || (b contains a)) isContained(atail, btail)
        //else if (b contains a) isContained(atail, btail)
        else isContained(as, btail)
    }
  }

  def findInCatalog[T <: Father](key: String, catalog: Seq[T]): Either[String, List[T]] = {
    val s = parts(key)
    if (s.forall(_.isEmpty))
      Left("Key has no content")
    else
      catalog.filter { entry =>
        isContained(s, parts(entry.descripcion.getOrElse("").toString) )
      } match {
        case Nil =>
          Left("key not found")
        //case x :: Nil => Right(x)
        case lots =>
          Right(lots.toList)  //Left(s"ambiguous key: could be any of ${lots.map(_.descripcion) } ")
      }
  }

}
