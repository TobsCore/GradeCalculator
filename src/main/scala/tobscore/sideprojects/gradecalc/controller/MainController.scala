package tobscore.sideprojects.gradecalc.controller

import java.io.IOException
import javafx.scene.Scene

import scalafx.Includes._
import scalafx.stage.{Modality, Stage}
import scalafxml.core.macros.sfxml
import scalafxml.core.{FXMLView, NoDependencyResolver}

/**
  * Created by Tobias Kerst on 08.03.17.
  */
@sfxml
class MainController {

  def openAboutDialog(): Unit = {
    val aboutDialogFXML: String = "/AboutDialog.fxml"
    val resource = getClass.getResource(aboutDialogFXML)
    if (resource == null) {
      throw new IOException(s"Cannot load $aboutDialogFXML")
    }
    val root = FXMLView(resource, NoDependencyResolver)

    val aboutStage = new Stage() {
      title = "About GradeCalculator"
      scene = new Scene(root)
      resizable = false

      initModality(Modality.ApplicationModal)
    }

    if (!aboutStage.showing()) {
      aboutStage.show()
    } else {
      aboutStage.requestFocus()
    }
  }


  def displayNewSubjectDialog() = {
    val aboutDialogFXML: String = "/AddSubjectDialog.fxml"
    val resource = getClass.getResource(aboutDialogFXML)
    if (resource == null) {
      throw new IOException(s"Cannot load $aboutDialogFXML")
    }
    val root = FXMLView(resource, NoDependencyResolver)

    val aboutStage = new Stage() {
      title = "Add Subject"
      scene = new Scene(root)
      resizable = false
      initModality(Modality.ApplicationModal)
    }

    if (!aboutStage.showing()) {
      aboutStage.show()
    } else {
      aboutStage.requestFocus()
    }
  }
}
