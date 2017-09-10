package tobscore.sideprojects.gradecalc

import tobscore.sideprojects.gradecalc.grade.Passable

case class Subject[T <: Passable](val name: String, val result: Option[T], val weight: Int = 1, val attempts: Int = 1, val instructor: Option[Professor], val isFinished: Boolean) {

}
