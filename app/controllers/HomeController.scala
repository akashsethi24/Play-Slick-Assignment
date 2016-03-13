package controllers

import javax.inject._

import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc._
import services._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.i18n.Messages.Implicits._
import play.api.Play.current
import scala.concurrent.Future
import services._

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(service: LoginServiceApi, certificateServices: CertificateServiceApi,
                               languageService: LanguageServiceApi, assignmentService: AssignmentServiceApi,
                               programmingService: ProgrammingServiceApi
                              ) extends Controller {

  /**
    * Create an Action to render an HTML page with a welcome message.
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */

  def index = Action { implicit request =>
    if(request.session.get("email").isDefined){
      Ok(views.html.index(request.session.get("email").get,request.session.get("isAdmin").get.toBoolean))
    }
    else {
      Redirect(routes.HomeController.showLogin)
    }
  }

  def showLogin = Action { implicit request =>
    if(request.session.get("email").isDefined){
      Redirect(routes.HomeController.index)
    }
    else {
      Ok(views.html.login(Forms.loginForm))
    }
  }

  def logout = Action{ implicit  request =>
    if(request.session.get("email").isDefined){
      Redirect(routes.HomeController.showLogin).withNewSession
    }
    else{
      Redirect(routes.HomeController.showLogin)
    }
  }

  def processLoginForm = Action.async { implicit request =>
    Forms.loginForm.bindFromRequest.fold(
      badForm => {
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
            Redirect(routes.HomeController.showLogin).flashing("error" -> "Invaild")

          }
        }
      }
    )
  }

  def showCertificates = Action { implicit request =>
    if(request.session.get("email").isDefined) {
      Ok(views.html.certificates(Forms.addCertificates, request.session.get("isAdmin").get.toBoolean, request.session.get("userId").get))
    }
    else{
      Redirect(routes.HomeController.showLogin)
    }
  }

  def addLanguage = Action.async { implicit request =>

    Forms.addLanguages.bindFromRequest.fold(
      badForm => {
        val list = languageService.getLanguageByUser(request.session.get("userId").get.toInt)
        list.map { listLang =>
          Ok(views.html.languageTable(listLang)).as("text/html")
        }

      },
      languageData => {
        languageService.insertLanguage(languageData).flatMap { r =>
          languageService.getLanguageByUser(1).map { listLang =>
            Ok(views.html.languageTable(listLang))
          }
        }
      }
    )
  }

  def addCertificate = Action.async { implicit request =>

    Forms.addCertificates.bindFromRequest.fold(
      badForm => {
        val list = certificateServices.getCertificateByUser(request.session.get("userId").get.toInt)
        list.map { listCert =>
          Ok(views.html.certificateTable(listCert)).as("text/html")
        }

      },
      certificateData => {
        certificateServices.insertCertificate(certificateData).flatMap { r =>
          certificateServices.getCertificateByUser(request.session.get("userId").get.toInt).map { listCert =>
            Ok(views.html.certificateTable(listCert))
          }
        }
      }
    )
  }

  def editCertificate = Action.async { implicit request =>


    Forms.addCertificates.bindFromRequest.fold(
      badForm => {
        val list = certificateServices.getCertificateByUser(request.session.get("userId").get.toInt)
        list.map { listCert =>
          Ok(views.html.certificateTable(listCert)).as("text/html")
        }

      },
      certificateData => {
        certificateServices.updateCertificate(certificateData).flatMap { r =>
          certificateServices.getCertificateByUser(request.session.get("userId").get.toInt).map { listCert =>
            Ok(views.html.certificateTable(listCert))
          }
        }
      }
    )
  }

  def editLanguage = Action.async { implicit request =>


    Forms.addLanguages.bindFromRequest.fold(
      badForm => {
        val list = languageService.getLanguageByUser(request.session.get("userId").get.toInt)
        list.map { listLang =>
          Ok(views.html.languageTable(listLang)).as("text/html")
        }

      },
      languageData => {
        languageService.updateLanguage(languageData).flatMap { r =>
          languageService.getLanguageByUser(request.session.get("userId").get.toInt).map { listLang =>
            Ok(views.html.languageTable(listLang))
          }
        }
      }
    )
  }

  def getCertificateList = Action.async { implicit request =>

    val a = certificateServices.getCertificateByUser(request.session.get("userId").get.toInt)
    a.map { list =>
      Ok(views.html.certificateTable(list)).as("text/html")
    }
  }

  def getCertificateListByUser(id:Int) = Action.async { implicit request =>

    val a = certificateServices.getCertificateByUser(id)
    a.map { list =>
      Ok(views.html.certificateTableAdmin(list)).as("text/html")
    }
  }

  def getLanguageList = Action.async { implicit request =>

    val a = languageService.getLanguageByUser(request.session.get("userId").get.toInt)
    a.map { list =>
      Ok(views.html.languageTable(list)).as("text/html")
    }
  }

  def getLanguageListByUser(id:Int) = Action.async { implicit request =>

    val a = languageService.getLanguageByUser(id)
    a.map { list =>
      Ok(views.html.languageTableAdmin(list)).as("text/html")
    }
  }

  def getAssignmentList = Action.async { implicit request =>

    val a = assignmentService.getAssignmentByUser(request.session.get("userId").get.toInt)
    a.map { list =>
      Ok(views.html.assignmentTable(list)).as("text/html")
    }
  }

  def getAssignmentListByUser(id:Int) = Action.async { implicit request =>

    val a = assignmentService.getAssignmentByUser(id)
    a.map { list =>
      Ok(views.html.assignmentTableAdmin(list)).as("text/html")
    }
  }

  def getProgrammingList = Action.async { implicit request =>

    val a = programmingService.getProgrammingByUser(request.session.get("userId").get.toInt)
    a.map { list =>
      Ok(views.html.programmingTable(list)).as("text/html")
    }
  }

  def getProgrammingListByUser(id:Int) = Action.async { implicit request =>

    val a = programmingService.getProgrammingByUser(id)
    a.map { list =>
      Ok(views.html.programmingTableAdmin(list)).as("text/html")
    }
  }

  def addProgrammingLanguage = Action.async { implicit request =>

    Forms.addProgrammingLanguages.bindFromRequest.fold(
      badForm => {
        val list = programmingService.getProgrammingByUser(request.session.get("userId").get.toInt)
        list.map { listProgLangauge =>
          Ok(views.html.programmingTable(listProgLangauge)).as("text/html")
        }

      },
      programmingData => {
        programmingService.insertProgramming(programmingData).flatMap { r =>
          programmingService.getProgrammingByUser(request.session.get("userId").get.toInt).map { listProgLang =>
            Ok(views.html.programmingTable(listProgLang))
          }
        }
      }
    )
  }

  def editProgramming = Action.async { implicit request =>


    Forms.addProgrammingLanguages.bindFromRequest.fold(
      badForm => {
        val list = programmingService.getProgrammingByUser(request.session.get("userId").get.toInt)
        list.map { listProgram =>
          Ok(views.html.programmingTable(listProgram)).as("text/html")
        }

      },
      programmingData => {
        programmingService.updateProgramming(programmingData).flatMap { r =>
          programmingService.getProgrammingByUser(request.session.get("userId").get.toInt).map { listProgram =>
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

  def getLanguageById(id: Int) = Action.async {
    val language = languageService.getLanguageById(id)
    language.map { program =>
      val jsonObj = Json.obj(
        "id" -> program.get.id.toString,
        "userId" -> program.get.userId.toString,
        "name" -> program.get.name,
        "fluency" -> program.get.fluency
      )
      Ok(jsonObj)
    }
  }



  def showLanguages = Action { implicit request =>
    if(request.session.get("email").isDefined) {
      Ok(views.html.language(Forms.addLanguages, request.session.get("isAdmin").get.toBoolean, request.session.get("userId").get))
    }
    else{
      Redirect(routes.HomeController.showLogin)
    }
  }

  def showAssignments = Action { implicit request =>
    if(request.session.get("email").isDefined) {
      Ok(views.html.assignments(request.session.get("isAdmin").get.toBoolean, request.session.get("userId").get))
    }
    else{
      Redirect(routes.HomeController.showLogin)
    }
  }

  def showProgrammingLanguages = Action { implicit request =>
    if(request.session.get("email").isDefined) {
      Ok(views.html.programingLangauges(Forms.addProgrammingLanguages, request.session.get("isAdmin").get.toBoolean, request.session.get("userId").get))
    }
    else{
      Redirect(routes.HomeController.showLogin)
    }
  }

  def deleteProgrammingLanguages(id: Int) = Action.async { implicit request =>

    programmingService.deleteProgramming(id).flatMap { r =>
      programmingService.getProgrammingByUser(request.session.get("userId").get.toInt).map { listProg =>
        Ok(views.html.programmingTable(listProg))
      }
    }
  }

  def deleteLanguages(id: Int) = Action.async { implicit request =>
    languageService.deleteLanguage(id).flatMap { r =>
      languageService.getLanguageByUser(request.session.get("userId").get.toInt).map { listLang =>
        Ok(views.html.languageTable(listLang))
      }
    }
  }

  def showAdminPanel = Action { implicit request =>
    if(request.session.get("isAdmin").isDefined && request.session.get("isAdmin").get.toBoolean) {
      Ok(views.html.adminPanel(request.session.get("isAdmin").get.toBoolean))
    }
    else{
      Redirect(routes.HomeController.index)
    }
  }

  def deleteCertificate(id: Int) = Action.async { implicit request =>

    certificateServices.deleteCertificate(id).flatMap { r =>
      certificateServices.getCertificateByUser(request.session.get("userId").get.toInt).map { listCert =>
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

  def getAllUser = Action.async{

    val listofUser = service.getAllUsers()
    listofUser.map{list =>
      Ok(views.html.userTable(list))
    }
  }
}
