package controllers

import models.Certificates
import org.junit.runner.RunWith
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.Application
import play.api.test.{FakeRequest, WithApplication}
import services._
import org.mockito.Mockito._
import org.specs2.mock.Mockito
import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.test._
import play.api.test.Helpers._
import scala.concurrent.{Future, Await}
import scala.concurrent.ExecutionContext.Implicits.global
/**
  * Created by akash on 10/3/16.
  */
@RunWith(classOf[JUnitRunner])
class HomeControllerSpec extends Specification with Mockito {


  "Home Controller " should {

    val certificate = Certificates(1,1,"Some","Some",2013)
    val loginService=mock[LoginServiceApi]
    val assignmentService = mock[AssignmentServiceApi]
    val certificateService = mock[CertificateServiceApi]
    val languageService = mock[LanguageServiceApi]
    val programmingService = mock[ProgrammingServiceApi]

    val controller=new HomeController(loginService,certificateService,languageService,assignmentService,programmingService)

    "Show the Home Page" in new WithApplication {
      val result = controller.index()
      val home = route(FakeRequest(GET, "/").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"1")).get
      status(home) must equalTo(SEE_OTHER)
    }

    "Show the Login Page" in new WithApplication {
      val result = controller.index()
      val home = route(FakeRequest(GET, "/login")).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
      contentAsString(home) must contain ("Login")
    }

