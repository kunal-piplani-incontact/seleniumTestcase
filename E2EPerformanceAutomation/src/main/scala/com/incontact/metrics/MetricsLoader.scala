package com.incontact.metrics

import com.google.gson.{JsonElement, JsonObject, JsonParser, JsonArray}

import scala.io.Source

/**
  * Created by sbhattacharjee on 11/17/2017.
  */
object MetricsLoader {
  def loadMetrics(): Unit ={
    val source: String = Source.fromFile("metrics/metrics.json").getLines.mkString
    val parser: JsonParser = new JsonParser
    val root: JsonArray = parser.parse(source).getAsJsonArray
    for (i <- 0 until root.size()) {
      val metrics: JsonObject = root.get(i).getAsJsonObject
      MetricsContainer.addToContainer(new Metrics(metrics.get("Endpoint").getAsString,metrics.get("ResponseTime").getAsString,metrics.get("PercentageExpected").getAsString,metrics.get("PassPercentage").getAsString))
    }
  }
}
