package tobscore.sideprojects.gradecalc.controller

import java.io.{File, IOException}

import com.typesafe.scalalogging.Logger
import javafx.scene.layout.GridPane
import javafx.{scene => jfxs}
import scalafx.Includes._
import scalafx.application.Platform
import scalafx.scene.control.Label
import scalafx.scene.layout.VBox
import scalafx.stage.FileChooser.ExtensionFilter
import scalafx.stage.{FileChooser, Modality, Stage}
import scalafxml.core.macros.sfxml
import scalafxml.core.{FXMLLoader, FXMLView, NoDependencyResolver}
import tobscore.sideprojects.gradecalc._
import tobscore.sideprojects.gradecalc.grade.{Fail, Grade, Pass}

trait MainControllerInterface {
  def addSubject(subject: MutableSubject[Grade]): Unit

  def updateResults(): Unit
}

/**
  * Created by Tobias Kerst on 08.03.17.
  */
@sfxml
class MainController(val subjectList: VBox, val resultLabel: Label, val exactGradeLabel: Label)
    extends MainControllerInterface {

  val logger = Logger(classOf[MainController])
  val semester: MutableSemester = MutableSemester(1)

  val fileChooser: FileChooser = new FileChooser() {
    title() = "Vorlage Speichern"
    initialFileName() = "Name"
    extensionFilters.add(new ExtensionFilter("Grade Calc Format", "*.gradecalc"))
  }
  val serializer = new Serializer[Semester]()

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

  def importPreset(): Unit = {
    val file = fileChooser.showOpenDialog(subjectList.getScene.getWindow)
    if (file != null) {
      logger.info(s"Importing preset from ${file.getAbsolutePath}")

      val importedSemester = serializer.deserialize(file.getAbsolutePath)
      logger.debug(s"Loaded semester with ${importedSemester.subjects.length} subjects")
      semester.reset()
      subjectList.children.clear()
      logger.debug(s"Semester count before import: ${semester.subjects.size}")
      importedSemester.subjects
        .map(_.toMutableSubject)
        .map(e => e.asInstanceOf[MutableSubject[Grade]])
        .foreach(e => addSubject(e))

      logger.debug(s"Semester count after import: ${semester.subjects.size}")
    }
  }

  def exportPreset(): Unit = {
    val file: File = fileChooser.showSaveDialog(subjectList.getScene.getWindow)
    if (file != null) {
      logger.debug(s"Couting semesters: ${semester.subjects.size}")
      val serializableSemester: Semester = semester.toSerializable
      logger.debug(s"Exporting: ${serializableSemester.subjects.size} semesters")
      serializer.serialize(serializableSemester, file.getAbsolutePath)
      logger.info(s"Exporting preset to ${file.getAbsolutePath}")
    }
  }

  def clearAndNew(): Unit = {
    subjectList.children.clear()
    logger.info("Deleting existing subjects")
  }

  def quitApplication(): Unit = {
    Platform.exit()
    System.exit(0)
  }

  override def addSubject(subject: MutableSubject[Grade]): Unit = {
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
    if (semester.result().isDefined) {
      semester.result().get match {
        case Fail() =>
          exactGradeLabel.text() = ""
          resultLabel.text() = "Nicht bestanden"
        case Pass() =>
          exactGradeLabel.text() = ""
          resultLabel.text() = "Bestanden"
        case grade: Grade =>
          exactGradeLabel.text() = s"(${semester.exactGradeResult().toString})"
          resultLabel.text() = grade.toString
      }
    }
  }
}
