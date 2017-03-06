package tobscore.sideproject.gradecalc

import org.scalatest.{BeforeAndAfter, FunSuite}
import tobscore.sideprojects.gradecalc.grade.Grade

/**
  * Created by tobscore on 05.03.17.
  */
class GradeValTest extends FunSuite with BeforeAndAfter {

  var grade: Grade = _
  var failGrade: Grade = _
  var almostFailButStillPass: Grade = _

  before {
    grade = new Grade(1.0)
    failGrade = new Grade(4.3)
    almostFailButStillPass = new Grade(4.0)
  }

  test("Testing default grade value") {
    assert(grade.grade.equals(1.0))
  }


  test("Testing grade value too low (< 1.0)") {
    assertThrows[IllegalArgumentException](new Grade(0.7))
  }

  test("Testing grade value too high (> 5.0)") {
    assertThrows[IllegalArgumentException](new Grade(5.3))
  }

  test("Testing grade value in scope but still illegal value") {
    assertThrows[IllegalArgumentException](new Grade(1.5))
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
}
