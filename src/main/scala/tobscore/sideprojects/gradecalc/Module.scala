package tobscore.sideprojects.gradecalc

import tobscore.sideprojects.gradecalc.grade.{Fail, Grade, Pass, Passable}

import scala.collection.mutable.ListBuffer

/**
  * Created by Tobias Kerst on 06.03.17.
  */
class Module(name: String, professor: String) {

  def -=(subjectList: List[MutableSubject[_ <: Passable]]): Unit =
    subjectList.foreach(subjects -= _)

  def -=(subject: MutableSubject[_ <: Passable]): ListBuffer[MutableSubject[_ <: Passable]] =
    subjects -= subject

  def result: scala.Option[_ <: Passable] = {
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

      var weightSum = 0
      var gradeAccumulator: Int = 0
      subjects
        .map(_.result.get)
        .filter(_.isInstanceOf[Grade])
        .map(_.asInstanceOf[Grade])
        .foldLeft(0.0)((a: Double, b: Grade) => b.grade + a)

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
      Some(Grade(averageGrade))
    } else {
      None
    }
  }

  def +=(subject: MutableSubject[_ <: Passable]): Unit = {
    subjects += subject
  }

  def +=(subjectList: List[MutableSubject[_ <: Passable]]): Unit = {
    subjects ++= subjectList
  }

  var subjects: ListBuffer[MutableSubject[_ <: Passable]] =
    new ListBuffer[MutableSubject[_ <: Passable]]()

  def amountOfSubjects: Int = subjects.size
}
