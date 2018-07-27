package tobscore.sideprojects.gradecalc

import com.typesafe.scalalogging.Logger
import tobscore.sideprojects.gradecalc.grade._

import scala.collection.mutable.ListBuffer

case class MutableSemester(semesterNumber: Int) {
  val logger = Logger(classOf[MutableSemester])
  val subjects: ListBuffer[MutableSubject[_ <: Passable]] = new ListBuffer

  def +=(subject: MutableSubject[_ <: Passable]): Unit = {
    subjects += subject
  }

  def +=(subjectList: List[MutableSubject[_ <: Passable]]): Unit = {
    subjects ++= subjectList
  }

  def remove(c: Int): Unit = {
    subjects.remove(c)

  }

  def reset(semester: Semester): Unit = {
    subjects.clear()
    semester.subjects.map(_.toMutableSubject).foreach(subjects += _)
  }

  def exactGradeResult(): Double = calcResult()


  private def calcResult(): Double = {
    var weightSum = 0
    var gradeAccumulator: Int = 0
    subjects.map(_.result.get).filter(_.isInstanceOf[Grade]).map(_.asInstanceOf[Grade]).foldLeft(0.0)((a: Double, b: Grade) => b.grade + a)

    for (subject <- subjects) {
      subject.result.get match {
        case subjectGrade: Grade =>
          weightSum += subject.weight()
          gradeAccumulator += subject.weight() * subjectGrade.grade
        case _ =>
      }
    }
    val gradeAccumulatorDoubleVal: Double = gradeAccumulator / 10.0
    val averageGrade: Double = gradeAccumulatorDoubleVal / weightSum

    averageGrade
  }

  def result(): Option[_ <: Passable] = {
    def containsFailedSubject(): Boolean = {
      subjects.exists(_.isFail.getOrElse(true))
    }

    def containsGrades(): Boolean = {
      subjects.map(_.result.get).exists(_.isInstanceOf[Grade])
    }

    if (subjects.isEmpty) {
      None
    } else if (containsFailedSubject()) {
      Some(Fail())
    } else if (!containsGrades()) {
      Some(Pass())
    } else if (containsGrades()) {
      val averageGrade = calcResult()
      Some(Grade(averageGrade))
    } else {
      None
    }
  }

  def toSerializable: Semester = {
    val serializableSubjects = subjects.map(subject => subject.toSubject).toList
    Semester(semesterNumber, serializableSubjects)
  }
}
