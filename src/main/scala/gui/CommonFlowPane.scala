package gui

import scalafx.beans.binding.NumberBinding
import scalafx.geometry.Insets
import scalafx.scene.Node
import scalafx.scene.control.Control
import scalafx.scene.layout.FlowPane

import scala.collection.mutable.ArrayBuffer

/**
  * Created by linsixin on 2019/3/23.
  */
class CommonFlowPane(_width: NumberBinding, _height: NumberBinding) extends FlowPane {

  prefWidth <== _width
  prefHeight <== _height

  hgap = 10
  vgap = 10

  val smallMargin = 5
  val leftWidth = _width / 4 - hgap - smallMargin
  val rightWidth = _width - leftWidth - hgap - smallMargin
  val nodeHeight = 30

  val topNodes = ArrayBuffer[Control]()
  val leftNodes = ArrayBuffer[Control]()
  val rightNodes = ArrayBuffer[Control]()
  val bottomNodes = ArrayBuffer[Control]()

  def init() = {
    leftNodes.foreach { node =>
      node.margin = Insets(10,0,0,0)
      node.prefWidth <== leftWidth
      node.prefHeight = nodeHeight
    }

    rightNodes.foreach { node =>
      node.prefWidth <== rightWidth
      node.prefHeight = nodeHeight
    }

    (topNodes ++ bottomNodes).par.foreach { node =>
      node.prefWidth <== _width - 2 * smallMargin
      node.prefHeight = nodeHeight
      node.margin = Insets(0,0,0,smallMargin)
    }

    children = topNodes ++
      leftNodes.view.zip(rightNodes).flatMap { case (left,right) => ArrayBuffer(left,right)} ++
      bottomNodes
  }

  init()

}
