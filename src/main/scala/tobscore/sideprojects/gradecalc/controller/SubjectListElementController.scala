package tobscore.sideprojects.gradecalc.controller

import javafx.css.PseudoClass
import javafx.scene.input.{KeyCode, KeyEvent}

import com.typesafe.scalalogging.Logger
import sun.plugin2.main.client.MessagePassingExecutionContext
import tobscore.sideprojects.gradecalc.Subject
import tobscore.sideprojects.gradecalc.grade.{Grade, GradeMatcher, Passable}

import scalafx.Includes._
import scalafx.scene.control._
import scalafxml.core.macros.sfxml

trait SubjectListElementControllerInterface {
  def setModel(model: Subject[Grade])

  def setMainController(controller: MainControllerInterface)
}

@sfxml
class SubjectListElementController(val subjectLabel: Label,
                                   val subjectGrade: TextField,
                                   val elementMenu: MenuButton,
                                   val subjectPassing: Label,
                                   val subjectFinished: CheckBox) extends SubjectListElementControllerInterface {

  val logger = Logger(classOf[SubjectListElementController])
  var subject: Subject[Grade] = _
  var mainController: MainControllerInterface = _
  val errorStyle = PseudoClass.getPseudoClass("error")
  val failingStyle = PseudoClass.getPseudoClass("failing")

  subjectFinished.selected <==> subjectGrade.disable
  subjectGrade.text.addListener((_, previousGradeText, gradeText) => {

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

  def updatePassing(passing: Boolean): Unit = if (passing) {
    subjectPassing.text() = "Bestanden"
    subjectPassing.pseudoClassStateChanged(failingStyle, false)
  } else {
    subjectPassing.text() = "Nicht bestanden"
    subjectPassing.pseudoClassStateChanged(failingStyle, true)
  }


  def gradeValueKeyPress(event: KeyEvent): Unit = {
    val key = event.getCode()

    key match {
      case KeyCode.ENTER | KeyCode.TAB => {
        if (GradeMatcher(subjectGrade.text()).isCorrect()) {
          val gradeValue = subjectGrade.text().toDouble
          val grade = Grade(gradeValue)
          subject.result = Some(grade)
          mainController.updateResults()
          updatePassing(grade.isPass)
        } else if (subjectGrade.text().length == 0) {
          subject.result = None
          mainController.updateResults()
          updatePassing(false)
        }
      }
      case _ => {}
    }
  }

  def editEntry(): Unit = {
    println(s"Editing entry ${subjectLabel.text()}")
  }

  def deleteEntry(): Unit = {
    println(s"Deleting entry ${subjectLabel.text()}")
  }

  def toggleFinished(): Unit = {
  }

  override def setModel(model: Subject[Grade]): Unit = {
    subject = model
    subjectLabel.text <==> subject.name
    subjectFinished.selected <==> subject.isFinished
    subjectGrade.text() = subject.result.getOrElse("").toString
    updatePassing(subject.isPass.getOrElse(false))
  }

  override def setMainController(controller: MainControllerInterface): Unit = {
    mainController = controller
  }
}
