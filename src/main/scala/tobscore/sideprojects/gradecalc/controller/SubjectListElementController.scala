package tobscore.sideprojects.gradecalc.controller

import javafx.css.PseudoClass
import javafx.{scene => jfxs}

import tobscore.sideprojects.gradecalc.grade.GradeMatcher

import scalafx.Includes._
import scalafx.scene.control._
import scalafxml.core.macros.sfxml

@sfxml
class SubjectListElementController(val subjectLabel: Label,
                                   val subjectGrade: TextField,
                                   val elementMenu: MenuButton,
                                   val subjectFinished: CheckBox) {

  subjectFinished.selected <==> subjectGrade.disable
  subjectGrade.text.addListener((_, previousGradeText, gradeText) => {
    val errorStyle = PseudoClass.getPseudoClass("error")

    if (gradeText.length > 3) {
      subjectGrade.text() = previousGradeText
    } else if (gradeText.length == 0) {
      subjectGrade.pseudoClassStateChanged(errorStyle, false)
    } else if (!GradeMatcher(gradeText).isCorrect()) {
      subjectGrade.pseudoClassStateChanged(errorStyle, true)
    } else {
      subjectGrade.pseudoClassStateChanged(errorStyle, false)
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
