package tobscore.sideprojects.gradecalc

import tobscore.sideprojects.gradecalc.grade.Passable

import scalafx.beans.property.{BooleanProperty, IntegerProperty, StringProperty}

/**
  * Created by Tobias Kerst on 06.03.17.
  */
case class MutableSubject[T <: Passable](var name: StringProperty,
                                         var result: Option[T],
                                         var weight: IntegerProperty,
                                         var attempts: IntegerProperty,
                                         var instructor: Option[Professor],
                                         var isFinished: BooleanProperty) {

  def isGraded: Option[Boolean] = result match {
    case Some(res) => Some(res.isGraded)
    case None      => None
  }

  require(weight() > 0, "Weight cannot be less than 1")
  var identifier: String = _

  def isPass: Option[Boolean] = result match {
    case None       => None
    case Some(pass) => Some(pass.isPass)
  }

  def isFail: Option[Boolean] = result match {
    case None       => None
    case Some(pass) => Some(pass.isFail)
  }

  def toSubject: Subject[T] = {
    Subject(name(), result, weight(), attempts(), instructor, isFinished())
  }

  override def hashCode(): Int = name.getValue.hashCode

  override def equals(obj: Any): Boolean = obj match {
    case obj: MutableSubject[_] =>
      name.getValue.equals(obj.name.getValue)
    case _ => false
  }

}

object MutableSubject {

  def apply[T <: Passable](name: String,
                           result: Option[T],
                           weight: Int = 1,
                           attempts: Int = 1,
                           professor: Option[Professor],
                           isFinished: Boolean): MutableSubject[T] =
    new MutableSubject[T](StringProperty(name),
                          result,
                          IntegerProperty(weight),
                          IntegerProperty(attempts),
                          professor,
                          BooleanProperty(isFinished))

  def apply[T <: Passable](name: String, result: Option[T]): MutableSubject[T] =
    apply(name, result, 1, 1, None, isFinished = false)

  def apply[T <: Passable](name: String, result: Option[T], weight: Int): MutableSubject[T] =
    apply(name, result, weight, 1, None, isFinished = false)

}
