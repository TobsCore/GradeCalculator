package tobscore.sideproject.gradecalc

import org.scalatest.{BeforeAndAfter, FunSuite}
import tobscore.sideprojects.gradecalc.{Professor, MutableSubject}
import tobscore.sideprojects.gradecalc.grade.{Fail, FailPass, Grade, Pass}

import scalafx.beans.property.IntegerProperty

/**
  * Created by Tobias Kerst on 06.03.17.
  */
class SubjectTest extends FunSuite with BeforeAndAfter {

  var inMemoryDatabases: MutableSubject[Grade] = _
  var bigDataEngineering: MutableSubject[Grade] = _
  var computerVisionLab: MutableSubject[FailPass] = _
  var rzBetrieb: MutableSubject[Grade] = _
  var rhetorik: MutableSubject[FailPass] = _
  var recht: MutableSubject[FailPass] = _

  var ex: MutableSubject[Grade] = _

  before {
    inMemoryDatabases = MutableSubject("In Memory Datenbanken", Some(Grade(1.0)))
    bigDataEngineering = MutableSubject("Big DataEngineering", None)
    rzBetrieb = MutableSubject("RZ Betrieb", Some(Grade(4.7)))
    computerVisionLab = MutableSubject("Computer Vision Lab", Some(Fail()))
    rhetorik = MutableSubject("Rhetorik", Some(Pass()))
    recht = MutableSubject("Recht", None)

    ex = MutableSubject("Example", Some(Grade(1.3)))
  }

  test("Setting a new grade for class") {
    inMemoryDatabases.result = Some(Grade(1.3))
    assertResult(Grade(1.3)) {
      inMemoryDatabases.result.get
    }
  }

  test("Checking if class failed or passed") {
    assert(inMemoryDatabases.isPass.get)
    assert(rhetorik.isPass.get)
    assert(rzBetrieb.isFail.get)
    assert(computerVisionLab.isFail.get)
  }

  test("Checking non finished classes, if they return the correct result for passing/failing") {
    assert(bigDataEngineering.isPass.isEmpty)
    assert(recht.isPass.isEmpty)
    assert(recht.isFail.isEmpty)
  }

  test("Checking setting a teacher for a subject") {
    val prof = Professor("Prof. Dr. Peter Example")
    ex.instructor = Some(prof)
    assertResult(Professor("Prof. Dr. Peter Example")) {
      ex.instructor.get
    }
  }

  test("Checking the weight of a subject") {
   val tempSubject = MutableSubject[Grade]("Example", Some(Grade(2.3)), 2)
    assertResult(Grade(2.3)) {
      tempSubject.result.get
    }
  }

  test("Test Subject for module") {
    val subjectPassGrade = MutableSubject[Grade]("General Webtechnologies", Some(Grade(1.3)))
    assertResult(Grade(1.3)) {
      subjectPassGrade.result.get
    }
  }

  test("Empty Grade") {
    val exSubject = MutableSubject[Grade]("Some example subject", None)
    assertResult(None) {
      exSubject.result
    }
  }

  test("Checking if subject is graded") {
    assert(inMemoryDatabases.isGraded().get)
  }

  test("Checking if subject with no grade yet is graded") {
    val exSub = MutableSubject[Grade]("Example Subject", None)
    assertResult(None) {
      exSub.isGraded()
    }
  }

  test("Checking if fail/pass subject is graded") {
    assert(!computerVisionLab.isGraded().get)
  }
}
