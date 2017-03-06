package tobscore.sideprojects.gradecalc.grade

/**
  * Created by Tobias Kerst on 06.03.17.
  */
case class Pass (val pass: Boolean) extends PassFail {
  override def isPass(): Option[Boolean] = Some(pass)

  override def isFail(): Option[Boolean] = Some(!pass)
}
