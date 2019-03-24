package gui

import scalafx.beans.binding.NumberBinding
import scalafx.scene.control.{Button, Label, TextField}
import scalafx.scene.layout.FlowPane

/**
  * Created by linsixin on 2019/3/23.
  */
class PersonalInfoPane(_width: NumberBinding, _height: NumberBinding) extends CommonFlowPane(_width,_height) {

  val username: Label = centerAlignLabel("用户名")
  val textField = new TextField()

  val okBtn = new Button("确定")

  leftNodes += username
  rightNodes += textField
  bottomNodes += okBtn

  init()

}
