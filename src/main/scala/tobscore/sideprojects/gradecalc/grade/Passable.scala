package tobscore.sideprojects.gradecalc.grade

/**
  * Created by Tobias Kerst on 06.03.17.
  */
trait Passable {

  def isPass: Boolean
  def isFail: Boolean = !isPass

}
