package tobscore.sideproject.gradecalc

import org.scalatest.{BeforeAndAfter, FunSuite}
import tobscore.sideprojects.gradecalc.Professor

/**
  * Created by Tobias Kerst on 06.03.17.
  */
class ProfessorTest extends FunSuite with BeforeAndAfter {

  var prof1: Professor = _
  var prof2: Professor = _

  before {
    prof1 = Professor("Prof. Dr. Example")
    prof2 = Professor("Professor. Dr. Example")
  }

  test("Checks if the professors are the same") {
    assert(!prof1.equals(prof2))
  }


}
