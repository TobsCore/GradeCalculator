package tobscore.sideprojects.gradecalc.controller

import javafx.css.PseudoClass

import scalafx.Includes._
import scalafx.scene.control.{Alert, Button, ComboBox, TextField}
import javafx.stage.Stage

import com.typesafe.scalalogging.Logger
import tobscore.sideprojects.gradecalc.MutableSubject
import tobscore.sideprojects.gradecalc.grade.{FailPass, Grade, GradeMatcher, Passable}

import scalafx.beans.property.{BooleanProperty, IntegerProperty, StringProperty}
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control._
import scalafxml.core.macros.sfxml

trait MainControllerReceiver {
  def initController(controller: MainControllerInterface): Unit

  def initController(controller: SubjectListElementControllerInterface): Unit
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

  var mainController: Option[MainControllerInterface] = None
  var elementController: Option[SubjectListElementControllerInterface] = None
  val logger = Logger(classOf[AddSubjectDialogController])
  val errorStyle = PseudoClass.getPseudoClass("error")
  val subject = new MutableSubject[Grade](StringProperty(""), None, weight = IntegerProperty(1), IntegerProperty(0), None, BooleanProperty(false))
  subject.name <==> subjectIdentifier.text

  subjectGrade.text.addListener((_, previousGradeText, gradeText) => {
    if (gradeText.length > 3) {
      subjectGrade.text() = previousGradeText
    } else {
      val gradeIsCorrect = if (gradeText.length == 0) {
        true
      } else if (!GradeMatcher(gradeText).isCorrect()) {
        false
      } else {
        true
      }
      subjectGrade.pseudoClassStateChanged(errorStyle, !gradeIsCorrect)
      accept.disable() = !gradeIsCorrect

      subject.result = if (gradeIsCorrect && gradeText.nonEmpty) {
        Some(Grade(gradeText.toDouble))
      } else {
        None
      }
    }
  })

  subjectGradeWeight.text.addListener((_, previousWeight, weight) => {
    val weightIsCorrect = try {
      val weightValue = Integer.parseInt(weight)
      if (weightValue > 0 && weightValue < 100) {
        true
      } else {
        false
      }
    } catch {
      case e: Exception => false
    }
    subjectGradeWeight.pseudoClassStateChanged(errorStyle, !weightIsCorrect)
    accept.disable() = !weightIsCorrect

    subject.weight = if (weightIsCorrect && weight.nonEmpty) {
      IntegerProperty(weight.toInt)
    } else {
      IntegerProperty(1)
    }
  })

  subjectIdentifier.text.addListener((_, _, subjectIdentifierText) => {
    accept.disable() = subjectIdentifierText.length <= 0
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
    mainController.getOrElse(throw new IllegalStateException("No remote controller defined")).addSubject(subject)
    //mainController.getOrElse(throw new IllegalStateException("No remote controller defined")).addSubject(MutableSubject[Grade]("Hello World", Some(Grade(2.3))))
    quitDialog()
  }

  def quitDialog(): Unit = {
    val stage: Stage = subjectIdentifier.getScene.getWindow.asInstanceOf[Stage]
    stage.close()
  }

  override def initController(controller: MainControllerInterface): Unit = {
    this.mainController = Some(controller)
  }

  override def initController(controller: SubjectListElementControllerInterface): Unit = {
    this.elementController = Some(controller)
  }
}
