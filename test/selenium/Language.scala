package selenium

import java.util.concurrent.TimeUnit

import org.openqa.selenium.firefox.FirefoxDriver
import org.scalatest.FlatSpec
/**
  * Created by prabhat on 11/3/16.
  */
class Language extends FlatSpec {

  val baseUrl = "http://localhost:9000/"

  "User" should "hit language page" in{
    val driver = new FirefoxDriver()
    driver.get(baseUrl)
    driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS)
    driver.findElementById("email").sendKeys("prabhatkashyap33@gmail.com")
    driver.findElementById("password").sendKeys("test123")
    driver.findElementByClassName("btn-default").click()
    driver.findElementByClassName("glyphicon-comment").click()
    driver.findElementByCssSelector("BODY").getText().contains("Languages")
    Thread.sleep(2000)
    driver.kill()
  }

  "User" should "add a new language" in{
    val driver = new FirefoxDriver()
    driver.get(baseUrl)
    driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS)
    driver.findElementById("email").sendKeys("prabhatkashyap33@gmail.com")
    driver.findElementById("password").sendKeys("test123")
    Thread.sleep(2000)
    driver.findElementByClassName("btn-default").click()
    Thread.sleep(2000)
    driver.findElementByClassName("glyphicon-comment").click()
    Thread.sleep(2000)
    driver.findElementByClassName("btn-success").click()
    Thread.sleep(5000)
    driver.findElementByXPath("/html/body/div[4]/div/div/div[2]/form/dl[3]/dd[1]/input").sendKeys("ThisIsTest")
    driver.findElementByXPath("/html/body/div[4]/div/div/div[2]/form/dl[4]/dd[1]/input").sendKeys("Test")
    driver.findElementByXPath("/html/body/div[4]/div/div/div[2]/form/button").click()
    driver.findElementByCssSelector("BODY").getText().contains("ThisIsTest")
    Thread.sleep(2000)
    driver.kill()
  }

  "User" should "edit a previous language" in{
    val driver = new FirefoxDriver()
    driver.get(baseUrl)
    driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS)
    driver.findElementById("email").sendKeys("prabhatkashyap33@gmail.com")
    driver.findElementById("password").sendKeys("test123")
    Thread.sleep(2000)
    driver.findElementByClassName("btn-default").click()
    Thread.sleep(2000)
    driver.findElementByClassName("glyphicon-comment").click()
    Thread.sleep(2000)
    driver.findElementByClassName("editButton").click()
    Thread.sleep(5000)
    driver.findElementByXPath("/html/body/div[3]/div/div/div[2]/form/dl[3]/dd[1]/input").sendKeys("Changed")
    driver.findElementByXPath("/html/body/div[3]/div/div/div[2]/form/button").click()
    driver.findElementByCssSelector("BODY").getText().contains("Changed")
    Thread.sleep(2000)
    driver.kill()
  }

  "User" should "delete a language" in{
    val driver = new FirefoxDriver()
    driver.get(baseUrl)
    driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS)
    driver.findElementById("email").sendKeys("prabhatkashyap33@gmail.com")
    driver.findElementById("password").sendKeys("test123")
    Thread.sleep(2000)
    driver.findElementByClassName("btn-default").click()
    Thread.sleep(2000)
    driver.findElementByClassName("glyphicon-comment").click()
    Thread.sleep(2000)
    driver.findElementByClassName("deleteButton").click()
    !driver.findElementByCssSelector("BODY").getText().contains("Changed")
    Thread.sleep(2000)
    driver.kill()
  }

}