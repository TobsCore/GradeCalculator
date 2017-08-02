package tobscore.sideproject.gradecalc

import org.scalatest.{BeforeAndAfter, FunSuite}
import tobscore.sideprojects.gradecalc.{Professor, Subject}
import tobscore.sideprojects.gradecalc.grade.{Fail, Grade, Pass}

/**
  * Created by Tobias Kerst on 06.03.17.
  */
class SubjectTest extends FunSuite with BeforeAndAfter {

  var inMemoryDatabases: Subject[Grade] = _
  var bigDataEngineering: Subject[Grade] = _
  var computerVisionLab: Subject[Fail] = _
  var rzBetrieb: Subject[Grade] = _
  var rhetorik: Subject[Pass] = _
  var recht: Subject[Pass] = _

  var ex: Subject[Grade] = _

  before {
    inMemoryDatabases = Subject("In Memory Datenbanken", Some(Grade(1.0)))
    bigDataEngineering = Subject("Big DataEngineering", None)
    rzBetrieb = Subject("RZ Betrieb", Some(Grade(4.7)))
    computerVisionLab = Subject("Computer Vision Lab", Some(Fail()))
    rhetorik = Subject("Rhetorik", Some(Pass()))
    recht = Subject("Recht", None)

    ex = Subject("Example", Some(Grade(1.3)))
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
    val tempSubject = new Subject[Grade]("Example", weight = 2, result = Some(Grade(2.3)))
    assertResult(Grade(2.3)) {
      tempSubject.result.get
    }
  }
}
