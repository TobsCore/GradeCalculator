package tobscore.sideprojects.gradecalc

import tobscore.sideprojects.gradecalc.grade.ExplicitGrade

/**
  * Created by Tobias Kerst on 05.03.17.
  */
object RunApplication {

  def main(args:Array[String]): Unit = {
    val infoGrade = ExplicitGrade(1.7)
    println(infoGrade)
  }
}
