package controllers

import javax.inject._
import play.api.mvc._
import services.{LoginServiceApi, Forms}
import play.api.i18n.Messages.Implicits._
import play.api.Play.current

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
    val a = service.getUserByEmail("Akash@gmail.com")
    Thread.sleep(1000)
    println(a.value)
    Ok(views.html.login(Forms.loginForm))
  }

  def showCertificates = Action{
    Ok(views.html.certificates())
  }

}

