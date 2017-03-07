package tobscore.sideproject.gradecalc

import org.scalatest.{BeforeAndAfter, FunSuite}
import tobscore.sideprojects.gradecalc.grade.ExplicitGrade

/**
  * Created by tobscore on 05.03.17.
  */
class ExplicitGradeValTest extends FunSuite with BeforeAndAfter {

  var grade: ExplicitGrade = _
  var failGrade: ExplicitGrade = _
  var almostFailButStillPass: ExplicitGrade = _

  before {
    grade = new ExplicitGrade(1.0)
    failGrade = new ExplicitGrade(4.3)
    almostFailButStillPass = new ExplicitGrade(4.0)
  }

  test("Testing default grade value") {
    assert(grade.get.equals(1.0))
  }


  test("Testing grade value too low (< 1.0)") {
    assertThrows[IllegalArgumentException](new ExplicitGrade(0.7))
  }

  test("Testing grade value too high (> 5.0)") {
    assertThrows[IllegalArgumentException](new ExplicitGrade(5.3))
  }

  test("Testing grade value in scope but still illegal value") {
    assertThrows[IllegalArgumentException](new ExplicitGrade(1.5))
  }

  test("Test if passed grade is evaluated correctly") {
    assert(grade.isPass.get)
    assert(!grade.isFail.get)
  }

  test("Testing if failed grade is evaluated corrrectly") {
    assert(failGrade.isFail.get)
  }

  test("Testing if almost failed, but still passed grade is evaluated correctly") {
    assert(almostFailButStillPass.isPass.get)
  }

  test("Testing passing integers into class (1.0)") {
    assertResult(ExplicitGrade(1.0)) {
      ExplicitGrade(3)
    }
  }

  test("Testing passing integers into class (5.0)") {
    assertResult(ExplicitGrade(5.0)) {
      ExplicitGrade(15)
    }
  }

  test("Testing passing out of bounds integer into class (Too small)") {
    assertThrows[IllegalArgumentException](ExplicitGrade(2))
  }

  test("Testing passing out of bounds integer into class (Too big)") {
    assertThrows[IllegalArgumentException](ExplicitGrade(16))
  }
}
