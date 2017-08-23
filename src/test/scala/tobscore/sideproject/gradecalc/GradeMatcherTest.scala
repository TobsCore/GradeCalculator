package tobscore.sideproject.gradecalc

import org.scalatest.FunSuite
import tobscore.sideprojects.gradecalc.grade.GradeMatcher

class GradeMatcherTest extends FunSuite{

  test("Correct Grade pattern matching") {
    assert(GradeMatcher("1.3").matches())
  }

  test("Alphabet") {
    assert(!GradeMatcher("abc").matches())
  }

  test("Longer Double") {
    assert(!GradeMatcher("1.30").matches())
  }

  test("Integer") {
    assert(GradeMatcher("1").matches())
  }

  test("Unfinished Double") {
    assert(!GradeMatcher("1.").matches())
  }

  test("Large Number") {
    assert(!GradeMatcher("123").matches())
  }

  test("Integer correct") {
    assert(GradeMatcher("1").isCorrect())
  }

  test("Integer incorrect"){
    assert(!GradeMatcher("7").isCorrect())
  }

  test("Integer too small") {
    assert(!GradeMatcher("0").isCorrect())
  }

  test("Integer negative") {
    assert(!GradeMatcher("-1").isCorrect())
  }

  test("Double too small") {
    assert(!GradeMatcher("0.7").isCorrect())
  }

  test("Double 1.0 is correct") {
    assert(GradeMatcher("1.0").isCorrect())
  }

  test("Double 5.0 is correct") {
    assert(GradeMatcher("5.0").isCorrect())
  }
}
