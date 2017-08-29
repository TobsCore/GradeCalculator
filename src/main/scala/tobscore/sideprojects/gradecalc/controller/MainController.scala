package tobscore.sideprojects.gradecalc.controller

import java.io.IOException
import javafx.scene.layout.GridPane
import javafx.{scene => jfxs}

import tobscore.sideprojects.gradecalc.Subject
import tobscore.sideprojects.gradecalc.grade.Passable

import scalafx.Includes._
import scalafx.scene.control.{Label, TextField}
import scalafx.scene.layout.VBox
import scalafx.stage.{Modality, Stage}
import scalafxml.core.macros.sfxml
import scalafxml.core.{FXMLLoader, FXMLView, NoDependencyResolver}

trait MainControllerInterface {
  def addSubject(subject: Subject[_ <: Passable]): Unit
}

/**
  * Created by Tobias Kerst on 08.03.17.
  */
@sfxml
class MainController(val subjectList: VBox) extends MainControllerInterface {

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

  override def addSubject(subject: Subject[_ <: Passable]): Unit = {
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
    val controller = loader.getController[ISubjectListElementController]
    controller.setModel(subject)

    val subjectName: Label = root.lookup("#subjectLabel").asInstanceOf[jfxs.control.Label]
    val subjectGrade: TextField = root.lookup("#subjectGrade").asInstanceOf[jfxs.control.TextField]


    subjectList.children.add(root)
  }
}
