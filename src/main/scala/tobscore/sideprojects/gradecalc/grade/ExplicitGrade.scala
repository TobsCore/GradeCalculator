package tobscore.sideprojects.gradecalc.grade

/**
  * Created by Tobias Kerst on 05.03.17.
  */
case class ExplicitGrade(val grade: Double) extends Grade {
  require(List(1.0, 1.3, 1.7, 2.0, 2.3, 2.7, 3.0, 3.3, 3.7, 4.0, 4.3, 4.7, 5.0).contains(grade), s"Grade $grade must be between 1.0 and 5.0")
}
