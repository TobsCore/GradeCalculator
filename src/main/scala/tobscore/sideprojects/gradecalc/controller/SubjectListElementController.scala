package tobscore.sideprojects.gradecalc.controller

import tobscore.sideprojects.gradecalc.Subject
import tobscore.sideprojects.gradecalc.grade.Passable

import scalafx.scene.control.{CheckBox, Label, MenuButton, TextField}
import scalafxml.core.macros.sfxml

@sfxml
class SubjectListElementController(val subjectLabel: Label,
                                   val subjectGrade: TextField,
                                   val elementMenu: MenuButton,
                                   val subjectFinished: CheckBox) {

}
