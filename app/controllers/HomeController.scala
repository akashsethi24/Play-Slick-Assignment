package controllers

import javax.inject._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc._
import services._
import play.api.i18n.Messages.Implicits._
import play.api.Play.current
import scala.concurrent.Future

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(service: LoginServiceApi,
                               certificateServices: CertificateServiceApi,
                               languageService: LanguageServiceApi,
                               assignmentService: AssignmentServiceApi,
                               programmingService: ProgrammingServiceApi)
    extends Controller {

  /**
    * Create an Action to render an HTML page with a welcome message.
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */

  def index =
    Action { implicit request =>
      Ok(views.html.index("Random@Some.com"))
    }

  def showLogin =
    Action { implicit request =>
      Ok(views.html.login(Forms.loginForm))
    }

  def processLoginForm =
    Action.async { implicit request =>
      Forms.loginForm.bindFromRequest.fold(
          badForm =>
            {
              println(badForm)
              Future {
                BadRequest(views.html.login(badForm))
              }
            },
          userData =>
            {
              val userInfo = service.getUserByEmail(userData._1, userData._2)
              userInfo.map { user =>
                if (user.isDefined) {
                  Redirect(routes.HomeController.index).withSession(
                      "userId" -> (user.get.id.get).toString,
                      "email" -> user.get.email,
                      "isAdmin" -> service.isUserAdmin(user.get).toString)
                } else {
                  println("Invalid User" + userData + "::::" + user)
                  Redirect(routes.HomeController.showLogin)
                    .flashing("error" -> "Invaild")
                }
              }
            }
      )
    }

  def showCertificates =
    Action { implicit request =>
      certificateServices.createCertificateTable()
      Ok(views.html.certificates(Forms.addCertificates,
                                 request.session.get("isAdmin").get.toBoolean,
                                 request.session.get("userId").get))
    }

  def addCertificate =
    Action.async { implicit request =>
      Forms.addCertificates.bindFromRequest.fold(
          badForm =>
            {
              val list = certificateServices.getCertificateByUser(request.session.get("userId").get.toInt)
              list.map { listCert =>
                Ok(views.html.certificateTable(listCert)).as("text/html")
              }
            },
          certificateData =>
            {
              certificateServices.insertCertificate(certificateData).flatMap {
                number =>
                  certificateServices.getCertificateByUser(request.session.get("userId").get.toInt).map { listCert =>
                    Ok(views.html.certificateTable(listCert))
                  }
              }
            }
      )
    }

  def editCertificate =
    Action.async { implicit request =>
      Forms.addCertificates.bindFromRequest.fold(
          badForm =>
            {
              println(badForm)
              val list = certificateServices.getCertificateByUser(request.session.get("userId").get.toInt)
              list.map { listCert =>
                Ok(views.html.certificateTable(listCert)).as("text/html")
              }
            },
          certificateData =>
            {
              println(certificateData)
              certificateServices.updateCertificate(certificateData).flatMap {
                r =>
                  certificateServices.getCertificateByUser(request.session.get("userId").get.toInt).map { listCert =>
                    Ok(views.html.certificateTable(listCert))
                  }
              }
            }
      )
    }

  def getCertificateList =
    Action.async { implicit request =>
      val a = certificateServices.getCertificateByUser(request.session.get("userId").get.toInt)
      a.map { list =>
        Ok(views.html.certificateTable(list)).as("text/html")
      }
    }

  def getLanguageList =
    Action.async { implicit request =>
      val a = languageService.getLanguageByUser(request.session.get("userId").get.toInt)
      a.map { list =>
        Ok(views.html.languageTable(list)).as("text/html")
      }
    }

  def getAssignmentList =
    Action.async { implicit request =>
      val a = assignmentService.getAssignmentByUser(request.session.get("userId").get.toInt)
      a.map { list =>
        Ok(views.html.assignmentTable(list)).as("text/html")
      }
    }

  def getProgrammingList =
    Action.async { implicit request =>
      val a = programmingService.getProgrammingByUser(request.session.get("userId").get.toInt)
      a.map { list =>
        Ok(views.html.programmingTable(list)).as("text/html")
      }
    }

  def showLanguages =
    Action { implicit request =>
      languageService.createLanguageTable()
      Ok(views.html.language())
    }

  def showAssignments =
    Action { implicit request =>
      assignmentService.createAssignmentTable()
      Ok(views.html.assignments())
    }

  def showProgrammingLanguages =
    Action { implicit request =>
      programmingService.createProgrammingTable()
      Ok(views.html.programingLangauges())
    }

  def showAdminPanel =
    Action { implicit request =>
      Ok(views.html.adminPanel())
    }

  def deleteCertificate(id: Int) =
    Action.async { implicit request =>
      certificateServices.deleteCertificate(id).flatMap { r =>
        certificateServices.getCertificateByUser(request.session.get("userId").get.toInt).map { listCert =>
          Ok(views.html.certificateTable(listCert))
        }
      }
    }

  def getCertificateById(id: Int) =
    Action.async {
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
