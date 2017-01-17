package com.lightwaverf.api.model

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.lightwaverf.api.model.DeviceMessageJson.jsonFormat8
import spray.json._

import scala.util.Try

/**
  * Created by jimbo on 15/12/16.
  */
sealed trait LightwaveMessage {
  def trans: Int
  def mac: String
  def time: Long
  def pkt: String
}

object DeviceMessageJson extends SprayJsonSupport with DefaultJsonProtocol {

  implicit val format = jsonFormat8(DeviceMessage.apply/*.apply(_: Int, _: String, _: Long, _: String, _: String, _: Int, _: Int, _: Option[Int])*/)
}

object DeviceMessage {

  def apply2: PartialFunction[String, DeviceMessage] = {
    case message: String if message.startsWith("*!") && Try(message.drop(2).parseJson.convertTo[DeviceMessage](DeviceMessageJson.format)).isSuccess => message.drop(2).parseJson.convertTo[DeviceMessage](DeviceMessageJson.format)
    case otherMsg => throw new MatchError(s"Lightwave messages should start with '*!'. Message is: $otherMsg")
  }

  def apply(message: String): DeviceMessage = message match {
    case message: String if message.startsWith("*!") && Try(message.drop(2).parseJson.convertTo[DeviceMessage](DeviceMessageJson.format)).isSuccess => message.drop(2).parseJson.convertTo[DeviceMessage](DeviceMessageJson.format)
    case otherMsg => throw new MatchError(s"Lightwave messages should start with '*!'.  Message is: $otherMsg")
  }
}

case class DeviceMessage(trans: Int, mac: String, time: Long, pkt: String, fn: String, room: Int, dev: Int, param: Option[Int]) extends LightwaveMessage
