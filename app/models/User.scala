package models

import java.lang.System._
import java.util.{Calendar, Date}

import org.mindrot.jbcrypt.BCrypt
import play.api.Play._
import play.api.libs.json.{JsObject, Json}
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.bson.BSONDocument
import reactivemongo.core.commands.{Match, Project, Aggregate}
import scala.concurrent.ExecutionContext.Implicits.global
import play.modules.reactivemongo.json._

import scala.concurrent.Future

/**
  * Created by Asmau Muktar on 3/15/2017.
  */


case class User (
                  _id: Option[Long],
                  email: String,
                  firstName: Option[String],
                  lastName: Option[String],
                  password: String,
                  accountType: Option[String],
                  dateCreated: Option[Date]

                )

object User{

  implicit val json4userAccount = Json.format[User]

}

object UserDAO {
  lazy val reactiveMongoApi = current.injector.instanceOf[ReactiveMongoApi]

  def users = reactiveMongoApi.db.collection[JSONCollection]("users")
  val now = Calendar.getInstance().getTime()



  def aggregateUserQuery() = {

    import helpers.Implicits._

    val agg = Aggregate(users.name,
      Seq(Project("_id" -> 1,
        "email" -> 1,
        "firstName" -> 1,
        "lastName" -> 1,
        "accountType" -> 1
      )))

    users.db.command(agg)
  }

  def findOne(query: JsObject): Future[Option[User]] = {
    users.find(query).one[User]
  }

  def authenticate(userLogin: User): Future[Option[User]] = {

    val query = Json.obj("email" -> userLogin.email)

    users.find(query).one[User].map {

      case Some(dbUser: User) =>
        if (BCrypt.checkpw(userLogin.password, dbUser.password)) {
        //  @TODO add loginTime and update userAccount
        //print(dbUser)
        Some(dbUser)
      }
      else None

      case None => None
    }
  }

  def getUserAccount(user: User): Future[Option[User]] =
    users
      .find(Json.obj("email" -> user.email))
      .one[User]




  //Add new user

  def add(newUser: User) = {

    def getCurrentTimeMillis() = currentTimeMillis()
    val cryptedPass = BCrypt.hashpw(newUser.password, BCrypt.gensalt())
    val dbUser = newUser.copy(_id = Some(getCurrentTimeMillis()),
      password = cryptedPass,accountType = Some("User"), dateCreated = Some(now)
    )
    users.insert(dbUser)
  }



  def findAccount(query: BSONDocument)={

    import helpers.Implicits._

    val agg = Aggregate(users.name,
      Seq(Match(query),
        Project("_id" -> 1,
          "email" -> 1,
          "firstName" -> 1,
          "lastName" -> 1,
          "accountType" -> 1
        )))

    users.db.command(agg)
  }

}