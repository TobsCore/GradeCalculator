package tobscore.sideprojects.gradecalc

import tobscore.sideprojects.gradecalc.grade.{Grade,Fail, Pass, PassFail}

import scala.util.control.Breaks._
import scala.collection.mutable.ListBuffer

/**
  * Created by Tobias Kerst on 06.03.17.
  */
class Module(name: String, professor: String) {


  def result: scala.Option[_ <: PassFail] = {
    def containsFailedSubject(): Boolean = {
      subjects.filter(_.isFail.getOrElse(true)).nonEmpty
    }
    def containsGrades(): Boolean = {
      subjects.map(_.result.get).filter(_.isInstanceOf[Grade]).nonEmpty
    }

    var gradeAccumulator: Int = 0
    var gradeWeight: Int = 0
    var result: Option[_ <: PassFail] = None

    if (subjects.isEmpty) {
      None
    } else if (containsFailedSubject()) {
      Some(Fail())
    } else if (!containsGrades()) {
      Some(Pass())
    } else if (containsGrades()) {

      var weightSum = 0
      var gradeAccumulator = 0.0
      subjects.map(_.result.get).filter(_.isInstanceOf[Grade]).map(_.asInstanceOf[Grade]).foldLeft(0.0)((a:Double, b: Grade) => b.grade + a)

      for (subject <- subjects) {
        subject.result.get match {
          case subjectGrade: Grade => {
            weightSum += subject.weight
            gradeAccumulator += subject.weight * subjectGrade.grade
          }
          case _ => {}
        }
      }
      val averageGrade:Int = (gradeAccumulator / weightSum).toInt
      Some(Grade(averageGrade))
    } else {
      None
    }
  }

  def +=(subject: Subject[_ <: PassFail]): Unit = {
    subjects += subject
  }

  def +=(subjectList: List[Subject[_ <: PassFail]]): Unit = {
    subjects ++= subjectList
  }

  var subjects: ListBuffer[Subject[_ <: PassFail]] = new ListBuffer[Subject[_ <: PassFail]]()

  def size: Int = subjects.size
}
