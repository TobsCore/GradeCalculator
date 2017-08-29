package tobscore.sideprojects.gradecalc

import tobscore.sideprojects.gradecalc.grade.Passable

import scalafx.beans.property.{BooleanProperty, IntegerProperty, StringProperty}

/**
  * Created by Tobias Kerst on 06.03.17.
  */
case class Subject[T <: Passable](var name: StringProperty, var result: Option[T], var weight: IntegerProperty, var attempts: IntegerProperty, var instructor: Option[Professor], var isFinished: BooleanProperty) {

  def isGraded(): Option[Boolean] = result match {
    case Some(result) => Some(result.isGraded)
    case None => None
  }

  require(weight() >= 0, "Weight cannot be less than 0")
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

object Subject {
  def apply[T <: Passable](name: String, result: Option[T], weight: Int = 1, attempts: Int = 1, professor: Option[Professor], isFinished: Boolean): Subject[T] =
    new Subject[T](StringProperty(name), result, IntegerProperty(weight), IntegerProperty(attempts), professor, BooleanProperty(isFinished))

  def apply[T <: Passable](name: String, result: Option[T]): Subject[T] = apply(name, result, 1, 1, None, false)
  def apply[T <: Passable](name: String, result: Option[T], weight: Int): Subject[T] = apply(name, result, weight, 1, None, false)


}
