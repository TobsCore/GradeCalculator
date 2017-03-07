package tobscore.sideprojects.gradecalc.grade

import tobscore.sideprojects.gradecalc.Subject

/**
  * Created by Tobias Kerst on 07.03.17.
  */
case class CalculatedGrade(grade: Int, weight: Int) extends Grade {
  def this(subject: Subject[ExplicitGrade]) = this(subject.result.get.grade, subject.weight)

  val realGrade = Math.round(grade / 3)

  def get: Double = realGrade / weight

}
