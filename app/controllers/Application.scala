package controllers

import pdi.jwt.JwtSession
import play.api.mvc._
import javax.inject.Inject
import play.modules.reactivemongo.{ MongoController, ReactiveMongoApi, ReactiveMongoComponents }
import play.modules.reactivemongo.json._
import play.api.libs.json._
import play.api.libs.json.Json
import scala.concurrent.ExecutionContext.Implicits.global
import models._
import models.User._
import scala.concurrent.Future


/**
  * Created by Asmau Muktar on 2/22/2017.
  */

class Application @Inject()(val reactiveMongoApi: ReactiveMongoApi) extends Controller
  with MongoController
  with ReactiveMongoComponents
  with Secured {


  def index(any: String) = Action {
    Ok(views.html.index())
  }

  def login=Action.async(parse.json) {implicit request =>
    
    val form = request.body.validate[User]

    form.fold(
      formWithErrors =>  Future.successful(BadRequest(JsError.toJson(formWithErrors))),
      form => {
        import models.User._

        //get the user auth information
        val user: Future[Option[User]] = UserDAO.authenticate(form)
        user.flatMap {
          //get the officer profile
          case Some(a: User) =>
            println(a)
            UserDAO.findOne(Json.obj("email" -> a.email))


           val session = JwtSession() +("user", a)

            Future.successful(Ok(Json.obj("token" -> session.serialize,
              "userType" -> Json.obj("accountType" -> a.accountType),
              "profile" -> (a.lastName + " " + a.firstName)
            )
            ))

          case None => Future.successful( Unauthorized("Invalid Log in Credentials!"))

        }
      }
    )
  }

  def signup=Action.async(parse.json){ implicit request =>

    val user= (request.body\ "user").as[User]

    UserDAO.add(user).map { x =>
      Ok("User Added")
    }

  }
}