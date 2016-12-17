package smartii.lightwave.benchmark

import java.net._

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import akka.io.{IO, Udp}
import smartii.lightwave.{Lookup, MessageDigester}
import akka.event.Logging
import com.lightwaverf.api.model.DeviceMessage
import smartii.lightwave.model._

object LightwaveReceiver {

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("Sys", ConfigFactory.load("remotelookup"))
    system.actorOf(Props[LightwaveReceiver], "rcv")
  }
}

class LightwaveReceiver extends Actor with ActorLogging {
  import context.system
  import Sender._

  private val LightwaveUdpBroadcastPort = 9761

  private val lookupService: Lookup = new Lookup {
    override def getRoomName(id: Int) = ???

    override def getDeviceName(id: Int) = ???
  }

  IO(Udp) ! Udp.Bind(self, new InetSocketAddress(LightwaveUdpBroadcastPort))

  def receive = {
    case Udp.Bound(local) => context.become(ready(sender()))
    case s: String => sender() ! s
    case m: Echo  => sender() ! m
    case Shutdown => context.system.terminate()
    case _        =>
  }

  private def ready(socket: ActorRef): Receive = {
    case Udp.Received(data, remote) =>

      DeviceMessage(data.utf8String) match {
        case msg @ isDimEvent() => { DimmerDevEvent(msg).run(lookupService)  }
        case msg @ isOnOffEvent() => { OnOffDevEvent(msg).run(lookupService)  }
        case unknownMsg => log.error(new IllegalArgumentException(s"Unable to deserialise message: $unknownMsg"), s"Unable to deserialise message: $unknownMsg")
      }

    //      val processed = // parse data etc., e.g. using PipelineStage
    //        socket ! Udp.Send(data, remote) // example server echoes back
    //      nextActor ! processed
    case Udp.Unbind  => socket ! Udp.Unbind
    case Udp.Unbound => context.stop(self)
  }
}

