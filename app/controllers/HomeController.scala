package controllers

import javax.inject._
import play.api.i18n.Messages.Implicits._
import play.api.Play.current
import play.api.mvc._
import services.Forms

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject() extends Controller {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    Ok(views.html.index())
  }

  def showLogin = Action{
    Ok(views.html.login(Forms.loginForm))
  }

}
