package tobscore.sideprojects.gradecalc.controller

import scalafx.scene.control.{Button, ComboBox, TextField}
import scalafxml.core.macros.sfxml

@sfxml
class AddSubjectDialogController(val subjectIdentifier: TextField,
                                 val subjectGradeWeight: TextField,
                                 val subjectType: ComboBox[String],
                                 val cancelButton: Button,
                                 val acceptButton: Button) {

}
