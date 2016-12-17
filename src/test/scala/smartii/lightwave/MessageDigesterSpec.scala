package smartii.lightwave

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}
import smartii.lightwave.model.{On, OnOffDevEvent, isOnOffEvent}

/**
  * Created by jimbo on 14/12/16.
  */
class MessageDigesterSpec extends FlatSpec with Matchers with MockFactory {

  "MessageDigester matcher" should "marshal JSON to DeviceMessage class" in {

    val outcome = MessageDigester.deviceMessage("""*!{"trans":2,"mac":"03:3F:75","time":1481739529,"pkt":"433T","fn":"on","room":3,"dev":1}""")

    outcome.trans shouldBe 2
    outcome.mac shouldBe "03:3F:75"
    outcome.time shouldBe 1481739529
    outcome.pkt shouldBe "433T"
    outcome.fn shouldBe "on"
    outcome.room shouldBe 3
    outcome.dev shouldBe 1
    outcome.param shouldBe None
  }

  it should "match light on" in {

    val dm = MessageDigester.deviceMessage("""*!{"trans":2,"mac":"03:3F:75","time":1481739529,"pkt":"433T","fn":"on","room":3,"dev":1}""")

    val lookupService = mock[Lookup]

    (lookupService.getDeviceName _).expects(1).returns("test device")
    (lookupService.getRoomName _).expects(3).returns("test room")

    val outcome = dm match {
      case onOff @ isOnOffEvent() => OnOffDevEvent(dm).run(lookupService)
    }

    outcome.device.id shouldBe 1
    outcome.device.room.id shouldBe 3
    outcome.event shouldBe On
  }

  ignore should "match light dim 40%" in {

    """*!{"trans":3,"mac":"03:3F:75","time":1481739779,"pkt":"433T","fn":"dim","param":13,"room":1,"dev":1}"""
  }

  ignore should "match light dim 100%" in {

    """*!{"trans":3,"mac":"03:3F:75","time":1481739779,"pkt":"433T","fn":"dim","param":32,"room":1,"dev":1}"""
  }

  ignore should "match dusk/dawn messages" in {

    """*!{"trans":11,"mac":"03:3F:75","time":1481740551,"pkt":"system","fn":"hubCall","type":"hub","prod":"wfl","fw":"U2.93Z","uptime":3660,"timeZone":0,"lat":52.48,"long":-1.86,"tmrs":3,"evns":3,"run":0,"macs":5,"ip":"192.168.0.2","devs":1}
      |"""
  }

  ignore should "match events" in {
    """*!{"trans":41,"mac":"03:3F:75","time":1481804783,"pkt":"433T","fn":"allOff","room":1,"dev":1}"""
  }
}
