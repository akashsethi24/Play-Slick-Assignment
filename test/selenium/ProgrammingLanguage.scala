package selenium

import java.util.concurrent.TimeUnit

import org.openqa.selenium.firefox.FirefoxDriver
import org.scalatest.FlatSpec
/**
  * Created by prabhat on 11/3/16.
  */
class ProgrammingLanguage extends FlatSpec {

  val baseUrl = "http://localhost:9000/"

  "User" should "hit programming language page" in{
    val driver = new FirefoxDriver()
    driver.get(baseUrl)
    driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS)
    driver.findElementById("email").sendKeys("prabhatkashyap33@gmail.com")
    driver.findElementById("password").sendKeys("test123")
    driver.findElementByClassName("btn-default").click()
    driver.findElementByClassName("glyphicon-floppy-disk").click()
    driver.findElementByCssSelector("BODY").getText().contains("Programming Languages")
    Thread.sleep(1000)
    driver.kill()
  }

  "User" should "add a new programming language" in{
    val driver = new FirefoxDriver()
    driver.get(baseUrl)
    driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS)
    driver.findElementById("email").sendKeys("prabhatkashyap33@gmail.com")
    driver.findElementById("password").sendKeys("test123")
    Thread.sleep(2000)
    driver.findElementByClassName("btn-default").click()
    Thread.sleep(2000)
    driver.findElementByClassName("glyphicon-floppy-disk").click()
    Thread.sleep(2000)
    driver.findElementByClassName("btn-success").click()
    Thread.sleep(2000)
    driver.findElementByXPath("/html/body/div[4]/div/div/div[2]/form/dl[3]/dd[1]/input").sendKeys("ThisIsTest")
    driver.findElementByXPath("/html/body/div[4]/div/div/div[2]/form/dl[4]/dd[1]/input").sendKeys("Test")
    driver.findElementByXPath("/html/body/div[4]/div/div/div[2]/form/button").click()
    driver.findElementByCssSelector("BODY").getText().contains("ThisIsTest")
    Thread.sleep(1000)
    driver.kill()
  }

  "User" should "edit a previous programming Language" in{
    val driver = new FirefoxDriver()
    driver.get(baseUrl)
    driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS)
    driver.findElementById("email").sendKeys("prabhatkashyap33@gmail.com")
    driver.findElementById("password").sendKeys("test123")
    Thread.sleep(2000)
    driver.findElementByClassName("btn-default").click()
    Thread.sleep(2000)
    driver.findElementByClassName("glyphicon-floppy-disk").click()
    Thread.sleep(2000)
    driver.findElementByClassName("editButton").click()
    Thread.sleep(2000)
    driver.findElementByXPath("/html/body/div[3]/div/div/div[2]/form/dl[3]/dd[1]/input").sendKeys("Changed")
    driver.findElementByXPath("/html/body/div[3]/div/div/div[2]/form/dl[4]/dd[1]/input").sendKeys("1992")
    driver.findElementByXPath("/html/body/div[3]/div/div/div[2]/form/button").click()
    driver.findElementByCssSelector("BODY").getText().contains("Changed")
    Thread.sleep(1000)
    driver.kill()
  }

  "User" should "delete a certificate" in{
    val driver = new FirefoxDriver()
    driver.get(baseUrl)
    driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS)
    driver.findElementById("email").sendKeys("prabhatkashyap33@gmail.com")
    driver.findElementById("password").sendKeys("test123")
    Thread.sleep(2000)
    driver.findElementByClassName("btn-default").click()
    Thread.sleep(2000)
    driver.findElementByClassName("glyphicon-floppy-disk").click()
    Thread.sleep(2000)
    driver.findElementByClassName("deleteButton").click()
    !driver.findElementByCssSelector("BODY").getText().contains("Changed")
    Thread.sleep(1000)
    driver.kill()
  }

}