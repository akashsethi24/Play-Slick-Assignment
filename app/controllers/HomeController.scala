package controllers

import javax.inject._

import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.{JsString, Json}
import play.api.mvc._
import services.{CertificateServiceApi, Forms, LoginServiceApi}
import play.api.libs.concurrent.Execution.Implicits._
import play.api.i18n.Messages.Implicits._
import play.api.Play.current
import scala.concurrent.Future
import play.api.libs.json._

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(service: LoginServiceApi,certificateServices:CertificateServiceApi) extends Controller {

  /**
    * Create an Action to render an HTML page with a welcome message.
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */

  def index = Action { implicit request =>
    Ok(views.html.index(request.session.get("email").get))
  }

  def showLogin = Action { implicit request =>
    Ok(views.html.login(Forms.loginForm))
  }


  def processLoginForm = Action.async { implicit request =>
    Forms.loginForm.bindFromRequest.fold(
      badForm => {
        println(badForm)
        Future {
          BadRequest(views.html.login(badForm))
        }
      },
      userData => {
        val userInfo = service.getUserByEmail(userData._1, userData._2)
        userInfo.map { user =>
          if (user.isDefined) {
            Redirect(routes.HomeController.index).
              withSession("userId" -> (user.get.id.get).toString ,"email" -> user.get.email, "isAdmin" -> service.isUserAdmin(user.get).toString)
          }
          else {
            println("Invalid User"+userData+"::::"+user)
            Redirect(routes.HomeController.showLogin).flashing("error" -> "Invaild")

          }
        }
      }
    )
  }

  def showCertificates = Action { implicit request =>
    certificateServices.createCertificateTable
    val userId = request.session.get("email").get
    println(userId)
    Ok(views.html.certificates(Forms.addCertificates,request.session.get("isAdmin").get.toBoolean, request.session.get("userId").get))
  }

  def addCertificate = Action.async{implicit request =>

  Forms.addCertificates.bindFromRequest.fold(
    badForm => {
      println(badForm)
      println(request.session.get("userId").get)
      Future {
        BadRequest(views.html.certificates(badForm, request.session.get("isAdmin").get.toBoolean,request.session.get("userId").get))
      }
    },
    certificateData =>{
      val result = certificateServices.insertCertificate(certificateData)
      result.map{ returnedID =>
        if(returnedID > 0){
          println(returnedID+"::"+certificateData)
          Ok(views.html.certificates(Forms.addCertificates ,request.session.get("isAdmin").get.toBoolean,request.session.get("userId").get))
        }
        else{
          println("Failed : "+returnedID)
          Redirect(routes.HomeController.showCertificates).flashing("error" -> "Unable to add Certificate")
        }
      }
    }
  )
  }

  def getCertificateList = Action.async{

    val a = certificateServices.getCertificateByUser(1)
    a.map{list =>
    Ok(views.html.certificateTable(list)).as("text/html")
    }
  }

  def getLanguageList = Action.async{

    val a = certificateServices.getCertificateByUser(1)
    a.map{list =>
      Ok(views.html.certificateTable(list)).as("text/html")
    }
  }

  def getAssignmnentList = Action.async{

    val a = certificateServices.getCertificateByUser(1)
    a.map{list =>
      Ok(views.html.certificateTable(list)).as("text/html")
    }
  }

  def showLanguages = Action {
    Ok(views.html.language())
  }

  def showAssignments = Action {
    Ok(views.html.assignments())
  }

  def showProgrammingLanguages = Action {
    Ok(views.html.programingLangauges())
  }

  def showAdminPanel = Action {
    Ok(views.html.adminPanel())
  }


}
