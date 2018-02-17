package com.incontact.utilities

import com.typesafe.scalalogging.LazyLogging

/**
  * Created by sandip.bhattacharjee on 8/6/2017.
  */

/**Logging to execution.lo file
  * uses lazylogging to log execution events
  */
object Logger extends LazyLogging{
  /**
    * Log message as info to log file
    * @param message message to log
    */
  def info(message : String): Unit ={
    logger.info(message)
    println(message)
  }
  /**
    * Log message as warning to log file
    * @param message message to log
    */
  def warn(message : String): Unit ={
    logger.warn(message)
    println(message)
  }

  /**
    * Log message as error to log file
    * @param message message to log
    */
  def error(message : String): Unit ={
    logger.error(message)
    println(message)
  }

}
