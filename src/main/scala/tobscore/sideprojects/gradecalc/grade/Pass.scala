package tobscore.sideprojects.gradecalc.grade

/**
  * Created by Tobias Kerst on 06.03.17.
  */
case class Pass() extends PassFail {
  val pass: Boolean = true

  override def isPass(): Boolean = pass
}
