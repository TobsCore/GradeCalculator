package tobscore.sideprojects.gradecalc

import tobscore.sideprojects.gradecalc.grade._

import scala.collection.mutable.ListBuffer

case class Semester(semesterNumber: Int) {
  val subjects: ListBuffer[Subject[_ <: Passable]] = new ListBuffer

  def +=(subject: Subject[_ <: Passable]): Unit = {
    subjects += subject
  }

  def +=(subjectList: List[Subject[_ <: Passable]]): Unit = {
    subjects ++= subjectList
  }

  def remove(c: Int): Unit = {
    subjects.remove(c)

  }

  def calcResult(): Double = {
    var weightSum = 0
    var gradeAccumulator: Int = 0
    subjects.map(_.result.get).filter(_.isInstanceOf[Grade]).map(_.asInstanceOf[Grade]).foldLeft(0.0)((a: Double, b: Grade) => b.grade + a)

    for (subject <- subjects) {
      subject.result.get match {
        case subjectGrade: Grade => {
          weightSum += subject.weight()
          gradeAccumulator += subject.weight() * subjectGrade.grade
        }
        case _ => {}
      }
    }
    val gradeAccumulatorDoubleVal: Double = gradeAccumulator / 10.0
    val averageGrade: Double = (gradeAccumulatorDoubleVal / weightSum)

    return averageGrade
  }

  def result(): Option[_ <: Passable] = {
    def containsFailedSubject(): Boolean = {
      subjects.filter(_.isFail.getOrElse(true)).nonEmpty
    }

    def containsGrades(): Boolean = {
      subjects.map(_.result.get).filter(_.isInstanceOf[Grade]).nonEmpty
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
}
