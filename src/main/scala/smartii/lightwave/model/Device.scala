package smartii.lightwave.model

/**
  * Created by jimbo on 14/12/16.
  */
sealed trait Device

case class OnOff(id: Int, name: String, room: Room) extends Device

case class Dimmer(id: Int, name: String, room: Room) extends Device
