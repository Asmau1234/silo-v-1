package models

/**
  * Created by Asmau Muktar on 3/1/2017.
  */

import java.util.Calendar

import play.api.Play._
import play.api.libs.json.{JsObject, Json}
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import reactivemongo.core.commands.{Match, Project, Aggregate}

import scala.concurrent.ExecutionContext.Implicits.global
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json._
import java.util.Date


import scala.util.{Success, Failure}


case class SuspectProfile(
                          nameOfSuspect:Option[String],
                          genderOfSuspect:Option[String],
                          contactAddress:Option[String],
                          contactPhone:Option[String],
                          height:Option[String],
                          weight:Option[String],
                          skinColor:Option[String],
                          race:Option[String],
                          nationality:Option[String],
                          religion:Option[String],
                          tribe:Option[String],
                          tribalMark:Option[String],
                          glasses:Option[String],
                          facialHair:Option[String],
                          headHair:Option[String],
                          hairType:Option[String],
                          hairColor:Option[String],
                          eyeColor:Option[String],
                          tattoo:Option[String],
                          tattooImage:Option[String]
                         )

case class PersonReport( _id: Option[Int],
                         typeOfReport:Option[String],
                         suspectProfile: Option[SuspectProfile],
                         lastplaceSeen:Option[String],
                         ageOfvictim:Option[String],
                         genderOfvictim:Option[String],
                         suspicionText:Option[String],
                         dateReported:Option[Date],
                         reportedBy:Option[String]
)

object PersonReport{

  implicit val suspectprofile4json= Json.format[SuspectProfile]
  implicit val personreport4json=Json.format[PersonReport]

}



object PersonReportDAO {

  import PersonReport._
  import reactivemongo.bson.BSONObjectID

  lazy val reactiveMongoApi = current.injector.instanceOf[ReactiveMongoApi]
  val now = Calendar.getInstance().getTime()


  def personreport = reactiveMongoApi.db.collection[JSONCollection]("personReports")


  def findOne(query: JsObject ) = {
    personreport.find(query).one[PersonReport]
  }

  def find(query: BSONDocument ) = {

    import helpers.Implicits._

    val agg = Aggregate(personreport.name,
      Seq(Match(query),
        Project("_id" -> 1,
          "typeOfReport" ->1,
          "suspectProfile" ->1,
          "lastplaceSeen" ->1,
          "ageOfvictim" ->1,
          "genderOfvictim" ->1,
          "suspicionText" ->1,
          "dateReported" ->1,
          "reportedBy" ->1
        )))
    personreport.db.command(agg)
  }

  def add(newpersonReport:PersonReport)={

    val date= now
    val r = scala.util.Random
    val id= r.nextInt()
    //val id = BSONObjectID.generate()


    val dbnewReport = newpersonReport.copy(_id= Some(id),typeOfReport= Some("Person Report"), dateReported = Some(date), reportedBy= Some("Admin"))
    personreport.insert(dbnewReport)

  }

  def getAllreports={

    import helpers.Implicits._

    val agg = Aggregate(personreport.name,
      Seq(Project("_id" -> 1,
        "typeOfReport" ->1,
        "suspectProfile.nameOfSuspect" -> 1,
        "suspectProfile.genderOfSuspect" -> 1,
        "suspectProfile.contactAddress" -> 1,
        "suspectProfile.height" -> 1,
        "suspectProfile.weight" -> 1,
        "dateReported"->1
      )))

    personreport.db.command(agg)
  }

  def correlationData={

    import helpers.Implicits._

    val agg = Aggregate(personreport.name,
      Seq(Project("_id" -> 1,
        "suspectProfile.nameOfSuspect" -> 1,
        "suspectProfile.genderOfSuspect" -> 1,
        "suspectProfile.contactAddress" -> 1,
        "lastplaceSeen" -> 1,
        "suspicionText" -> 1,
        "ageOfVictim" -> 1,
        "genderOfVictim" -> 1
      )))

    personreport.db.command(agg)

  }

}