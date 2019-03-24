package entity

import java.util.Date

/**
  * Created by linsixin on 2019/3/23.
  */
case class Message(content:String, createTime:Date = new Date())

object Message {
  val CLOSE_CONTENT = "#CLOSE#"
  val CLOSE = Message(CLOSE_CONTENT, new Date())
}
