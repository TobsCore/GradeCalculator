package tobscore.sideproject.gradecalc

import org.scalatest.{BeforeAndAfter, FunSuite}
import tobscore.sideprojects.gradecalc.{Module, Professor, Subject}
import tobscore.sideprojects.gradecalc.grade.{Fail, Grade, Pass, Passable}

import scala.collection.mutable.ListBuffer

/**
  * Created by Tobias Kerst on 06.03.17.
  */
class ModuleTest extends FunSuite with BeforeAndAfter {

  var moduleGradeOnly: Module = _
  var moduleEmpty: Module = _

  var subjectPassGrade1: Subject[Grade] = _
  var subjectPassGrade2: Subject[Grade] = _
  var subjectPassGrade3: Subject[Grade] = _
  var subjectFailGrade1: Subject[Grade] = _
  var subjectPass1: Subject[Pass] = _
  var subjectFail1: Subject[Fail] = _


  before {
    subjectPassGrade1 = Subject[Grade]("General Webtechnologies", Some(Grade(1.3)))
    subjectPassGrade2 = Subject[Grade]("Applied Webtechnologies", Some(Grade(2.3)), weight = 2)
    subjectPassGrade3 = Subject[Grade]("Styling Webpages via CSS", Some(Grade(2.0)))
    subjectFailGrade1 = Subject[Grade]("Javascript Goodies", Some(Grade(4.3)))
    subjectPass1 = Subject[Pass]("Statistical Approach in Web", Some(Pass()), weight = 0, instructor = Some(Professor("Prof. Dr. Exampleton")))
    subjectFail1 = Subject[Fail]("Enterprise Web Technologies", Some(Fail()), weight = 0)

    moduleGradeOnly = new Module("WebTechnologies", "Prof. Dr. Mighty Examplus")
    moduleGradeOnly += List(subjectPassGrade1, subjectPassGrade2)

    moduleEmpty = new Module("WebTechnologies", "Prof. Dr. Mighty Examplus")
  }

  test("Checking module creation with two graded subjects") {
    assertResult(2) {
      moduleGradeOnly.size
    }
  }


  test("Creating a module with different subjects {1 Pass only, the other graded}") {
    moduleEmpty += List(subjectPassGrade1, subjectPass1)
    assertResult(2) {
      moduleGradeOnly.size
    }
  }


  test("Calculating result of empty module") {
    assertResult(None) {
      moduleEmpty.result
    }
  }

  test("Calculating result for failed subject") {
    moduleEmpty += subjectFail1
    assertResult(Fail()) {
      moduleEmpty.result.get
    }
  }

  test("Calculating result for passed subject") {
    moduleEmpty += subjectPass1
    assertResult(Pass()) {
      moduleEmpty.result.get
    }
  }

  test("Calculating result for 2 passed subjects") {
    moduleEmpty += List(subjectPass1, subjectPass1)
    assertResult(Pass()) {
      moduleEmpty.result.get
    }
  }

  test("Calculating result for 2 passed subjects {1 passed, 1 failed}") {
    moduleEmpty += List(subjectPass1, subjectFail1)
    assertResult(Fail()) {
      moduleEmpty.result.get
    }
  }

  test("Calculating result of 1 graded subject") {
    moduleEmpty += subjectPassGrade1
    assertResult(Grade(1.3)) {
      moduleEmpty.result.get
    }
  }

  test("Calculating result of 2 graded subjects (the same)") {
    moduleEmpty += List(subjectPassGrade1, subjectPassGrade1)
    assertResult(Grade(1.3)) {
      moduleEmpty.result.get
    }
  }

  test("Calculating result of 2 different subjects with even grade") {
    moduleEmpty += List(subjectPassGrade1, subjectPassGrade3)
    assertResult(Grade(1.65)) {
      moduleEmpty.result.get
    }
  }

  test("Calculating result of 2 times same subject with weight 2") {
    moduleEmpty += List(subjectPassGrade2, subjectPassGrade2)
    assertResult(Grade(2.3)) {
      moduleEmpty.result.get
    }
  }

  test("Calculating result of same grade with different weight") {
    val testSub = Subject("Test", Some(Grade(2.3)))
    moduleEmpty += List(testSub, subjectPassGrade2)
    assertResult(Grade(2.3)) {
      moduleEmpty.result.get
    }
  }

  test("Calculating 3 different subjects and grade, increasing") {
    val sub1 = Subject("Test 1", Some(Grade(1.7)))
    val sub2 = Subject("Test 2", Some(Grade(2.0)))
    val sub3 = Subject("Test 3", Some(Grade(2.3)))
    moduleEmpty += List(sub1, sub2, sub3)

    assertResult(Grade(2.0)) {
      moduleEmpty.result.get
    }
  }

  test("Calculating 2 different grades laying next to each other") {
    val sub1 = Subject("Test 1", Some(Grade(1.7)))
    val sub2 = Subject("Test 2", Some(Grade(1.3)))

    moduleEmpty += List(sub1, sub2)
    assertResult(Grade(1.5)) {
      moduleEmpty.result.get
    }
  }

  test("Calculating a two subjects with same grade, one with weight of 2, one with weight of 1") {
    val sub1 = Subject("Test 1", Some(Grade(3.3)), weight = 1)
    val sub2 = Subject("Test 2", Some(Grade(3.3)), weight = 2)
    moduleEmpty += List(sub1, sub2)

    assertResult(Grade(3.3)) {
      moduleEmpty.result.get
    }
  }

  test("Lenas Recht-Rhetorik Fall") {
    val rhetorik = Subject("Rhetorik", Some(Grade(1.3)))
    val recht = Subject("Recht", Some(Grade(1.7)))
    val interculturalCommunication = Subject("Intercultural Communication", Some(Pass()))

    moduleEmpty += List(rhetorik, recht, interculturalCommunication)
    assertResult(Grade(1.5)) {
      moduleEmpty.result.get
    }
  }

}

