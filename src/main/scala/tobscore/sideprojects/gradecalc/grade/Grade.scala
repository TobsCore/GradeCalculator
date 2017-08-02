package tobscore.sideprojects.gradecalc.grade

/**
  * Created by tobscore on 07.03.17.
  */
case class Grade(grade: Double) extends PassFail {
  require(grade >= 1.0 && grade <= 5.0, "Grade must be between 1.0 and 5.0")

  override def isPass(): Boolean = {
    grade <= 4.0
  }

  override def toString(): String = {
    grade.toString
  }

  def get(): Double = grade

}

object Grade {
  def apply(grade: Double): Grade = new Grade(grade)
}
