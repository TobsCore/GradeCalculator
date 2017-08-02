package tobscore.sideprojects.gradecalc

import tobscore.sideprojects.gradecalc.grade.{Grade, Fail, Pass, Passable}

import scala.util.control.Breaks._
import scala.collection.mutable.ListBuffer

/**
  * Created by Tobias Kerst on 06.03.17.
  */
class Module(name: String, professor: String) {


  def result: scala.Option[_ <: Passable] = {
    def containsFailedSubject(): Boolean = {
      subjects.filter(_.isFail.getOrElse(true)).nonEmpty
    }

    def containsGrades(): Boolean = {
      subjects.map(_.result.get).filter(_.isInstanceOf[Grade]).nonEmpty
    }

    var gradeAccumulator: Double = 0
    var gradeWeight: Int = 0
    var result: Option[_ <: Passable] = None

    if (subjects.isEmpty) {
      None
    } else if (containsFailedSubject()) {
      Some(Fail())
    } else if (!containsGrades()) {
      Some(Pass())
    } else if (containsGrades()) {

      var weightSum = 0
      var gradeAccumulator: Int = 0
      subjects.map(_.result.get).filter(_.isInstanceOf[Grade]).map(_.asInstanceOf[Grade]).foldLeft(0.0)((a: Double, b: Grade) => b.grade + a)

      for (subject <- subjects) {
        subject.result.get match {
          case subjectGrade: Grade => {
            weightSum += subject.weight
            gradeAccumulator += subject.weight * subjectGrade.grade
          }
          case _ => {}
        }
      }
      val gradeAccumulatorDoubleVal: Double = gradeAccumulator / 10.0
      val averageGrade: Double = (gradeAccumulatorDoubleVal / weightSum)
      Some(Grade(averageGrade))
    } else {
      None
    }
  }

  def +=(subject: Subject[_ <: Passable]): Unit = {
    subjects += subject
  }

  def +=(subjectList: List[Subject[_ <: Passable]]): Unit = {
    subjects ++= subjectList
  }

  var subjects: ListBuffer[Subject[_ <: Passable]] = new ListBuffer[Subject[_ <: Passable]]()

  def size: Int = subjects.size
}
