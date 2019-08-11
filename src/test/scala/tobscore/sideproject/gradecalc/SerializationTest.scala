package tobscore.sideproject.gradecalc

import org.scalatest.{BeforeAndAfter, FunSuite}
import tobscore.sideprojects.gradecalc._
import tobscore.sideprojects.gradecalc.grade.Grade

import scalafx.beans.property.{BooleanProperty, IntegerProperty, StringProperty}

class SerializationTest extends FunSuite with BeforeAndAfter {

  test("Testing grade serialization and deserialization") {
    val originalGrade = new Grade(2.3)

    val serializer = new Serializer[Grade]()
    serializer.serialize(originalGrade, "/tmp/grade")
    val deserializedGrade = serializer.deserialize("/tmp/grade")

    assertResult(originalGrade) {
      deserializedGrade
    }
  }

  test("Testing subject serialization and deserialization") {
    val originalSubject: Subject[Grade] = MutableSubject[Grade](
      StringProperty("Example Subject"),
      Some(Grade(1.3)),
      IntegerProperty(2),
      IntegerProperty(0),
      Some(Professor("Prof. Dr. Professorson")),
      BooleanProperty(false)
    ).toSubject

    val serializer = new Serializer[Subject[Grade]]()
    serializer.serialize(originalSubject, "/tmp/subject")
    val deserializedSubject = serializer.deserialize("/tmp/subject")

    assertResult(originalSubject) {
      deserializedSubject
    }
  }

  test("Testing semester serialization and deserialization") {
    val originSemester = MutableSemester(1)
    originSemester += MutableSubject[Grade]("ExampleSubject", Some(Grade(2.3)), 3)
    val serializableSemester = originSemester.toSerializable

    val serializer = new Serializer[Semester]()
    serializer.serialize(serializableSemester, "/tmp/semesterSimple")
    val deserializeSemester: Semester = serializer.deserialize("/tmp/semesterSimple")

    assertResult(serializableSemester) {
      deserializeSemester
    }
  }
}
