package tobscore.sideprojects.gradecalc.grade

/**
  * Created by Tobias Kerst on 06.03.17.
  */
case class Fail() extends PassFail {
  val pass: Boolean = false

  override def isPass(): Boolean = pass
}
