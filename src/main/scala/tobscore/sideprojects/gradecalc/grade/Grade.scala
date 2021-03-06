package tobscore.sideprojects.gradecalc.grade

/**
  * Created by tobscore on 07.03.17.
  */
case class Grade(var grade: Int) extends Passable {
  require(grade >= 10 && grade <= 50, s"Grade must be between 1.0 and 5.0 - actual $grade")

  def this(grade: Double) = this((grade * 10).toInt)

  override def isPass: Boolean = {
    grade <= 40
  }

  override def toString: String = {
    get().toString
  }

  def get(): Double = grade / 10.0

  override def isGraded = true
}

object Grade {
  def apply(grade: Double): Grade = new Grade(grade)
}
