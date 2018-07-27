package tobscore.sideprojects.gradecalc

import tobscore.sideprojects.gradecalc.grade.Passable

import scalafx.beans.property.{BooleanProperty, IntegerProperty, StringProperty}

case class Subject[T <: Passable](name: String, result: Option[T], weight: Int = 1, attempts: Int = 1, instructor: Option[Professor], isFinished: Boolean) {
  def toMutableSubject: MutableSubject[T] = {
    new MutableSubject[T](StringProperty(name), result, IntegerProperty(weight), IntegerProperty(attempts), instructor, BooleanProperty(isFinished))
  }

}
