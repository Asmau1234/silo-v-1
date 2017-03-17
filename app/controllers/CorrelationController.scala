package controllers

import models.Vehicle._
import models._
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
  * Created by Asmau Muktar on 3/9/2017.
  */
class CorrelationController @Inject()(val reactiveMongoApi: ReactiveMongoApi) extends Controller with ReactiveMongoComponents
  with MongoController {

def index= Action{

  Ok("")
}

  def make=Action.async{

  val person= PersonReportDAO.correlationData
  val vehicle= VehicleReportDAO.correlationData
  val incident= IncidentReportDAO.correlationData



    for {
      person <- person
      vehicle <- vehicle
      incident <- incident
    } yield Ok(Json.obj("person" -> Json.toJson(person) , "vehicle" -> Json.toJson(vehicle), "incident" -> Json.toJson(incident)))

}

}
