package controllers

import javax.inject._
import play.api.Play._
import play.api.libs.json.{JsValue, Json, JsError}
import play.api.mvc._
import javax.inject.Inject
import play.modules.reactivemongo.{ MongoController, ReactiveMongoApi, ReactiveMongoComponents }


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
  def index(any: String) = Action {
    Ok(views.html.index())
  }



  /** load an HTML page from public/html */
  def loadPublicHTML(any: String) = Action {
    val file = current.getFile("/public/partials/" + any)
    if (file.exists())
      Ok(scala.io.Source.fromFile(file.getCanonicalPath).mkString).as("text/html");
    else
      NotFound
  }

  def search(params: String)= Action{

    Ok("Here you go!")
  }

  def map=Action{

    Ok("here")
  }

}
