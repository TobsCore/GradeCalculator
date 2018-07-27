package tobscore.sideprojects.gradecalc

import java.io.IOException
import java.net.URL

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.Includes._
import scalafxml.core.{FXMLView, NoDependencyResolver}

/**
  * Created by Tobias Kerst on 05.03.17.
  */
object GradeCalculator extends JFXApp {

  private val layoutFile: String = "/GradeCalculator.fxml"
  val resource: URL = getClass.getResource(layoutFile)

  if (resource == null) {
    throw new IOException(s"Cannot load resource: $layoutFile")
  }

  val root = FXMLView(resource, NoDependencyResolver)

  stage = new PrimaryStage() {
    title = "GradeCalc"
    scene = new Scene(root)
    minWidth = 630
    minHeight = 400
  }

}
