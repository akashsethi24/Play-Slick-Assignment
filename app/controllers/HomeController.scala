package controllers

import javax.inject._

import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.{JsString, Json}
import play.api.mvc._
import services._
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
class HomeController @Inject()(service: LoginServiceApi, certificateServices: CertificateServiceApi,
                               languageService: LanguageServices, assignmentService: AssignmentServices,
                               programmingService: ProgrammingServices
                              ) extends Controller {

  /**
    * Create an Action to render an HTML page with a welcome message.
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */

  def index = Action { implicit request =>
    Ok(views.html.index("Random@Some.com"))
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
              withSession("userId" -> (user.get.id.get).toString, "email" -> user.get.email, "isAdmin" -> service.isUserAdmin(user.get).toString)
          }
          else {
            println("Invalid User" + userData + "::::" + user)
            Redirect(routes.HomeController.showLogin).flashing("error" -> "Invaild")

          }
        }
      }
    )
  }

  def showCertificates = Action { implicit request =>
    Ok(views.html.certificates(Forms.addCertificates, request.session.get("isAdmin").get.toBoolean, request.session.get("userId").get))
  }

  def addCertificate = Action.async { implicit request =>

    Forms.addCertificates.bindFromRequest.fold(
      badForm => {
        val list = certificateServices.getCertificateByUser(1)
        list.map { listCert =>
          Ok(views.html.certificateTable(listCert)).as("text/html")
        }

      },
      certificateData => {
        certificateServices.insertCertificate(certificateData).flatMap { r =>
          certificateServices.getCertificateByUser(1).map { listCert =>
            Ok(views.html.certificateTable(listCert))
          }
        }
      }
    )
  }

  def editCertificate = Action.async { implicit request =>


    Forms.addCertificates.bindFromRequest.fold(
      badForm => {
        println(badForm)
        val list = certificateServices.getCertificateByUser(1)
        list.map { listCert =>
          Ok(views.html.certificateTable(listCert)).as("text/html")
        }

      },
      certificateData => {
        println(certificateData)
        certificateServices.updateCertificate(certificateData).flatMap { r =>
          certificateServices.getCertificateByUser(1).map { listCert =>
            Ok(views.html.certificateTable(listCert))
          }
        }
      }
    )
  }

  def ajaxCall = Action { implicit request =>
    Ok("Ajax Call!")
  }

  def getCertificateList = Action.async {

    val a = certificateServices.getCertificateByUser(1)
    a.map { list =>
      Ok(views.html.certificateTable(list)).as("text/html")
    }
  }

  def getLanguageList = Action.async {

    val a = languageService.getLanguageByUser(1)
    a.map { list =>
      Ok(views.html.languageTable(list)).as("text/html")
    }
  }

  def getAssignmentList = Action.async {

    val a = assignmentService.getAssignmentByUser(1)
    a.map { list =>
      Ok(views.html.assignmentTable(list)).as("text/html")
    }
  }

  def getProgrammingList = Action.async {

    val a = programmingService.getProgrammingByUser(1)
    a.map { list =>
      Ok(views.html.programmingTable(list)).as("text/html")
    }
  }

  def addProgrammingLanguage = Action.async { implicit request =>

    Forms.addProgrammingLanguages.bindFromRequest.fold(
      badForm => {
        val list = programmingService.getProgrammingByUser(1)
        list.map { listProgLangauge =>
          Ok(views.html.programmingTable(listProgLangauge)).as("text/html")
        }

      },
      programmingData => {
        programmingService.insertProgramming(programmingData).flatMap { r =>
          programmingService.getProgrammingByUser(1).map { listProgLang =>
            Ok(views.html.programmingTable(listProgLang))
          }
        }
      }
    )
  }

  def editProgramming = Action.async { implicit request =>


    Forms.addProgrammingLanguages.bindFromRequest.fold(
      badForm => {
        println(badForm)
        val list = programmingService.getProgrammingByUser(1)
        list.map { listProgram =>
          Ok(views.html.programmingTable(listProgram)).as("text/html")
        }

      },
      programmingData => {
        println(programmingData)
        programmingService.updateProgramming(programmingData).flatMap { r =>
          programmingService.getProgrammingByUser(1).map { listProgram =>
            Ok(views.html.programmingTable(listProgram))
          }
        }
      }
    )
  }

  def getProgrammingById(id: Int) = Action.async {
    val programming = programmingService.getProgrammingById(id)
    programming.map { program =>

      val jsonObj = Json.obj(
        "id" -> program.get.id.toString,
        "userId" -> program.get.userId.toString,
        "name" -> program.get.name,
        "skill" -> program.get.skillLevel
      )
      Ok(jsonObj)
    }
  }


  def showLanguages = Action { implicit request =>
    Ok(views.html.language())
  }

  def showAssignments = Action { implicit request =>
    assignmentService.createAssignmentTable()
    Ok(views.html.assignments())
  }

  def showProgrammingLanguages = Action { implicit request =>
    programmingService.createProgrammingTable()
    Ok(views.html.programingLangauges(Forms.addProgrammingLanguages, request.session.get("isAdmin").get.toBoolean, request.session.get("userId").get))
  }

  def deleteProgrammingLanguages(id: Int) = Action.async {

    programmingService.deleteProgramming(id).flatMap { r =>
      programmingService.getProgrammingByUser(1).map { listProg =>
        Ok(views.html.programmingTable(listProg))
      }
    }
  }

  def showAdminPanel = Action { implicit request =>
    Ok(views.html.adminPanel())
  }

  def deleteCertificate(id: Int) = Action.async {

    certificateServices.deleteCertificate(id).flatMap { r =>
      certificateServices.getCertificateByUser(1).map { listCert =>
        Ok(views.html.certificateTable(listCert))
      }
    }
  }

  def getCertificateById(id: Int) = Action.async {

    val certificate = certificateServices.getById(id)
    certificate.map { cert =>

      val jsonObj = Json.obj(
        "id" -> cert.get.id.toString,
        "userId" -> cert.get.userId.toString,
        "name" -> cert.get.name,
        "year" -> cert.get.year.toString,
        "desc" -> cert.get.description
      )
      Ok(jsonObj)
    }

  }
}
