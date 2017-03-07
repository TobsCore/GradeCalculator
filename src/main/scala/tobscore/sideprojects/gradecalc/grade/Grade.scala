package tobscore.sideprojects.gradecalc.grade

/**
  * Created by tobscore on 07.03.17.
  */
trait Grade extends PassFail {
  def grade: Int

  override def isPass(): Option[Boolean] = {
    Some(grade <= 12)
  }

  override def toString(): String = {
    grade.toString
  }

  override def isFail(): Option[Boolean] = {
    Some(grade > 12)
  }
}
