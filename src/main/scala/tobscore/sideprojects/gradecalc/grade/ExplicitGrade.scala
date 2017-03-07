package tobscore.sideprojects.gradecalc.grade

/**
  * Created by Tobias Kerst on 05.03.17.
  */
case class ExplicitGrade(grade: Int) extends Grade {
  require(grade >= 3 && grade <= 15, s"Grade $grade must be between 3 and 15")

  def this(grade: Double) = {
    this(Math.round(grade * 3.0).toInt)
    require(ExplicitGrade.values.contains(grade), s"Grade $grade must be between 1.0 and 5.0")
  }

  def get: Double = {
    BigDecimal(grade / 3).setScale(1, BigDecimal.RoundingMode.HALF_UP).toDouble
  }
}

object ExplicitGrade {
  def apply(grade: Double): ExplicitGrade = new ExplicitGrade(grade)


  val values = List(1.0, 1.3, 1.7, 2.0, 2.3, 2.7, 3.0, 3.3, 3.7, 4.0, 4.3, 4.7, 5.0)
}
