package selenium

import java.util.concurrent.TimeUnit

import org.openqa.selenium.firefox.FirefoxDriver
import org.scalatest.FlatSpec
/**
  * Created by prabhat on 11/3/16.
  */
class Assignment extends FlatSpec {

  val baseUrl = "http://localhost:9000/"

  "User" should "hit Assignment page" in{
    val driver = new FirefoxDriver()
    driver.get(baseUrl)
    driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS)
    driver.findElementById("email").sendKeys("prabhatkashyap33@gmail.com")
    driver.findElementById("password").sendKeys("test123")
    driver.findElementByClassName("btn-default").click()
    driver.findElementByClassName("glyphicon-folder-open").click()
    driver.findElementByCssSelector("BODY").getText().contains("Assignments")
    Thread.sleep(2000)
    driver.kill()
  }

}