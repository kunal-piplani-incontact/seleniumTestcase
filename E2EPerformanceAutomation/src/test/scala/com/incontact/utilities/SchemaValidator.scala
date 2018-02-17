package com.incontact.utilities

import java.io.FileNotFoundException
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.SchemaFactory

import org.xml.sax.SAXException

/**
  * Created by sandip.bhattacharjee on 8/1/2017.
  */

/**Schema validators for config and test case files
  */
object SchemaValidator {

  /**
    * Validates config file against existing schema
    * @param xmlFile xml config file to validate
    * @return
    */
  def validateConfigFile(xmlFile : String): Boolean ={
    Logger.info("Initiating schema validation for Config File")
    validate(xmlFile,"xmlSchema\\apiConfig.xsd")

  }

  /**
    * Validates test case file against existing schema
    * @param xmlFile xml test case file to validate
    * @return
    */
  def validateTestCase(xmlFile : String): Boolean = {
    Logger.info("Initiating schema validation for Test Case File")
    validate(xmlFile,"xmlSchema\\testcase.xsd")
  }

  /**
    * Generic method to valdates xml file agaist schema
    * @param xmlFile xml file to validate
    * @param xsdFile xsd schema to validate against
    * @return
    */
  def validate(xmlFile: String, xsdFile: String): Boolean = {
    Logger.info("Validating "+xmlFile+" against schema")
    try {
        val schemaLang = "http://www.w3.org/2001/XMLSchema"
        val factory = SchemaFactory.newInstance(schemaLang)
        val schema = factory.newSchema(new StreamSource(xsdFile))
        val validator = schema.newValidator()
        validator.validate(new StreamSource(xmlFile))
      } catch {
        case ex: SAXException => Logger.error("Schema validation Failed for "+xmlFile);println(ex); return false
        case ex: FileNotFoundException => Logger.error("Invalid file location <"+xmlFile+">");println(ex);return false
        case ex: Exception => Logger.error("Unknown exception while validating "+xmlFile+"against schema");println(ex);return false
      }
    Logger.info("Schema validation completed successfully for" + xmlFile)
    true
  }
}
