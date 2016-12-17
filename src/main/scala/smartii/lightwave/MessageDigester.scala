package smartii.lightwave

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.lightwaverf.api.model.DeviceMessage
import spray.json._
import com.lightwaverf.api.model.DeviceMessageJson._

import scala.util.Try

/**
  * Created by jimbo on 14/12/16.
  */
object MessageDigester extends SprayJsonSupport with DefaultJsonProtocol {

  def deviceMessage: PartialFunction[String, DeviceMessage] = {
    case message: String if message.startsWith("*!") && Try(message.drop(2).parseJson.convertTo[DeviceMessage]).isSuccess => message.drop(2).parseJson.convertTo[DeviceMessage]
  }
}
