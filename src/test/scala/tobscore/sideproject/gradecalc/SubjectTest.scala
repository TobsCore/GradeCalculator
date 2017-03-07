package tobscore.sideproject.gradecalc

import org.scalatest.{BeforeAndAfter, FunSuite}
import tobscore.sideprojects.gradecalc.{Professor, Subject}
import tobscore.sideprojects.gradecalc.grade.{ExplicitGrade, Pass}

/**
  * Created by Tobias Kerst on 06.03.17.
  */
class SubjectTest extends FunSuite with BeforeAndAfter {

  var inMemoryDatabases: Subject[ExplicitGrade] = _
  var bigDataEngineering: Subject[ExplicitGrade] = _
  var computerVisionLab: Subject[Pass] = _
  var rzBetrieb: Subject[ExplicitGrade] = _
  var rhetorik: Subject[Pass] = _
  var recht: Subject[Pass] = _

  var ex: Subject[ExplicitGrade] = _

  before {
    inMemoryDatabases = Subject("In Memory Datenbanken", Some(ExplicitGrade(1.0)))
    bigDataEngineering = Subject("Big DataEngineering", None)
    rzBetrieb = Subject("RZ Betrieb", Some(ExplicitGrade(4.7)))
    computerVisionLab = Subject("Computer Vision Lab", Some(Pass(false)))
    rhetorik = Subject("Rhetorik", Some(Pass(true)))
    recht = Subject("Recht", None)

    ex = Subject("Example", Some(ExplicitGrade(1.3)))
  }

  test("Setting a new grade for class") {
    inMemoryDatabases.result = Some(ExplicitGrade(1.3))
    assertResult(ExplicitGrade(1.3)) {
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
    val tempSubject = new Subject[ExplicitGrade]("Example", weight = 2, result = Some(ExplicitGrade(2.3)))
    assertResult(ExplicitGrade(2.3)) {
      tempSubject.result.get
    }
  }
}
