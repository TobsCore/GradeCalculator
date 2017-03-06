package tobscore.sideprojects.gradecalc

import tobscore.sideprojects.gradecalc.grade.Grade

/**
  * Created by Tobias Kerst on 05.03.17.
  */
object RunApplication {

  def main(args:Array[String]) = {
    val infoGrade = new Grade(1.7)
    println(infoGrade)
  }
}
