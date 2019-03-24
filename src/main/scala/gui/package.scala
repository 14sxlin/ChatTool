import scalafx.application.Platform
import scalafx.geometry.Pos
import scalafx.scene.control.Label


/**
  * Created by linsixin on 2019/3/23.
  */
package object gui {

  def centerAlignLabel(text:String): Label = new Label(text) {
    alignment = Pos.CenterRight
  }

  def runLater(run : => Unit): Unit = {
    Platform.runLater(run)
  }

}