    "process the Login Page" in new WithApplication {
      val result = controller.index()
      val login = route(FakeRequest(POST, "/loginsubmit").withFormUrlEncodedBody("email" -> "Akash@gmail.com","password" -> "AKash")).get
      status(login) must equalTo(SEE_OTHER)
    }
    "process the Login Page with Wrong Data" in new WithApplication {
      val result = controller.index()
      val login = route(FakeRequest(POST, "/loginsubmit").withFormUrlEncodedBody("email" -> "Aash@gmail.com","password" -> "AKash")).get
      status(login) must equalTo(SEE_OTHER)
    }
    "process the Login Page with Empty Data" in new WithApplication {
      val result = controller.index()
      val login = route(FakeRequest(POST, "/loginsubmit").withFormUrlEncodedBody("email" -> "","password" -> "AKash")).get
      status(login) must equalTo(BAD_REQUEST)
    }
    "Show the Certificate Page" in new WithApplication {
      val result = controller.index()
      val login = route(FakeRequest(GET, "/certificates").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"1")).get
      status(login) must equalTo(OK)
      contentType(login) must beSome.which(_ == "text/html")
      contentAsString(login) must contain ("Certificates")
    }
    "Show the Certificate Page with Mockito" in new WithApplication {
      when(certificateService.createCertificateTable()).thenReturn(true)
      val result = controller.index()
      val login = route(FakeRequest(GET, "/certificates").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"1")).get
      status(login) must equalTo(OK)
      contentType(login) must beSome.which(_ == "text/html")
      contentAsString(login) must contain ("Certificates")
    }
    "process the Certificate Page with Valid Data " in new WithApplication {
      val result = controller.index()
      val login = route(FakeRequest(POST, "/addcertificates").withFormUrlEncodedBody("id" -> "1","userId" -> "1","name"->"Some","description"->"Some","year"->"2013")).get
      status(login) must equalTo(OK)
    }
    "process the Certificate Page with Valid Data with Mock" in new WithApplication {
      when(certificateService.insertCertificate(certificate)).thenReturn(Future{1})
      when(certificateService.getCertificateByUser(1)).thenReturn(Future(List()))
      val result = controller.index()
      val login = route(FakeRequest(POST, "/addcertificates").withFormUrlEncodedBody("id" -> "1","userId" -> "1","name"->"Some","description"->"Some","year"->"2013")).get
      status(login) must equalTo(OK)
    }
    "process the Certificate Page with InValid Data with Mock" in new WithApplication {
      when(certificateService.insertCertificate(certificate)).thenReturn(Future{1})
      when(certificateService.getCertificateByUser(1)).thenReturn(Future(List()))
      val result = controller.index()
      val login = route(FakeRequest(POST, "/addcertificates").withFormUrlEncodedBody("id" -> "","userId" -> "1","name"->"Some","description"->"Some","year"->"2013")).get
      status(login) must equalTo(OK)
    }
    "process the Edit Certificate Page with Valid Data" in new WithApplication {
      val result = controller.index()
      val login = route(FakeRequest(POST, "/editCertificate").withFormUrlEncodedBody("id" -> "1","userId" -> "1","name"->"Some","description"->"Some","year"->"2013")).get
      status(login) must equalTo(OK)
    }
    "process the Edit Certificate Page with Valid Data with Mock" in new WithApplication {
      when(certificateService.insertCertificate(certificate)).thenReturn(Future{1})
      when(certificateService.getCertificateByUser(1)).thenReturn(Future(List()))
      val result = controller.index()
      val login = route(FakeRequest(POST, "/editCertificate").withFormUrlEncodedBody("id" -> "1","userId" -> "1","name"->"Some","description"->"Some","year"->"2013")).get
      status(login) must equalTo(OK)
    }
    "process the Edit Certificate Page with InValid Data with Mock" in new WithApplication {
      when(certificateService.insertCertificate(certificate)).thenReturn(Future{1})
      when(certificateService.getCertificateByUser(1)).thenReturn(Future(List()))
      val result = controller.index()
      val login = route(FakeRequest(POST, "/editCertificate").withFormUrlEncodedBody("id" -> "","userId" -> "1","name"->"Some","description"->"Some","year"->"2013")).get
      status(login) must equalTo(OK)
    }
    "Send the List of Certificates " in new WithApplication {
      val result = controller.index()
      val home = route(FakeRequest(GET, "/getCert")).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
    }
    "Send the List of Certificates with Mockito" in new WithApplication {
      when(certificateService.getCertificateByUser(1)).thenReturn(Future(List()))
      val result = controller.index()
      val home = route(FakeRequest(GET, "/getCert")).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
    }
    "Send the List of Assignment " in new WithApplication {
      val result = controller.index()
      val home = route(FakeRequest(GET, "/getAssignment")).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
    }
    "Send the List of Assignment with Mockito" in new WithApplication {
      when(assignmentService.getAssignmentByUser(1)).thenReturn(Future(List()))
      val result = controller.index()
      val home = route(FakeRequest(GET, "/getAssignment")).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
    }
    "Send the List of Languages " in new WithApplication {
      val result = controller.index()
      val home = route(FakeRequest(GET, "/getLang")).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
    }
    "Send the List of Languages with Mockito" in new WithApplication {
      when(languageService.getLanguageByUser(1)).thenReturn(Future(List()))
      val result = controller.index()
      val home = route(FakeRequest(GET, "/getLang")).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
    }
    "Send the List of ProgrammingLanguages " in new WithApplication {
      val result = controller.index()
      val home = route(FakeRequest(GET, "/getProgramming")).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
    }
    "Send the List of PogrammingLanguages with Mockito" in new WithApplication {
      when(programmingService.getProgrammingByUser(1)).thenReturn(Future(List()))
      val result = controller.index()
      val home = route(FakeRequest(GET, "/getProgramming")).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
    }
    "Send the Language Page" in new WithApplication {
      val result = controller.index()
      val login = route(FakeRequest(GET, "/languages").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"1")).get
      status(login) must equalTo(OK)
      contentType(login) must beSome.which(_ == "text/html")
      contentAsString(login) must contain ("Languages")
    }
    "Send the Language Page with Mockito" in new WithApplication {
      when(languageService.createLanguageTable()).thenReturn(true)
      val result = controller.index()
      val login = route(FakeRequest(GET, "/languages").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"1")).get
      status(login) must equalTo(OK)
      contentType(login) must beSome.which(_ == "text/html")
      contentAsString(login) must contain ("Languages")
    }
    "Send the Assignment Page" in new WithApplication {
      val result = controller.index()
      val login = route(FakeRequest(GET, "/assignments").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"1")).get
      status(login) must equalTo(OK)
      contentType(login) must beSome.which(_ == "text/html")
      contentAsString(login) must contain ("Assignment")
    }
    "Send the Assignment Page with Mockito" in new WithApplication {
      when(assignmentService.createAssignmentTable()).thenReturn(true)
      val result = controller.index()
      val login = route(FakeRequest(GET, "/assignments").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"1")).get
      status(login) must equalTo(OK)
      contentType(login) must beSome.which(_ == "text/html")
      contentAsString(login) must contain ("Assignment")
    }
    "Send the Programming Page" in new WithApplication {
      val result = controller.index()
      val login = route(FakeRequest(GET, "/programming").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"1")).get
      status(login) must equalTo(OK)
      contentType(login) must beSome.which(_ == "text/html")
      contentAsString(login) must contain ("Programming")
    }
    "Send the Programming Page with Mockito" in new WithApplication {
      when(programmingService.createProgrammingTable()).thenReturn(true)
      val result = controller.index()
      val login = route(FakeRequest(GET, "/programming").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"1")).get
      status(login) must equalTo(OK)
      contentType(login) must beSome.which(_ == "text/html")
      contentAsString(login) must contain ("Programming")
    }

  }

}
