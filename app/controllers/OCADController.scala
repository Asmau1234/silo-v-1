package controllers


import models.Vehicle._
import models._
import models.Color._
import models.PersonReportDAO
import play.api.mvc.{Controller, Action}
import play.modules.reactivemongo.json.collection._
import javax.inject.Inject
import reactivemongo.bson._

import play.modules.reactivemongo.json.collection.JSONCollection
import play.modules.reactivemongo.{MongoController, ReactiveMongoComponents, ReactiveMongoApi}
import play.modules.reactivemongo.json._
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.json.Json
import play.api.libs.json.Json._

import scala.concurrent.Future


/**
  * Created by Asmau Muktar on 2/28/2017.
  */
class OCADController @Inject()(val reactiveMongoApi: ReactiveMongoApi) extends Controller with ReactiveMongoComponents
  with MongoController {

  def vehicles=reactiveMongoApi.db.collection[JSONCollection]("vehicles")
  def colors=reactiveMongoApi.db.collection[JSONCollection]("colors")



  def index=Action{

    Ok("Gotcha!")
  }

  def getVehicles = Action.async {

    vehicles.find(Json.obj()).cursor[Vehicle].collect[List]().map{x =>
      Ok(Json.toJson(x))
    }

  }

  def getColors = Action.async {

    colors.find(Json.obj()).cursor[Color].collect[List]().map{x =>
      Ok(Json.toJson(x))
    }

  }

  def getVehiclemakes(vehicle: String) = Action.async {
  val query = Json.obj("name" -> vehicle)

    vehicles.find(query).one[Vehicle].map {

      case Some(s: Vehicle) => Ok(Json.toJson(s.types))
      case None => Ok("None")
    }
  }

  def makePersonreport= Action.async(parse.json){request =>


    val personReport= (request.body)

    println(personReport)
    PersonReportDAO.add(personReport.as[PersonReport])
    Future.successful(Ok(Json.toJson(Map("error"-> "Some error occured"))))
  }

  def makeVehiclereport= Action.async(parse.json){ request =>

    val vehicleReport= (request.body)
    VehicleReportDAO.add(vehicleReport.as[VehicleReport])
    Future.successful(Ok(Json.toJson(Map("error"-> "Some error occured"))))
  }

  def makeIncidentreport= Action.async(parse.json){ request =>

    val incidentReport= (request.body)

    IncidentReportDAO.add(incidentReport.as[IncidentReport])
    Future.successful(Ok(Json.toJson(Map("error"-> "Some error occured"))))

  }

  def viewPersonreport=Action.async{

     PersonReportDAO.getAllreports.map{x =>
       Ok(Json.toJson(x))
}
  }

  def viewVehiclereport=Action.async{

    VehicleReportDAO.getAllreports.map{x =>
      Ok(Json.toJson(x))
    }
  }

  def viewIncidentreport=Action.async{

    IncidentReportDAO.getAllreports.map{x =>
      Ok(Json.toJson(x))
    }
  }

  def viewPReportDetails(id: Int)=Action.async{

    val query= Json.obj("_id"-> id)
    val report = PersonReportDAO.findOne(query)

    report.map{ e: Option[PersonReport] =>
      e match {
        case Some(t: PersonReport) =>
          Ok(Json.toJson(t))
        case None =>  (Ok("Report Not Found"))
      }

    }.recover{
      case _ => Ok("Network error")
    }
  }

  def viewIReportDetails(id: Int)=Action.async{

    val query= Json.obj("_id"-> id)
    val report = IncidentReportDAO.findOne(query)

    report.map{ e: Option[IncidentReport] =>
      e match {
        case Some(t: IncidentReport) =>
          Ok(Json.toJson(t))
        case None =>  (Ok("Report Not Found"))
      }

    }.recover{
      case _ => Ok("Network error")
    }

  }

  def viewVReportDetails(id: Int)=Action.async {

    val query = Json.obj("_id" -> id)
    val report = VehicleReportDAO.findOne(query)

    report.map { e: Option[VehicleReport] =>
      e match {
        case Some(t: VehicleReport) =>
          Ok(Json.toJson(t))
        case None => (Ok("Report Not Found"))
      }

    }.recover {
      case _ => Ok("Network error")
    }
  }
}
