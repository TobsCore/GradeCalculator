package tobscore.sideprojects.gradecalc

import tobscore.sideprojects.gradecalc.grade.Passable

/**
  * Created by Tobias Kerst on 06.03.17.
  */
case class Subject[T <: Passable](var name: String, var result: Option[T], var weight: Int = 1, var attempts: Int = 1, var instructor: Option[Professor] = None) {
  def isGraded: Option[Boolean] = result match {
    case Some(result) => Some(result.isGraded)
    case None => None
  }

  require(weight >= 0, "Weight cannot be less than 0")
  var identifier: String = _

  def isPass: Option[Boolean] = result match {
    case None => None
    case Some(pass) => Some(pass.isPass)
  }

  def isFail: Option[Boolean] = result match {
    case None => None
    case Some(pass) => Some(pass.isFail)
  }
}
