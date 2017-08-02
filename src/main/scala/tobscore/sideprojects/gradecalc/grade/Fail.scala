package tobscore.sideprojects.gradecalc.grade

/**
  * Created by Tobias Kerst on 06.03.17.
  */
case class Fail() extends FailPass {
  def pass = false
}
