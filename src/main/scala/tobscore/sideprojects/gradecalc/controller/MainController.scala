package tobscore.sideprojects.gradecalc.controller

import java.io.IOException
import javafx.scene.layout.GridPane
import javafx.{scene => jfxs}

import com.typesafe.scalalogging.Logger
import tobscore.sideprojects.gradecalc.{Semester, Subject}
import tobscore.sideprojects.gradecalc.grade.{Grade, Passable}

import scala.collection.mutable.ListBuffer
import scalafx.Includes._
import scalafx.scene.control.{Label, TextField}
import scalafx.scene.layout.VBox
import scalafx.stage.{Modality, Stage}
import scalafxml.core.macros.sfxml
import scalafxml.core.{FXMLLoader, FXMLView, NoDependencyResolver}

trait MainControllerInterface {
  def addSubject(subject: Subject[Grade]): Unit

  def updateResults(): Unit
}

/**
  * Created by Tobias Kerst on 08.03.17.
  */
@sfxml
class MainController(val subjectList: VBox,
                     val resultLabel: Label) extends MainControllerInterface {

  val logger = Logger(classOf[MainController])
  val semester: Semester = Semester(1)

  def openAboutDialog(): Unit = {
    val aboutDialogFXML: String = "/AboutDialog.fxml"
    val resource = getClass.getResource(aboutDialogFXML)
    if (resource == null) {
      throw new IOException(s"Cannot load $aboutDialogFXML")
    }
    val root = FXMLView(resource, NoDependencyResolver)

    val aboutStage = new Stage() {
      title = "About GradeCalculator"
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


  def displayNewSubjectDialog(): Unit = {
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

  override def addSubject(subject: Subject[Grade]): Unit = {
    def populateView(): Unit = {
      val label = new Label()
      label.text() = subject.name()

      val customControl: String = "/SubjectListElement.fxml"
      val resource = getClass.getResource(customControl)
      if (resource == null) {
        throw new IOException(s"Cannot load $customControl")
      }
      val loader = new FXMLLoader(resource, NoDependencyResolver)
      loader.load()
      val root = loader.getRoot[jfxs.Parent].asInstanceOf[GridPane]
      val controller = loader.getController[SubjectListElementControllerInterface]
      controller.setMainController(this)
      controller.setModel(subject)

      subjectList.children.add(root)
    }


    logger.info(s"Adding subject ${subject.name}")
    populateView()
    semester += subject
    updateResults()
  }

  override def updateResults(): Unit = {
    logger.trace("Updating the grade")
    resultLabel.text() = semester.result().getOrElse("Not set").toString
  }
}
