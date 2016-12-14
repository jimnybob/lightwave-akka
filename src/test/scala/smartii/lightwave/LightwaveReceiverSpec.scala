package smartii.lightwave

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.WordSpecLike
import org.scalatest.Matchers
import smartii.lightwave.benchmark.LightwaveReceiver

import scala.concurrent.duration._

/**
  * Created by jimbo on 14/12/16.
  */
class LightwaveReceiverSpec extends TestKit(ActorSystem("LightwaveReceiverSpec")) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  val actorRef = TestActorRef(new LightwaveReceiver)

  override def afterAll {
    shutdown()
  }

  "A LightwaveReceiver actor" should {
    "Respond with the same message it receives" in {
      within(500 millis) {
        actorRef ! "test"
        expectMsg("test")
      }
    }
  }
}
