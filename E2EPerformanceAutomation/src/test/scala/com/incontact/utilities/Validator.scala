package com.incontact.utilities

/**
  * Created by sandip.bhattacharjee on 8/10/2017.
  */

/**All implemented task keyword for validating testcase anf config file strucutre.
  * For future enhancements as explicit check is provided in GUI
  */
object Validator {

  private val requestTypes = ("Get", "Post", "Put", "Delete", "Head")
  private val simulationProfiles = ("AtOnceUser", "RampUser")
  private val simulationParameters = ("NumberofUser","Duration")
  private val requestParameter = ("QueryParam", "QueryParamFromFile","Header", "HeaderFromFile", "BasicAuthentication", "DigestAuthentication", "FormParameter", "FormParameterFromFile", "BodyString", "BodyStringFromFile")
  private val requestValidations =("ResponseTime", "CurrentUrl", "Header", "ResponseString", "Status")

}
