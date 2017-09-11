package tobscore.sideprojects.gradecalc

import tobscore.sideprojects.gradecalc.grade.Passable

import scalafx.beans.property.{BooleanProperty, IntegerProperty, StringProperty}

case class Subject[T <: Passable](val name: String, val result: Option[T], val weight: Int = 1, val attempts: Int = 1, val instructor: Option[Professor], val isFinished: Boolean) {
  def toMutableSubject: MutableSubject[T] = {
    new MutableSubject[T](StringProperty(name), result, IntegerProperty(weight), IntegerProperty(attempts), instructor, BooleanProperty(isFinished))
  }

}
