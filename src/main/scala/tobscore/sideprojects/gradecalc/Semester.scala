package tobscore.sideprojects.gradecalc

import tobscore.sideprojects.gradecalc.grade.Passable

case class Semester(semesterNumber: Int, subjects: List[Subject[_ <: Passable]]) {

}

