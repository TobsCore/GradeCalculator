package tobscore.sideprojects.gradecalc.grade

case class GradeMatcher(s: String) {
  val pattern = "\\d(?:\\.\\d)?".r
  private var value: Double = _

  def isCorrect(): Boolean = {
    if (!matches()) {
      false
    }
    value = s.toDouble
    isInRange(value)

  }

  def isInRange(gradeValue: Double): Boolean = gradeValue >= 1.0 && gradeValue <= 5.0

  def matches(): Boolean = s match {
    case pattern() => true
    case _ => false
  }
}
