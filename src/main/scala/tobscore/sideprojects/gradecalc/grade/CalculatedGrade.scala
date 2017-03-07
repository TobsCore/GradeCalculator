package tobscore.sideprojects.gradecalc.grade

import tobscore.sideprojects.gradecalc.Subject

/**
  * Created by Tobias Kerst on 07.03.17.
  */
case class CalculatedGrade(grade: Double, weight: Int) extends Grade {
  def this(subject: Subject[ExplicitGrade]) = this(subject.result.get.grade, subject.weight)

  def get(): Double = grade / weight

}
