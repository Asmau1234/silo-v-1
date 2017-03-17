package models

import java.awt.Color
import java.util.{Date, Calendar}

import com.google.inject.Inject
import play.api.Play._
import play.api.libs.json.{JsObject, Json}
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import reactivemongo.core.commands.{Match, Project, Aggregate}

import scala.concurrent.ExecutionContext.Implicits.global
import play.modules.reactivemongo.json._
/**
  * Created by Asmau Muktar on 3/1/2017.
  */




case class VehicleReport(_id:Option[Int],
                         typeOfReport: Option[String],
                         vehicleType: Option[String],
                         vehicleMake:Option[String],
                         vehicleColor:Option[String],
                         doorNumber:Option[String],
                         typeofSeats:Option[String],
                         colorofSeats:Option[String],
                         plateNumber:Option[String],
                         lastlocationSeen:Option[String],
                         emptyVehicle:Option[String],
                         noofPeopleinvehicle:Option[String],
                         nameofOwner:Option[String],
                         genderofOwner:Option[String],
                         addressofOwner:Option[String],
                         ageofVictim:Option[String],
                         genderofVictim:Option[String],
                         suspicionText:Option[String],
                         specialFeature:Option[String],
                         dateReported:Option[Date],
                         reportedBy:Option[String]

)

object VehicleReport{

  implicit val vehiclereport4json=Json.format[VehicleReport]
}

object VehicleReportDAO {

  import VehicleReport._
  import reactivemongo.bson.BSONObjectID

  lazy val reactiveMongoApi = current.injector.instanceOf[ReactiveMongoApi]
  val now = Calendar.getInstance().getTime()


  def vehiclereport = reactiveMongoApi.db.collection[JSONCollection]("vehicleReports")


  def findOne(query: JsObject ) = {
    vehiclereport.find(query).one[VehicleReport]
  }

  def add(newvehicleReport:VehicleReport)={

    val date= now
    val r = scala.util.Random
    val id= r.nextInt()/*
    val getcolor = newvehicleReport.vehicleColor
    val colorName = Color.decode(getcolor.toString)
    val seatcolor= newvehicleReport.colorofSeats
    val seatcol=Color.decode(seatcolor.toString)
    println(colorName + "" + seatcol)*/
    val dbnewReport = newvehicleReport.copy(_id= Some(id),typeOfReport= Some("Vehicle Report"),
                                            dateReported = Some(date), reportedBy= Some("Admin"))
/*
                                            vehicleColor = Some(colorName.toString),colorofSeats =  Some(seatcol.toString))
*/

    vehiclereport.insert(dbnewReport)
  }

  def find(query: BSONDocument ) = {

    import helpers.Implicits._

    val agg = Aggregate(vehiclereport.name,
      Seq(Match(query),
        Project(
          "_id"->1,
          "typeOfReport"->1,
          "vehicleType"->1,
          "vehicleMake"->1,
          "vehicleColor"->1,
          "doorNumber"->1,
          "typeofSeats"->1,
          "colorosSeats"->1,
          "plateNumber"->1,
          "lastlocationSeen"->1,
          "emptyVehicle"->1,
          "noofPeopleinvehicle"->1,
          "nameofOwner"->1,
          "genderofOwner"->1,
          "addressofOwner"->1,
          "ageofVictim"->1,
          "genderofVictim"->1,
          "suspicionText"->1,
          "specialFeature"->1,
          "dateReported"->1,
          "reportedBy"->1
        )))
    vehiclereport.db.command(agg)
  }
  def getAllreports={

    import helpers.Implicits._

    val agg = Aggregate(vehiclereport.name,
      Seq(Project("_id" -> 1,
        "typeOfReport" ->1,
        "vehicleType" -> 1,
        "vehicleMake" -> 1,
        "plateNumber" -> 1,
        "vehicleColor"->1,
        "specialFeature" -> 1,
        "dateReported"->1
      )))

    vehiclereport.db.command(agg)
  }

  def correlationData={

    import helpers.Implicits._

    val agg = Aggregate(vehiclereport.name,
      Seq(Project("_id" -> 1,
        "lastlocationSeen" ->1,
        "nameofOwner" -> 1,
        "addressofOwner" -> 1,
        "genderofOwner" -> 1,
        "suspicionText"->1,
        "ageofVictim" -> 1,
        "genderofVictim"->1
      )))

    vehiclereport.db.command(agg)
  }

}