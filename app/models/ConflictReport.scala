package models

import java.util.{Calendar, Date}

import play.api.Play._
import play.api.libs.json.{JsObject, Json}
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json.collection.JSONCollection
import play.modules.reactivemongo.json._
import reactivemongo.bson.BSONDocument
import reactivemongo.core.commands.{Match, Project, Aggregate}
import scala.concurrent.ExecutionContext.Implicits.global
/**
  * Created by Asmau Muktar on 3/6/2017.
  */
case class ConflictReport (
                      _id:Option[Int],
                      currentLocation:Option[String],
                      stateOfCrisis:Option[String],
                      lgaOfCrisis:Option[String],
                      longitude: Option[Double],
                      latitude:Option[Double],
                      levelOfCrisis:Option[String],
                      dateReported: Option[Date],
                      reportedBy: Option[String]
                     )

object ConflictReport{

  implicit val map4json=Json.format[ConflictReport]
}


object ConflictReportDAO{

  import ConflictReport._

  lazy val reactiveMongoApi = current.injector.instanceOf[ReactiveMongoApi]
  def conflictreport = reactiveMongoApi.db.collection[JSONCollection]("conflictReports")

  val now = Calendar.getInstance().getTime()


  def findOne(query: JsObject ) = {
    conflictreport.find(query).one[IncidentReport]
  }

  def add(conflictrep:ConflictReport)={
    val date= now


    val dbnewReport = conflictrep.copy(dateReported = Some(date), reportedBy=Some("Admin"))
    conflictreport.insert(dbnewReport)
  }

  def getLatLang(query:BSONDocument)={

    import helpers.Implicits._

    val agg = Aggregate(conflictreport.name,
      Seq(Match(query),
        Project("_id" -> 1,
        "latitude" ->1,
        "longitude" -> 1,
          "levelOfCrisis"->1
      )))

    conflictreport.db.command(agg)
  }

}


