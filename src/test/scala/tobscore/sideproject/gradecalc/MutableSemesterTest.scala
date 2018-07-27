package tobscore.sideproject.gradecalc

import org.scalatest.{BeforeAndAfter, FunSuite}
import tobscore.sideprojects.gradecalc.grade.{Fail, FailPass, Grade, Pass}
import tobscore.sideprojects.gradecalc.{MutableSemester, MutableSubject}

class MutableSemesterTest extends FunSuite with BeforeAndAfter {

  var semester: MutableSemester = _

  before {
    semester = MutableSemester(1)
  }

  test("Add subject to semester") {
    var subject1 = MutableSubject("Subject 1", None)
    semester += subject1
  }

  test("Remove a subject from semester") {
    var subject1 = MutableSubject("Subject 1", None)
    semester += subject1
    semester.remove(0)
    assertResult(0) {
      semester.subjects.length
    }
  }

  test("Calculate the average of one graded subject") {
    val subject = MutableSubject[Grade]("Subject", Some(Grade(2.3)))
    assertResult(Grade(2.3)) {
      subject.result.get
    }
  }

  test("Get result for one pass subject") {
    val subject = MutableSubject[FailPass]("Subject", Some(Pass()))
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
    val subjectPass = MutableSubject[FailPass]("Subject Pass", Some(Pass()))
    val subjectGradedPass = MutableSubject[Grade]("Subject Graded and Passing", Some(Grade(1.3)))
    semester += List(subjectPass, subjectGradedPass)
    assertResult(Grade(1.3)) {
      semester.result.get
    }

  }

  test("Result of one failing and one passing graded subject") {
    val subjectFail = MutableSubject[FailPass]("Subject Fail", Some(Fail()))
    val subjectPassGraded = MutableSubject[Grade]("Grades Subject", Some(Grade(2.3)))
    semester += List(subjectFail, subjectPassGraded)
    assertResult(Fail()) {
      semester.result.get
    }
  }

  test("Create a simple semester, generate serializable list of subjects and so on") {
    val subject1 = MutableSubject[FailPass]("Subject that either fails, or passes", Some(Pass()))
    val subject2 = MutableSubject[Grade]("Graded subject", Some(Grade(2.3)))

    semester += List(subject1, subject2)
    val serializableSemester = semester.toSerializable()

    assertResult(subject1.toSubject()) {
      serializableSemester.subjects.head
    }
  }
}
