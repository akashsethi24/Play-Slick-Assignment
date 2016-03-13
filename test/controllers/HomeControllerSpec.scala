package controllers

import models.{ProgrammingLanguages, Languages, User, Certificates}
import org.junit.runner.RunWith
import org.scalatest.mock.MockitoSugar
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.test.Helpers._
import play.api.test.{FakeRequest, WithApplication}
import services._
import org.mockito.Mockito._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
/**
  * Created by akash on 10/3/16.
  */
@RunWith(classOf[JUnitRunner])
class HomeControllerSpec extends Specification with MockitoSugar{


  "Home Controller " should {

    val certificate = Certificates(1,1,"Some","Some",2013)
    val language  = Languages(1,3,"Some","Average")
    val programming = ProgrammingLanguages(1,3,"Some","Average")
    val loginService=mock[LoginServiceApi]
    val assignmentService = mock[AssignmentServiceApi]
    val certificateService = mock[CertificateServiceApi]
    val languageService = mock[LanguageServiceApi]
    val programmingService = mock[ProgrammingServiceApi]

    val controller=new HomeController(loginService,certificateService,languageService,assignmentService,programmingService)

    "Show the Home Page" in new WithApplication {
      val home = route(FakeRequest(GET, "/").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"1")).get
      status(home) must equalTo(OK)
    }
    "Show the Home Page without Session Data" in new WithApplication {
      val home = route(FakeRequest(GET, "/")).get
      status(home) must equalTo(SEE_OTHER)
    }

