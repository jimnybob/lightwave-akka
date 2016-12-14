package smartii.lightwave

import org.scalatest.FlatSpec

/**
  * Created by jimbo on 14/12/16.
  */
class CommsPatternSpec extends FlatSpec {

  "CommsPattern matcher" should "match light on" in {

    """*!{"trans":2,"mac":"03:3F:75","time":1481739529,"pkt":"433T","fn":"on","room":3,"dev":1}"""
  }

  it should "match light dim 40%" in {

    """*!{"trans":3,"mac":"03:3F:75","time":1481739779,"pkt":"433T","fn":"dim","param":13,"room":1,"dev":1}"""
  }

  it should "match light dim 100%" in {

    """*!{"trans":3,"mac":"03:3F:75","time":1481739779,"pkt":"433T","fn":"dim","param":32,"room":1,"dev":1}"""
  }

  it should "match dusk/dawn messages" in {

    """*!{"trans":11,"mac":"03:3F:75","time":1481740551,"pkt":"system","fn":"hubCall","type":"hub","prod":"wfl","fw":"U2.93Z","uptime":3660,"timeZone":0,"lat":52.48,"long":-1.86,"tmrs":3,"evns":3,"run":0,"macs":5,"ip":"192.168.0.2","devs":1}
      |"""
  }
}
