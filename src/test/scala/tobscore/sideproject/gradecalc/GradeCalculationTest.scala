package tobscore.sideproject.gradecalc

import org.scalatest.{BeforeAndAfter, FunSuite}
import tobscore.sideprojects.gradecalc.Subject
import tobscore.sideprojects.gradecalc.grade.{CalculatedGrade, ExplicitGrade}

/**
  * Created by Tobias Kerst on 07.03.17.
  */
class GradeCalculationTest extends FunSuite with BeforeAndAfter {

  var sub10: Subject[ExplicitGrade] = _
  var sub17: Subject[ExplicitGrade] = _
  var sub50: Subject[ExplicitGrade] = _

  before {
    sub10 = new Subject[ExplicitGrade]("Sub10", Some(ExplicitGrade(1.0)))
    sub17 = new Subject[ExplicitGrade]("Sub17", Some(ExplicitGrade(1.7)))
    sub50 = new Subject[ExplicitGrade]("Sub50", Some(ExplicitGrade(5.0)))
  }

  test("Calculate simple grade") {
    val grade = new CalculatedGrade(sub10)
    assertResult(1.0) {
      grade.get
    }
  }

  test("Calculating simple grade (5.0)") {
    val grade = new CalculatedGrade(sub50)
    assertResult(5.0) {
      grade.get
    }
  }
}
