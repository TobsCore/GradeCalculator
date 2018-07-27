package tobscore.sideprojects.gradecalc

import java.io.{FileInputStream, FileOutputStream, ObjectInputStream, ObjectOutputStream}

class Serializer[T]() {

  def serialize(obj: T, path: String): Unit = {
    val oos = new ObjectOutputStream(new FileOutputStream(path))
    oos.writeObject(obj)
    oos.close()
  }

  def deserialize(path: String): T = {
    val ois = new ObjectInputStream(new FileInputStream(path))
    val obj = ois.readObject().asInstanceOf[T]
    ois.close()
    obj
  }

}
