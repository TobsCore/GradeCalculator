package tobscore.sideprojects.gradecalc.controller

import javafx.css.PseudoClass

import scalafx.Includes._
import scalafx.scene.control.{Alert, Button, ComboBox, TextField}
import javafx.stage.Stage

import com.typesafe.scalalogging.Logger
import tobscore.sideprojects.gradecalc.MutableSubject
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
  val logger = Logger(classOf[AddSubjectDialogController])
  val errorStyle = PseudoClass.getPseudoClass("error")

  subjectIdentifier.text.addListener((_, _, subjectIdentifiertText) => {
    accept.disable() = subjectIdentifiertText.length <= 0
  })

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

  subjectGradeWeight.text.addListener((_, previousWeight, weight) => {
    try {
      val weightValue = Integer.parseInt(weight)
      if (weightValue >= 0 && weightValue < 100) {
        subjectGradeWeight.pseudoClassStateChanged(errorStyle, false)
      } else {
        subjectGradeWeight.pseudoClassStateChanged(errorStyle, true)
      }
    } catch {
      case e => subjectGradeWeight.pseudoClassStateChanged(errorStyle, true)
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

    def createSubject(): MutableSubject[Grade] = {
      if (gradeType.equalsIgnoreCase("Benotung")) {
        val grade: Option[Grade] = if (subjectGrade.text().length == 0) {
          None
        } else {
          Some(new Grade(subjectGrade.text().toDouble))
        }
        MutableSubject[Grade](subjectName, grade, weight.get)
      } else {
        //TODO: Implement Fail Pass subject
        //Subject[FailPass](subjectName, None, weight.get)
        logger.error("The method to add a subject that is only fail/pass is not implemented, yet")
        MutableSubject[Grade](subjectName, None, weight.get)
      }
    }

    try {
      val subject = createSubject()
      controller.getOrElse(throw new IllegalStateException("No remote controller defined")).addSubject(subject)
    } catch {
      case e => {
        new Alert(AlertType.Error, s"${subjectGrade.text()} is not a valid grade").showAndWait()
        return
      }
    }

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
