package smartii.lightwave.model

/**
  * Created by jimbo on 14/12/16.
  */
sealed trait DeviceEvent {
  def device: Device
  def event: Event
}

case class OnDevEvent(device: OnOff) extends DeviceEvent {
  override def event: Event = On
}

case class OffDevEvent(device: OnOff) extends DeviceEvent {
  override def event: Event = Off
}

case class DimmerDevEvent(device: Dimmer, event: Dim) extends DeviceEvent