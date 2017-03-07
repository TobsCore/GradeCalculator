package tobscore.sideprojects.gradecalc.grade

/**
  * Created by tobscore on 07.03.17.
  */
trait Grade extends PassFail {
  def grade: Double
  require(grade >= 1.0 && grade <= 5.0)

  override def isPass(): Option[Boolean] = {
    Some(grade <= 4.0)
  }

  override def toString(): String = {
    grade.toString
  }

  override def isFail(): Option[Boolean] = {
    Some(grade > 4.0)
  }
}
