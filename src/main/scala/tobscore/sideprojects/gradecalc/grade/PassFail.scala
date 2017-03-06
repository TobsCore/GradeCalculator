package tobscore.sideprojects.gradecalc.grade

/**
  * Created by tobscore on 06.03.17.
  */
trait PassFail {

  def isPass(): Boolean
  def isFail(): Boolean = !isPass()

}
