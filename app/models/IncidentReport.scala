package models

import java.sql.{Timestamp, Time}
import java.util.{Date, Calendar}

import play.modules.reactivemongo.json._
import play.api.Play._
import play.api.libs.json.{JsObject, Json}
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.core.commands.{Match, Project, Aggregate}
import reactivemongo.bson.{BSONDocument, BSONObjectID}

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Asmau Muktar on 3/1/2017.
  */

case class IncidentReport(
                         _id:Option[Int],
                         typeOfReport:Option[String],
                         dateOfIncident:Option[Date],
                         timeOfIncident:Option[String],
                         locationofIncident:Option[String],
                         addressofIncident:Option[String],
                         nameOfSuspect:Option[String],
                         genderOfSuspect:Option[String],
                         typeOfIncident:Option[String],
                         noofPeopleInvolved:Option[String],
                         ageofVictim:Option[String],
                         genderofVictim:Option[String],
                         suspicionText:Option[String],
                         dateReported:Option[Date],
                         reportedBy:Option[String]
                         )

object IncidentReport{

  implicit val incidentreport4Json=Json.format[IncidentReport]
}

object IncidentReportDAO {

  import IncidentReport._

  lazy val reactiveMongoApi = current.injector.instanceOf[ReactiveMongoApi]
  val now = Calendar.getInstance().getTime()


  def incidentreport = reactiveMongoApi.db.collection[JSONCollection]("incidentReports")


  def findOne(query: JsObject ) = {
    incidentreport.find(query).one[IncidentReport]
  }

  def find(query: BSONDocument ) = {

    import helpers.Implicits._

    val agg = Aggregate(incidentreport.name,
      Seq(Match(query),
        Project(
          "_id"->1,
          "typeOfReport"->1,
          "dateOfIncident"->1,
          "timeOfIncident"->1,
          "locationofIncident"->1,
          "addressofIncident"->1,
          "nameOfSuspect"->1,
          "genderOfSuspect"->1,
          "typeOfIncident"->1,
          "noofPeopleInvolved"->1,
          "ageofVictim"->1,
          "genderofVictim"->1,
          "suspicionText"->1,
          "dateReported"->1,
          "reportedBy"->1
        )))
    incidentreport.db.command(agg)
  }

  def add(newincidentReport:IncidentReport)={
    val date= now
    val r = scala.util.Random
    val id= r.nextInt()

    val dbnewReport = newincidentReport.copy(_id= Some(id),typeOfReport= Some("Incident Report"), dateReported = Some(date), reportedBy= Some("Admin"))
    incidentreport.insert(dbnewReport)
  }

  def getAllreports={

    import helpers.Implicits._

    val agg = Aggregate(incidentreport.name,
      Seq(Project("_id" -> 1,
        "typeOfReport" ->1,
        "dateOfIncident" -> 1,
        "timeOfIncident" -> 1,
        "locationofIncident"->1,
        "typeOfIncident" -> 1,
        "genderofvictim" -> 1,
        "suspicionText" -> 1,
        "dateReported"->1
      )))

    incidentreport.db.command(agg)
  }

  def correlationData={

    import helpers.Implicits._

    val agg = Aggregate(incidentreport.name,
      Seq(Project("_id" -> 1,
        "locationofIncident" -> 1,
        "addressofIncident" -> 1,
        "nameOfSuspect" -> 1,
        "genderOfSuspect" -> 1,
        "suspicionText" -> 1,
        "ageofVictim" -> 1,
        "genderofVictim" -> 1
      )))

    incidentreport.db.command(agg)

  }
}
