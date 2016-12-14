package smartii.lightwave

/**
  * Created by jimbo on 14/12/16.
  */
object CommsPatterns {


  def isDeviceOnOff: PartialFunction[String, Int] = { case s => 1 }
}
