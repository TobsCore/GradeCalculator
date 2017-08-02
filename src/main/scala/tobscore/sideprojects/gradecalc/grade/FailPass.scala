package tobscore.sideprojects.gradecalc.grade

trait FailPass extends Passable {
  def pass: Boolean

  def isPass: Boolean = pass
}
