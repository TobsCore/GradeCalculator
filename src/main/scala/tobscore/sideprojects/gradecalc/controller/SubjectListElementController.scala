package tobscore.sideprojects.gradecalc.controller

import scalafx.scene.control.{Label, MenuButton, TextField, CheckBox}
import scalafxml.core.macros.sfxml

@sfxml
class SubjectListElementController(val subjectLabel: Label,
                                   val subjectGrade: TextField,
                                   val elementMenu: MenuButton,
                                   val subjectFinished: CheckBox) {

}
