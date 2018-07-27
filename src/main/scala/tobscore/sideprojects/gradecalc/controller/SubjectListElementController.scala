package tobscore.sideprojects.gradecalc.controller

import java.io.IOException

import com.typesafe.scalalogging.Logger
import javafx.css.PseudoClass
import javafx.scene.input.{KeyCode, KeyEvent}
import javafx.{scene => jfxs}
import scalafx.Includes._
import scalafx.scene.control._
import scalafx.stage.{Modality, Stage}
import scalafxml.core.macros.sfxml
import scalafxml.core.{FXMLLoader, NoDependencyResolver}
import tobscore.sideprojects.gradecalc.MutableSubject
import tobscore.sideprojects.gradecalc.grade.{Grade, GradeMatcher}

trait SubjectListElementControllerInterface {
  def setModel(model: MutableSubject[Grade])

  def setMainController(controller: MainControllerInterface)
}

trait SubjectListElementControllerIntertface {
  def receiveUpdatedSubject(subject: MutableSubject[Grade]): Unit
}

@sfxml
class SubjectListElementController(val subjectLabel: Label,
                                   val subjectGrade: TextField,
                                   val elementMenu: MenuButton,
                                   val subjectPassing: Label,
                                   val subjectFinished: CheckBox)
    extends SubjectListElementControllerInterface {

  val logger = Logger(classOf[SubjectListElementController])
  var subject: MutableSubject[Grade] = _
  var mainController: MainControllerInterface = _
  val errorStyle: PseudoClass = PseudoClass.getPseudoClass("error")
  val failingStyle: PseudoClass = PseudoClass.getPseudoClass("failing")

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

  def updatePassing(passing: Option[Boolean]): Unit = passing match {
    case Some(pass) =>
      if (pass) {
        subjectPassing.text() = "Bestanden"
        subjectPassing.pseudoClassStateChanged(failingStyle, false)
      } else {
        subjectPassing.text() = "Nicht bestanden"
        subjectPassing.pseudoClassStateChanged(failingStyle, true)
      }
    case None => subjectPassing.text() = ""
  }

  def gradeValueKeyPress(event: KeyEvent): Unit = {
    event.getCode match {
      case KeyCode.ENTER | KeyCode.TAB =>
        if (GradeMatcher(subjectGrade.text()).isCorrect()) {
          val gradeValue = subjectGrade.text().toDouble
          val grade = Grade(gradeValue)
          subject.result = Some(grade)
          mainController.updateResults()
          updatePassing(Some(grade.isPass))
        } else if (subjectGrade.text().length == 0) {
          subject.result = None
          mainController.updateResults()
          updatePassing(None)
        }
      case _ =>
    }
  }

  def editEntry(): Unit = {
    logger.info(s"Editing entry ${subjectLabel.text()}")
    val addSubjectDialogFXML: String = "/AddSubjectDialog.fxml"
    val resource = getClass.getResource(addSubjectDialogFXML)
    if (resource == null) {
      throw new IOException(s"Cannot load $addSubjectDialogFXML")
    }
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val controller = loader.getController[MainControllerReceiver]
    controller.initController(this)

    val root = loader.getRoot[jfxs.Parent]

    val aboutStage = new Stage() {
      title = "Add Subject"
      scene = new jfxs.Scene(root)
      resizable = false
      initModality(Modality.ApplicationModal)
    }

    if (!aboutStage.showing()) {
      aboutStage.show()
    } else {
      aboutStage.requestFocus()
    }
  }

  def deleteEntry(): Unit = {
    logger.info(s"Deleting entry ${subjectLabel.text()}")
  }

  def toggleFinished(): Unit = {}

  override def setModel(model: MutableSubject[Grade]): Unit = {
    subject = model
    subjectLabel.text <==> subject.name
    subjectFinished.selected <==> subject.isFinished
    subjectGrade.text() = subject.result.getOrElse("").toString
    updatePassing(subject.isPass)
  }

  override def setMainController(controller: MainControllerInterface): Unit = {
    mainController = controller
  }
}
