package smartii.roku

import java.net._
import java.nio.channels.DatagramChannel

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.io.Inet.{DatagramChannelCreator, SocketOptionV2}
import akka.io.{IO, Udp, UdpConnected}
import akka.util.ByteString
import smartii.lightwave.Lookup
import smartii.lightwave.benchmark.Sender

import scala.collection.JavaConversions._

object SSDPListener {
  def main(args: Array[String]): Unit = {
    /* create byte arrays to hold our send and response data */
    var sendData = new Array[Byte](1024)
    var receiveData = new Array[Byte](1024)

    /* our M-SEARCH data as a byte array */
    val MSEARCH = "M-SEARCH * HTTP/1.1\nHost: 239.255.255.250:1900\nMan: \"ssdp:discover\"\nST: roku:ecp\n";
    sendData = MSEARCH.getBytes

    /* create a packet from our data destined for 239.255.255.250:1900 */
    val sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("239.255.255.250"), 1900);

    println("sending")
    /* send packet to the socket we're creating */
    val clientSocket = new DatagramSocket();
    clientSocket.send(sendPacket);

    println("receiving")
    /* recieve response and store in our receivePacket */
    val receivePacket = new DatagramPacket(receiveData, receiveData.length);
    clientSocket.receive(receivePacket);

    println("extracting response")
    /* get the response as a string */
    val response = new String(receivePacket.getData());

    /* print the response */
    println(response);

    /* close the socket */
    clientSocket.close();
  }
}

/**
  * Created by jimbo on 15/01/17.
  */
class SSDPListener extends Actor with ActorLogging {
  import Sender._
  import context.system

  val remote = new InetSocketAddress(1900)
  IO(Udp) ! Udp.Bind(self, remote, List(new DatagramChannelCreator {
    override def create() =
      DatagramChannel.open(StandardProtocolFamily.INET)
  }, new SocketOptionV2 {
    override def afterBind(s: DatagramSocket) {
      val group = InetAddress.getByName("239.255.255.250")
      val networkInterface = NetworkInterface.getByName("wlx30b5c21fa799")
      s.getChannel.join(group, networkInterface)
    }
  }))
//  IO(UdpConnected) ! UdpConnected.Connect(self, new InetSocketAddress("239.255.255.250", 1900))

  println("SSDPListener sending msg")

  self ! """M-SEARCH * HTTP/1.1
           |Host: 239.255.255.250:1900
           |Man: "ssdp:discover"
           |ST: roku:ecp
           |""".stripMargin

  def receive = {
    case Udp.Bound(local) => context.become(ready(sender()))
    case s: String => sender() ! s
    case m: Echo  => sender() ! m
    case Shutdown => context.system.terminate()
    case _        =>
  }

  def ready(socket: ActorRef): Receive = {
    case Udp.Received(data, remote) => println("Received:\n" + data.utf8String)
    // process data, send it on, etc.
    case msg: String => println("Sending:\n" + msg)
      socket ! Udp.Send(ByteString(msg), remote)
    case Udp.Unbind  => socket ! Udp.Unbind
    case Udp.Unbound => context.stop(self)
  }
}
