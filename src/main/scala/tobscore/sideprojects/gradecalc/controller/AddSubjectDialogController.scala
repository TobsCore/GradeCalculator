package tobscore.sideprojects.gradecalc.controller

import scalafx.Includes._
import scalafx.scene.control.{Button, ComboBox, TextField}
import javafx.stage.Stage

import scalafxml.core.macros.sfxml

@sfxml
class AddSubjectDialogController(val subjectIdentifier: TextField,
                                 val subjectGradeWeight: TextField,
                                 val subjectType: ComboBox[String],
                                 val cancel: Button,
                                 val accept: Button) {

  subjectIdentifier.text.addListener((_, _, subjectIdentifiertText) => {
    accept.disable() = subjectIdentifiertText.size <= 0
  })

  def quitDialog(): Unit = {
    val stage: Stage = subjectIdentifier.getScene.getWindow.asInstanceOf[Stage]
    stage.close()
  }
}
