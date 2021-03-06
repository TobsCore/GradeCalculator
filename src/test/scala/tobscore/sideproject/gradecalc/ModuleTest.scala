package tobscore.sideproject.gradecalc

import org.scalatest.{BeforeAndAfter, FunSuite}
import tobscore.sideprojects.gradecalc.{Module, Professor, MutableSubject}
import tobscore.sideprojects.gradecalc.grade._

import scala.collection.mutable.ListBuffer

/**
  * Created by Tobias Kerst on 06.03.17.
  */
class ModuleTest extends FunSuite with BeforeAndAfter {

  var moduleGradeOnly: Module = _
  var moduleEmpty: Module = _

  var subjectPassGrade1: MutableSubject[Grade] = _
  var subjectPassGrade2: MutableSubject[Grade] = _
  var subjectPassGrade3: MutableSubject[Grade] = _
  var subjectFailGrade1: MutableSubject[Grade] = _
  var subjectPass1: MutableSubject[FailPass] = _
  var subjectFail1: MutableSubject[FailPass] = _


  before {
    subjectPassGrade1 = MutableSubject[Grade]("General Webtechnologies", Some(Grade(1.3)))
    subjectPassGrade2 = MutableSubject[Grade]("Applied Webtechnologies", Some(Grade(2.3)), weight = 2)
    subjectPassGrade3 = MutableSubject[Grade]("Styling Webpages via CSS", Some(Grade(2.0)))
    subjectFailGrade1 = MutableSubject[Grade]("Javascript Goodies", Some(Grade(4.3)))
    subjectPass1 = MutableSubject[FailPass]("Statistical Approach in Web", Some(Pass()), 1, 1, Some(Professor("Prof. Dr. Exampleton")), isFinished = false)
    subjectFail1 = MutableSubject[FailPass]("Enterprise Web Technologies", Some(Fail()), weight = 1)

    moduleGradeOnly = new Module("WebTechnologies", "Prof. Dr. Mighty Examplus")
    moduleGradeOnly += List(subjectPassGrade1, subjectPassGrade2)

    moduleEmpty = new Module("WebTechnologies", "Prof. Dr. Mighty Examplus")
  }

  test("Checking module creation with two graded subjects") {
    assertResult(2) {
      moduleGradeOnly.amountOfSubjects
    }
  }


  test("Creating a module with different subjects {1 Pass only, the other graded}") {
    moduleEmpty += List(subjectPassGrade1, subjectPass1)
    assertResult(2) {
      moduleGradeOnly.amountOfSubjects
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
    val testSub = MutableSubject("Test", Some(Grade(2.3)))
    moduleEmpty += List(testSub, subjectPassGrade2)
    assertResult(Grade(2.3)) {
      moduleEmpty.result.get
    }
  }

  test("Calculating 3 different subjects and grade, increasing") {
    val sub1 = MutableSubject("Test 1", Some(Grade(1.7)))
    val sub2 = MutableSubject("Test 2", Some(Grade(2.0)))
    val sub3 = MutableSubject("Test 3", Some(Grade(2.3)))
    moduleEmpty += List(sub1, sub2, sub3)

    assertResult(Grade(2.0)) {
      moduleEmpty.result.get
    }
  }

  test("Calculating 2 different grades laying next to each other") {
    val sub1 = MutableSubject("Test 1", Some(Grade(1.7)))
    val sub2 = MutableSubject("Test 2", Some(Grade(1.3)))

    moduleEmpty += List(sub1, sub2)
    assertResult(Grade(1.5)) {
      moduleEmpty.result.get
    }
  }

  test("Calculating a two subjects with same grade, one with weight of 2, one with weight of 1") {
    val sub1 = MutableSubject("Test 1", Some(Grade(3.3)), weight = 1)
    val sub2 = MutableSubject("Test 2", Some(Grade(3.3)), weight = 2)
    moduleEmpty += List(sub1, sub2)

    assertResult(Grade(3.3)) {
      moduleEmpty.result.get
    }
  }

  test("Lenas Recht-Rhetorik Fall") {
    val rhetorik = MutableSubject("Rhetorik", Some(Grade(1.3)))
    val recht = MutableSubject("Recht", Some(Grade(1.7)))
    val interculturalCommunication = MutableSubject("Intercultural Communication", Some(Pass()))

    moduleEmpty += List(rhetorik, recht, interculturalCommunication)
    assertResult(Grade(1.5)) {
      moduleEmpty.result.get
    }
  }

  test("Remove subject from module") {
    val exModule = new Module("Example Module", "Prof. Dr. Example")
    exModule += List(subjectPassGrade1, subjectPassGrade2, subjectPassGrade3)
    assertResult(3) {
      exModule.amountOfSubjects
    }

    // Removing one subject
    exModule -= subjectPassGrade3
    assertResult(2) {
      exModule.amountOfSubjects
    }
  }

  test("Remove multiple subject from module") {
    val exModule = new Module("Example Module", "Prof. Dr. Example")
    exModule += List(subjectPassGrade1, subjectPassGrade2, subjectPassGrade3)

    exModule -= List(subjectPassGrade3, subjectPassGrade2)
    assertResult(1) {
      exModule.amountOfSubjects
    }
  }

  test("Remove the same item multiple times") {
    moduleGradeOnly += subjectPass1
    assertResult(3) {
      moduleGradeOnly.amountOfSubjects
    }

    moduleGradeOnly -= List(subjectPass1, subjectPass1)
    assertResult(2) {
      moduleGradeOnly.amountOfSubjects
    }
  }

  test("Remove subject from empty module") {
    // Module is really empty
    assertResult(0) {
      moduleEmpty.amountOfSubjects
    }

    moduleEmpty -= subjectPass1

    assertResult(0) {
      moduleEmpty.amountOfSubjects
    }
  }

}