    "Show the Login Page" in new WithApplication {
      val home = route(FakeRequest(GET, "/login")).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
      contentAsString(home) must contain ("Login")
    }
    "Show the Login Page with Session" in new WithApplication {
      val home = route(FakeRequest(GET, "/login").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"1")).get
      status(home) must equalTo(SEE_OTHER)
    }

    "process the Login Page" in new WithApplication {
      val login = route(FakeRequest(POST, "/loginsubmit").withFormUrlEncodedBody("email" -> "Akash@gmail.com","password" -> "AKash","userId"->"3")).get
      status(login) must equalTo(SEE_OTHER)
    }
    "process the Login Page with Wrong Data" in new WithApplication {
      val login = route(FakeRequest(POST, "/loginsubmit").withFormUrlEncodedBody("email" -> "Aash@gmail.com","password" -> "AKash","userId"->"3")).get
      status(login) must equalTo(SEE_OTHER)
    }
    "process the Login Page with Empty Data" in new WithApplication {
      val login = route(FakeRequest(POST, "/loginsubmit").withFormUrlEncodedBody("email" -> "","password" -> "AKash","userId"->"3")).get
      status(login) must equalTo(BAD_REQUEST)
    }
    "process the Login Page with Valid Data" in new WithApplication {
      val login = route(FakeRequest(POST, "/loginsubmit").withFormUrlEncodedBody("email" -> "Akash@gmail.com","password" -> "AKash")).get
      status(login) must equalTo(SEE_OTHER)
    }
    "process the Login Page with Valid Data and Mockito" in new WithApplication {
      when(loginService.getUserByEmail("Akash@gmail.com","AKASH")).thenReturn(Future{Some(User("Akash Sethi","Akash@gmail.com","akash","9898989898",true,Some(3)))})
      val login = call(controller.processLoginForm,FakeRequest(POST, "/loginsubmit").withFormUrlEncodedBody("email" -> "Akash@gmail.com","password" -> "AKASH"))
      status(login) must equalTo(SEE_OTHER)
    }

    "Show the Logout Page " in new WithApplication {
      val logout = route(FakeRequest(GET, "/logout")).get
      status(logout) must equalTo(SEE_OTHER)
    }
    "Show the Logout Page with Session" in new WithApplication {
      val logout = route(FakeRequest(GET, "/logout").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"1")).get
      status(logout) must equalTo(SEE_OTHER)
    }

    "Show the Certificate Page" in new WithApplication {
      val login = route(FakeRequest(GET, "/certificates").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"1")).get
      status(login) must equalTo(OK)
      contentType(login) must beSome.which(_ == "text/html")
      contentAsString(login) must contain ("Certificates")
    }
    "Show the Certificate Page without session" in new WithApplication {
      when(certificateService.createCertificateTable()).thenReturn(true)
      val login = route(FakeRequest(GET, "/certificates")).get
      status(login) must equalTo(SEE_OTHER)
    }

    "process the Certificate Page with InValid Data " in new WithApplication {
      val addCertificate = route(FakeRequest(POST, "/addcertificates").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"1").withFormUrlEncodedBody("id" -> "","userId" -> "1","name"->"Some","description"->"Some","year"->"2013")).get
      status(addCertificate) must equalTo(OK)
    }
    "process the Certificate Page with Valid Data " in new WithApplication {
      val addCertificate = route(FakeRequest(POST, "/addcertificates").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"1").withFormUrlEncodedBody("id" -> "1","userId" -> "1","name"->"Some","description"->"Some","year"->"2013")).get
      status(addCertificate) must equalTo(OK)
    }
    "process the Certificate Page with Valid Data with Mock" in new WithApplication {
      when(certificateService.insertCertificate(certificate)).thenReturn(Future{1})
      when(certificateService.getCertificateByUser(1)).thenReturn(Future(List()))
      val login = call(controller.addCertificate,FakeRequest(POST, "/addcertificates").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"1").withFormUrlEncodedBody("id" -> "1","userId" -> "1","name"->"Some","description"->"Some","year"->"2013"))
      status(login) must equalTo(OK)
    }
    "process the Certificate Page with InValid Data with Mock" in new WithApplication {
      when(certificateService.insertCertificate(certificate)).thenReturn(Future{1})
      when(certificateService.getCertificateByUser(1)).thenReturn(Future(List()))
      val login = route(FakeRequest(POST, "/addcertificates").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"1").withFormUrlEncodedBody("id" -> "","userId" -> "1","name"->"Some","description"->"Some","year"->"2013"))
      status(login.get) must equalTo(OK)
    }

    "process the Edit Certificate Page with Valid Data" in new WithApplication {
      val login = route(FakeRequest(POST, "/editCertificate").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"1").withFormUrlEncodedBody("id" -> "1","userId" -> "1","name"->"Some","description"->"Some","year"->"2013")).get
      status(login) must equalTo(OK)
    }
    "process the Edit Certificate Page with Valid Data with Mock" in new WithApplication {
      when(certificateService.updateCertificate(certificate)).thenReturn(Future{1})
      when(certificateService.getCertificateByUser(1)).thenReturn(Future(List()))
      val login = call(controller.editCertificate,FakeRequest(POST, "/editCertificate").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"1").withFormUrlEncodedBody("id" -> "1","userId" -> "1","name"->"Some","description"->"Some","year"->"2013"))
      status(login) must equalTo(OK)
    }
    "process the Edit Certificate Page with InValid Data " in new WithApplication {
      val login = route(FakeRequest(POST, "/editCertificate").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"1").withFormUrlEncodedBody("id" -> "","userId" -> "1","name"->"Some","description"->"Some","year"->"2013")).get
      status(login) must equalTo(OK)
    }

    "process the Edit Language Page with Valid Data" in new WithApplication {
      val login = route(FakeRequest(POST, "/editlanguage").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"1").withFormUrlEncodedBody("id" -> "1","userId" -> "1","name"->"Some","fluency"->"Average")).get
      status(login) must equalTo(OK)
    }
    "process the Edit Language Page with Valid Data with Mock" in new WithApplication {
      when(languageService.updateLanguage(language)).thenReturn(Future{1})
      when(certificateService.getCertificateByUser(3)).thenReturn(Future(List()))
      val login = call(controller.editCertificate,FakeRequest(POST, "/editlanguage").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"3").withFormUrlEncodedBody("id" -> "1","userId" -> "3","name"->"Some","fluency"->"Average"))
      status(login) must equalTo(OK)
    }
    "process the Edit Language Page with InValid Data " in new WithApplication {
      val login = route(FakeRequest(POST, "/editlanguage").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"3").withFormUrlEncodedBody("id" -> "","userId" -> "1","name"->"Some","description"->"Some","year"->"2013")).get
      status(login) must equalTo(OK)
    }

    "Send the List of Certificates " in new WithApplication {
      val home = route(FakeRequest(GET, "/getCert").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"3")).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
    }
    "Send the List of Certificates with Mockito" in new WithApplication {
      when(certificateService.getCertificateByUser(3)).thenReturn(Future(List()))
      val home = call(controller.showCertificates,FakeRequest(GET, "/getCert").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"3"))
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
    }

    "Send the List of Certificates By User" in new WithApplication {
      val home = route(FakeRequest(GET, "/getcertbyuser/3").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"3")).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
    }
    "Send the List of Certificates with Mockito By User" in new WithApplication {
      when(certificateService.getCertificateByUser(3)).thenReturn(Future(List()))
      val home = call(controller.getCertificateListByUser(3),FakeRequest(GET, "/getcertbyuser/3").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"3"))
      status(home) must equalTo(OK)
    }
    "Send the List of Assignment " in new WithApplication {
      val home = route(FakeRequest(GET, "/getassignment").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"3")).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
    }
    "Send the List of Assignment with Mockito" in new WithApplication {
      when(assignmentService.getAssignmentByUser(3)).thenReturn(Future(List()))
      val home = call(controller.getAssignmentList,FakeRequest(GET, "/getassignment").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"3"))
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
    }

    "Send the List of Languages " in new WithApplication {
      val home = route(FakeRequest(GET, "/getLang").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"3")).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
    }
    "Send the List of Languages with Mockito" in new WithApplication {
      when(languageService.getLanguageByUser(3)).thenReturn(Future(List()))
      val home = call(controller.getLanguageList,FakeRequest(GET, "/getLang").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"3"))
      status(home) must equalTo(OK)
    }
    "Send the List of Languages By User" in new WithApplication {
      val home = route(FakeRequest(GET, "/getlangbyuser/3").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"3")).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
    }
    "Send the List of Languages with Mockito By User" in new WithApplication {
      when(languageService.getLanguageByUser(3)).thenReturn(Future(List()))
      val home = call(controller.getLanguageListByUser(3),FakeRequest(GET, "/getlangbyuser/3").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"3"))
      status(home) must equalTo(OK)

    }
    "Send the List of ProgrammingLanguages " in new WithApplication {
      val home = route(FakeRequest(GET, "/getProgramming").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"3")).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
    }
    "Send the List of PogrammingLanguages with Mockito" in new WithApplication {
      when(programmingService.getProgrammingByUser(3)).thenReturn(Future(List()))
      val home = call(controller.getProgrammingList,FakeRequest(GET, "/getProgramming").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"3"))
      status(home) must equalTo(OK)
    }

    "Send the List of ProgrammingLanguages By User" in new WithApplication {
      val home = route(FakeRequest(GET, "/getprogbyuser/3").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"3")).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
    }
    "Send the List of PogrammingLanguages with Mockito By User" in new WithApplication {
      when(programmingService.getProgrammingByUser(3)).thenReturn(Future(List()))
      val home = call(controller.getProgrammingListByUser(3),FakeRequest(GET, "/getprogbyuser/3").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"3"))
      status(home) must equalTo(OK)
    }

    "Send the Language Page" in new WithApplication {
      val login = route(FakeRequest(GET, "/languages").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"1")).get
      status(login) must equalTo(OK)
      contentType(login) must beSome.which(_ == "text/html")
      contentAsString(login) must contain ("Languages")
    }
    "Send the Language Page without Session" in new WithApplication {
      val login = route(FakeRequest(GET, "/languages")).get
      status(login) must equalTo(SEE_OTHER)
    }

    "Send the Assignment Page" in new WithApplication {
      val login = route(FakeRequest(GET, "/assignments").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"1")).get
      status(login) must equalTo(OK)
      contentType(login) must beSome.which(_ == "text/html")
      contentAsString(login) must contain ("Assignment")
    }
    "Send the Assignment Page without Session" in new WithApplication {
      val login = route(FakeRequest(GET, "/assignments")).get
      status(login) must equalTo(SEE_OTHER)
    }

    "Send the Programming Page" in new WithApplication {
      val login = route(FakeRequest(GET, "/programming").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"1")).get
      status(login) must equalTo(OK)
      contentType(login) must beSome.which(_ == "text/html")
      contentAsString(login) must contain ("Programming")
    }
    "Send the Programming Page without Session" in new WithApplication {
      val login = route(FakeRequest(GET, "/programming")).get
      status(login) must equalTo(SEE_OTHER)
    }

    "Add the Language with Valid Data" in new WithApplication {
      val login = route(FakeRequest(POST, "/addlang").withFormUrlEncodedBody("id"->"2","userId"->"3","name"->"Some","fluency"->"Average")).get
      status(login) must equalTo(OK)
    }
    "Add the Language with InValid Data" in new WithApplication {
      val login = route(FakeRequest(POST, "/addlang").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"1").withFormUrlEncodedBody("id"->"","userId"->"3","name"->"Some","fluency"->"Average")).get
      status(login) must equalTo(OK)
    }
    "Add the Language with Valid Data and Mockito" in new WithApplication {
      when(languageService.insertLanguage(language)).thenReturn(Future{1})
      val login = call(controller.addLanguage,FakeRequest(POST, "/addlang").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"1").withFormUrlEncodedBody("id"->"1","userId"->"3","name"->"Some","fluency"->"Average"))
      status(login) must equalTo(OK)
    }

    "Add the ProgrammingLanguage with Valid Data" in new WithApplication {
      val login = route(FakeRequest(POST, "/addproglang").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"1").withFormUrlEncodedBody("id"->"2","userId"->"3","name"->"Some","fluency"->"Average")).get
      status(login) must equalTo(OK)
    }
    "Add the ProgrammingLanguage with InValid Data" in new WithApplication {
      val login = route(FakeRequest(POST, "/addproglang").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"1").withFormUrlEncodedBody("id"->"","userId"->"3","name"->"Some","fluency"->"Average")).get
      status(login) must equalTo(OK)
    }
    "Add the ProgrammingLanguage with Valid Data and Mockito" in new WithApplication {
      when(programmingService.insertProgramming(programming)).thenReturn(Future{1})
      val login = call(controller.addProgrammingLanguage,FakeRequest(POST, "/addproglang").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"3").withFormUrlEncodedBody("id"->"1","userId"->"3","name"->"Some","fluency"->"Average"))
      status(login) must equalTo(OK)
    }

    "process the Edit ProgrammingLanguage Page with Valid Data" in new WithApplication {
      val login = route(FakeRequest(POST, "/editprogramming").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"1").withFormUrlEncodedBody("id" -> "1","userId" -> "1","name"->"Some","fluency"->"Average")).get
      status(login) must equalTo(OK)
    }
    "process the Edit ProgrammingLanguage Page with Valid Data with Mock" in new WithApplication {
      when(programmingService.updateProgramming(programming)).thenReturn(Future{1})
      when(programmingService.getProgrammingByUser(3)).thenReturn(Future(List()))
      val login = call(controller.editProgramming,FakeRequest(POST, "/editprogramming").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"3").withFormUrlEncodedBody("id" -> "1","userId" -> "3","name"->"Some","fluency"->"Average"))
      status(login) must equalTo(OK)
    }
    "process the Edit ProgrammingLanguage Page with InValid Data " in new WithApplication {
      val login = route(FakeRequest(POST, "/editprogramming").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"3").withFormUrlEncodedBody("id" -> "","userId" -> "1","name"->"Some","description"->"Some","year"->"2013")).get
      status(login) must equalTo(OK)
    }

    "get the  ProgrammingLanguage  " in new WithApplication {
      val login = route(FakeRequest(GET, "/getprog/2").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"3")).get
      status(login) must equalTo(OK)
    }
    "get the  ProgrammingLanguage Page with Mockito " in new WithApplication {
      when(programmingService.getProgrammingById(1)).thenReturn(Future{Some(programming)})
      val login = call(controller.showProgrammingLanguages,FakeRequest(GET, "/getprog/1").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"3"))
      status(login) must equalTo(OK)
    }

    "get the  Language " in new WithApplication {
      val login = route(FakeRequest(GET, "/getlang/4").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"3")).get
      status(login) must equalTo(OK)
    }
    "get the  Language Page with Mockito " in new WithApplication {
      when(languageService.getLanguageById(1)).thenReturn(Future{Some(language)})
      val login = call(controller.getLanguageById(1),FakeRequest(GET, "/getlang/1").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"3"))
      status(login) must equalTo(OK)
    }

    "get the  Certificate " in new WithApplication {
      val login = route(FakeRequest(GET, "/getCert/8").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"3")).get
      status(login) must equalTo(OK)
    }
    "get the  Certificate Page with Mockito " in new WithApplication {
      when(certificateService.getById(8)).thenReturn(Future{Some(Certificates(8,3,"Some","Ssadfasd",2012))})
      val login = call(controller.getLanguageById(1),FakeRequest(GET, "/getCert/8").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"3"))
      status(login) must equalTo(OK)
    }

    "delete the  ProgrammingLanguage Page " in new WithApplication {
      val login = route(FakeRequest(POST, "/deleteprog/1").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"3")).get
      status(login) must equalTo(OK)
    }
    "delete the  ProgrammingLanguage Page with Mockito " in new WithApplication {
      when(programmingService.deleteProgramming(1)).thenReturn(Future{1})
      val login = call(controller.getProgrammingById(1),FakeRequest(POST, "/deleteprog/1").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"3"))
      status(login) must equalTo(OK)
    }

    "delete the  Language Page " in new WithApplication {
      val login = route(FakeRequest(POST, "/deletelang/1").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"3")).get
      status(login) must equalTo(OK)
    }
    "delete the  Language Page with Mockito " in new WithApplication {
      when(languageService.deleteLanguage(1)).thenReturn(Future{1})
      val login = call(controller.getLanguageById(1),FakeRequest(POST, "/deletelang/1").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"3"))
      status(login) must equalTo(OK)
    }
    "delete the  Certificate " in new WithApplication {
      val login = route(FakeRequest(POST, "/deleteCert/1").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"3")).get
      status(login) must equalTo(OK)
    }
    "delete the  Certificate with Mockito " in new WithApplication {
      when(certificateService.deleteCertificate(1)).thenReturn(Future{1})
      val login = call(controller.getLanguageById(1),FakeRequest(POST, "/deleteCert/1").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"3"))
      status(login) must equalTo(OK)
    }

    "Get all the  User " in new WithApplication {
      val login = route(FakeRequest(GET, "/getuser").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"3")).get
      status(login) must equalTo(OK)
    }
    "Get all the  User with Mockito " in new WithApplication {
      when(loginService.getAllUsers()).thenReturn(Future{List()})
      val login = call(controller.getAllUser,FakeRequest(GET, "/getuser").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"3"))
      status(login) must equalTo(OK)
    }

    "Show the Admin Panel" in new WithApplication {
      val login = route(FakeRequest(GET, "/adminpanel").withSession("email" ->"Akash@gmail.com","isAdmin"->"true","userId"->"3")).get
      status(login) must equalTo(OK)
    }
    "Show the Admin panel with wrong privillages" in new WithApplication {
      val login = route(FakeRequest(GET, "/adminpanel").withSession("email" ->"Akash@gmail.com","isAdmin"->"false","userId"->"3")).get
      status(login) must equalTo(SEE_OTHER)
    }

  }

}
