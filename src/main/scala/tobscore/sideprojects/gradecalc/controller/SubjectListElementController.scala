package tobscore.sideprojects.gradecalc.controller

import scalafx.Includes._
import scalafx.scene.control._
import scalafx.util.StringConverter
import scalafxml.core.macros.sfxml

@sfxml
class SubjectListElementController(val subjectLabel: Label,
                                   val subjectGrade: TextField,
                                   val elementMenu: MenuButton,
                                   val subjectFinished: CheckBox) {

  subjectFinished.selected <==> subjectGrade.disable
  subjectGrade.text.addListener((_, previousGradeText, gradeText) => {
    if (gradeText.length > 3) {
      subjectGrade.text() = previousGradeText
    }
  })

  def editEntry(): Unit = {
    println(s"Deleting entry ${subjectLabel.text()}")
  }

  def deleteEntry(): Unit = {
    println(s"Deleting entry ${subjectLabel.text()}")
  }

  def toggleFinished(): Unit = {
    println(s"Toggles finished state of ${subjectLabel.text()}")
  }

}
