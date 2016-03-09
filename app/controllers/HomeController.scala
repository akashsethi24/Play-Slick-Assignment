package controllers

import javax.inject._
import play.api.mvc._
import services.{LoginServiceApi, Forms}
import play.api.i18n.Messages.Implicits._
import play.api.Play.current
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(service:LoginServiceApi) extends Controller {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */

  def index = Action {
    Ok(views.html.index())
  }

  def showLogin = Action{implicit request =>

    service.createUserTable()
    Ok(views.html.login(Forms.loginForm))
  }

  def showCertificates = Action{
    Ok(views.html.certificates(Forms.addCertificates))
  }

  def showLanguages = Action{
    Ok(views.html.language())
  }

  def showAssignments = Action{
    Ok(views.html.assignments())
  }

  def showProgrammingLanguages = Action{
    Ok(views.html.programingLangauges())
  }

  def showAdminPanel = Action{
    Ok(views.html.adminPanel())
  }

}

