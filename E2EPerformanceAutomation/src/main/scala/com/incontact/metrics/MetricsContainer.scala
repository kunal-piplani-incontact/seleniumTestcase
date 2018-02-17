package com.incontact.metrics

import scala.collection.mutable

/**
  * Created by sbhattacharjee on 11/17/2017.
  */
object MetricsContainer {

  private var metricsContainer = new mutable.HashMap[String, Metrics]()

  def addToContainer(metrics: Metrics): Unit ={
    metricsContainer += (metrics.endpoint -> metrics)
  }


  def getMetrics(endpoint : String) : Metrics= {
    metricsContainer(endpoint)
  }
}
