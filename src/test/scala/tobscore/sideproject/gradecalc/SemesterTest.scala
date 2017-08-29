package tobscore.sideproject.gradecalc

import org.scalatest.{BeforeAndAfter, FunSuite}
import tobscore.sideprojects.gradecalc.grade.{Fail, FailPass, Grade, Pass}
import tobscore.sideprojects.gradecalc.{Semester, Subject}

class SemesterTest extends FunSuite with BeforeAndAfter {

  var semester: Semester = _

  before {
    semester = Semester(1)
  }

  test("Creating a semester") {
    val testSemester = Semester(1);
  }

  test("Add subject to semester") {
    var subject1 = Subject("Subject 1", None)
    semester += subject1
  }

  test("Remove a subject from semester") {
    var subject1 = Subject("Subject 1", None)
    semester += subject1
    semester.remove(0)
    assertResult(0) {
      semester.subjects.length
    }
  }

  test("Calculate the average of one graded subject") {
    var subject = Subject[Grade]("Subject", Some(Grade(2.3)))
    assertResult(Grade(2.3)) {
      subject.result.get
    }
  }

  test("Get result for one pass subject") {
    var subject = Subject[FailPass]("Subject", Some(Pass()))
    semester += subject
    assertResult(Pass()) {
      semester.result.get
    }
  }

  test("Semester with no classes") {
    assertResult(None) {
      semester.result
    }
  }

  test("Result of pass and graded passing subject") {
    var subjectPass = Subject[FailPass]("Subject Pass", Some(Pass()))
    var subjectGradedPass = Subject[Grade]("Subject Graded and Passing", Some(Grade(1.3)))
    semester += List(subjectPass, subjectGradedPass)
    assertResult(Grade(1.3)) {
      semester.result.get
    }

  }

  test("Result of one failing and one passing graded subject") {
    var subjectFail = Subject[FailPass]("Subject Fail", Some(Fail()))
    var subjectPassGraded = Subject[Grade]("Grades Subject", Some(Grade(2.3)))
    semester += List(subjectFail, subjectPassGraded)
    assertResult(Fail()) {
      semester.result.get
    }
  }
}
