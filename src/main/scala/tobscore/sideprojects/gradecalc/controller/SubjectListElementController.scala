package tobscore.sideprojects.gradecalc.controller

import javafx.css.PseudoClass

import tobscore.sideprojects.gradecalc.Subject
import tobscore.sideprojects.gradecalc.grade.{GradeMatcher, Passable}

import scalafx.Includes._
import scalafx.scene.control._
import scalafxml.core.macros.sfxml

trait SubjectListElementControllerInterface {
  def setModel(model: Subject[_ <: Passable])
  def setMainController(controller: MainControllerInterface)
}

@sfxml
class SubjectListElementController(val subjectLabel: Label,
                                   val subjectGrade: TextField,
                                   val elementMenu: MenuButton,
                                   val subjectFinished: CheckBox) extends SubjectListElementControllerInterface {

  var subject: Subject[_ <: Passable] = _
  var mainController: MainControllerInterface = _

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
    println(s"Editing entry ${subjectLabel.text()}")
  }

  def deleteEntry(): Unit = {
    println(s"Deleting entry ${subjectLabel.text()}")
  }

  def toggleFinished(): Unit = {
  }

  override def setModel(model: Subject[_ <: Passable]): Unit = {
    subject = model
    subjectLabel.text <==> subject.name
    subjectFinished.selected <==> subject.isFinished
    subjectGrade.text() = subject.result.getOrElse("").toString
  }

  override def setMainController(controller: MainControllerInterface): Unit = {
    mainController = controller
  }
}
