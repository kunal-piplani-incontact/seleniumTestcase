package com.incontact.metrics

/**
  * Created by sbhattacharjee on 11/17/2017.
  */
class Metrics(_endpoint:String, _responseTime:String, _responsePercentage:String, _passPercantage:String) {

  val endpoint = _endpoint
  val responseTime = _responseTime.toInt
  val responsePercentage = _responsePercentage.toInt
  val passPercentage = _passPercantage.toInt

}
