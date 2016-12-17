package smartii.lightwave.model

import cats.data.{Kleisli, Reader}
import com.lightwaverf.api.model.DeviceMessage
import smartii.lightwave.Lookup

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

object DimmerDevEvent {

  def apply(deviceMessage: DeviceMessage):  Reader[Lookup, DimmerDevEvent] = deviceMessage match {
    case deviceMessage if deviceMessage.fn == "dim" => {
      Dimmer(deviceMessage).flatMap[DimmerDevEvent](k => k.map[DimmerDevEvent](d => DimmerDevEvent(d, Dim(deviceMessage.param.get))))
    }
  }
}

object isOnEvent {

  def unapply(deviceMessage: DeviceMessage): Boolean = deviceMessage.fn == "on"
}

object isOffEvent {

  def unapply(deviceMessage: DeviceMessage): Boolean = deviceMessage.fn == "off"
}

object isDimEvent {

  def unapply(deviceMessage: DeviceMessage): Boolean = deviceMessage.fn == "dim"
}

case class DimmerDevEvent(device: Dimmer, event: Dim) extends DeviceEvent