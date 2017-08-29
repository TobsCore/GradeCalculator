package tobscore.sideprojects.gradecalc.controller

import javafx.css.PseudoClass

import scalafx.Includes._
import scalafx.scene.control.{Alert, Button, ComboBox, TextField}
import javafx.stage.Stage

import tobscore.sideprojects.gradecalc.Subject
import tobscore.sideprojects.gradecalc.grade.{FailPass, Grade, GradeMatcher, Passable}

import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control._
import scalafxml.core.macros.sfxml

trait MainControllerReceiver {
  def initController(controller: MainControllerInterface)
}

@sfxml
class AddSubjectDialogController(val subjectIdentifier: TextField,
                                 val subjectGradeLabel: Label,
                                 val subjectGrade: TextField,
                                 val subjectPassLabel: Label,
                                 val subjectPass: CheckBox,
                                 val subjectGradeWeight: TextField,
                                 val subjectType: ComboBox[String],
                                 val cancel: Button,
                                 val accept: Button) extends MainControllerReceiver {

  var controller: Option[MainControllerInterface] = None

  subjectIdentifier.text.addListener((_, _, subjectIdentifiertText) => {
    accept.disable() = subjectIdentifiertText.length <= 0
  })

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

  subjectPass.visible <==> subjectPassLabel.visible
  subjectGrade.visible <==> subjectGradeLabel.visible
  subjectGrade.visible.delegate.bind(subjectPass.visible.not)

  subjectType.value.addListener((_, _, selectedSubject) => {
    if (selectedSubject.equals("Benotung")) {
      subjectPass.setVisible(false)

    } else {
      subjectPass.setVisible(true)
    }
  })

  def addSubject(): Unit = {
    val gradeType: String = subjectType.getValue
    val subjectName: String = subjectIdentifier.text()
    val weight: Option[Int] = try {
      Some(subjectGradeWeight.text().toInt)
    } catch {
      case _: NumberFormatException => None
    }

    if (weight.isEmpty) {
      new Alert(AlertType.Warning, s"'${subjectGradeWeight.text()}' is not a valid number.").showAndWait()
      return
    }

    def createSubject(): Subject[_ <: Passable] = {
      if (gradeType.equalsIgnoreCase("Benotung")) {
        val grade: Option[Grade] = if (subjectGrade.text().length == 0) {
          None
        } else {
          Some(new Grade(subjectGrade.text().toDouble))
        }
        Subject[Grade](subjectName, grade, weight.get)
      } else {
        Subject[FailPass](subjectName, None, weight.get)
      }
    }

    val subject = createSubject()
    controller.getOrElse(throw new IllegalStateException("No remote controller defined")).addSubject(subject)

    quitDialog()
  }

  def quitDialog(): Unit = {
    val stage: Stage = subjectIdentifier.getScene.getWindow.asInstanceOf[Stage]
    stage.close()
  }

  override def initController(controller: MainControllerInterface): Unit = {
    this.controller = Some(controller)
  }
}
