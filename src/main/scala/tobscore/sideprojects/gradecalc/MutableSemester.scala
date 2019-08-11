package tobscore.sideprojects.gradecalc

import com.typesafe.scalalogging.Logger
import tobscore.sideprojects.gradecalc.grade._

import scala.collection.mutable

case class MutableSemester(semesterNumber: Int) {

  def remove(subject: MutableSubject[Nothing]): Boolean = {
    subjects.remove(subject)
  }

  val logger = Logger(classOf[MutableSemester])

  val subjects: mutable.LinkedHashSet[MutableSubject[_ <: Passable]] =
    mutable.LinkedHashSet.empty

  def +=(subject: MutableSubject[_ <: Passable]): Boolean = {
    subjects.add(subject)
  }

  def +=(subjectList: List[MutableSubject[_ <: Passable]]): Boolean = {
    subjectList.map(subjects.add).foldLeft(true)(_ && _)
  }

  def reset(): Unit = {
    subjects.clear()
  }

  def containsSubject(subject: MutableSubject[_ <: Passable]): Boolean = {
    subjects.contains(subject)
  }

  def exactGradeResult(): Double = calcResult()

  private def calcResult(): Double = {
    var weightSum = 0
    var gradeAccumulator: Int = 0
    val subjectsWithResults = subjects.filter(_.result.isDefined)
    subjectsWithResults
      .map(_.result.get)
      .filter(_.isInstanceOf[Grade])
      .map(_.asInstanceOf[Grade])
      .foldLeft(0.0)((a: Double, b: Grade) => b.grade + a)

    for (subject <- subjectsWithResults) {
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
      subjects.exists(_.isFail.getOrElse(false))
    }

    def containsGrades(): Boolean = {
      subjects.filter(_.result.isDefined).map(_.result.get).exists(_.isInstanceOf[Grade])
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
