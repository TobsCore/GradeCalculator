package tobscore.sideprojects.gradecalc.grade

/**
  * Created by Tobias Kerst on 06.03.17.
  */
trait PassFail {

  def isPass: Option[Boolean]
  def isFail: Option[Boolean]

}
