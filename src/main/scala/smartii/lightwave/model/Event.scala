package smartii.lightwave.model

/**
  * Created by jimbo on 14/12/16.
  */
sealed trait Event

case class Dim(percent: Int) extends Event

case object On extends Event

case object Off extends Event

