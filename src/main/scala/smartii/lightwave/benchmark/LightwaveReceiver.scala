package smartii.lightwave.benchmark

import java.net._
import java.nio.channels.DatagramChannel

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.io.Inet.{DatagramChannelCreator, SocketOptionV2}
import com.typesafe.config.ConfigFactory
import akka.io.{IO, Udp}

object LightwaveReceiver {

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("Sys", ConfigFactory.load("remotelookup"))
    system.actorOf(Props[LightwaveReceiver], "rcv")
  }
}

class LightwaveReceiver extends Actor {
  import context.system
  import Sender._


  private val LightwaveUdpBroadcastPort = 9761

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
      println("Akka received stuff")

      println(data.asInstanceOf[akka.util.ByteString].utf8String)
    //      val processed = // parse data etc., e.g. using PipelineStage
    //        socket ! Udp.Send(data, remote) // example server echoes back
    //      nextActor ! processed
    case Udp.Unbind  => socket ! Udp.Unbind
    case Udp.Unbound => context.stop(self)
  }
}

