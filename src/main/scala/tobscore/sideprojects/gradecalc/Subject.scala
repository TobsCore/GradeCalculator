package tobscore.sideprojects.gradecalc

import tobscore.sideprojects.gradecalc.grade.PassFail

/**
  * Created by Tobias Kerst on 06.03.17.
  */
case class Subject[T <: PassFail](name: String, var result: Option[T] = None, weight: Int = 1, attempts: Int = 1, var instructor: Option[Professor] = None) extends PassFail {
  require(weight >= 0, "Weight cannot be less than 0")
  var identifier: String = _

  override def isPass: Option[Boolean] = result match {
    case None => None
    case Some(pass) => result.get.isPass
  }

  override def isFail: Option[Boolean] = result match {
    case None => None
    case Some(pass) => result.get.isFail
  }
}
