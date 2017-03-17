package controllers

import javax.inject.Inject

import models._
import models.States._
import models.ConflictReport._
import play.api.Play._
import play.api.mvc.{Controller, Action}
import play.modules.reactivemongo.json.collection._
import javax.inject.Inject

import play.modules.reactivemongo.json.collection.JSONCollection
import play.modules.reactivemongo.{MongoController, ReactiveMongoComponents, ReactiveMongoApi}
import play.modules.reactivemongo.json._
import reactivemongo.bson._
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.json.Json._
import play.api.libs.json.Json

import scala.concurrent.Future


/**
  * Created by Asmau Muktar on 2/22/2017.
  */
class MapController @Inject()(val reactiveMongoApi: ReactiveMongoApi) extends Controller with ReactiveMongoComponents
  with MongoController {

  def states=reactiveMongoApi.db.collection[JSONCollection]("states")




  def index= Action{

    Ok("something")
  }

  def getStates = Action.async {

    states.find(Json.obj()).cursor[States].collect[List]().map{x =>
      Ok(Json.toJson(x))
    }

  }



  def getLga(state: String) = Action.async { //request =>
  val query = Json.obj("name" -> state)

    states.find(query).one[States].map {

      case Some(s: States) => Ok(Json.toJson(s.lgas))
      case None => Ok("None")
    }
  }


  def makeConflictreport= Action.async(parse.json){ implicit request=>

    val conflictReport= (request.body\"reportDets")

    println(conflictReport)
    ConflictReportDAO.add(conflictReport.as[ConflictReport])
    Future.successful(Ok(Json.toJson(Map("error"-> "Some error occured"))))
  }

  def searchConflicts = Action.async(parse.json){ request =>

    val search = (request.body \ "data" ).as[SearchConflict]

    println(search)
    search match {

      case SearchConflict(Some(state), Some(lga),Some(severity)) =>
        val state = search.state.getOrElse(None).toString
        val lga = search.lga.getOrElse(None).toString
        val severity = search.severity.getOrElse(None).toString
        val query = document("stateOfCrisis" -> state, "lgaOfCrisis" -> lga, "levelOfCrisis" -> severity)
        ConflictReportDAO.getLatLang(query).map{x =>
          Ok(Json.toJson(x))

        }

      case SearchConflict(Some(state),_, Some(severity))=>
        val state = search.state.getOrElse(None).toString
        val severity = search.severity.getOrElse(None).toString
        val query=document("stateOfCrisis"->state, "levelOfCrisis"->severity)
        ConflictReportDAO.getLatLang(query).map{x =>
          Ok(Json.toJson(x))

        }

      case SearchConflict(_,_, Some(severity))=>
        val severity = search.severity.getOrElse(None).toString
        val query=document("levelOfCrisis"->severity)
        ConflictReportDAO.getLatLang(query).map{x =>
          Ok(Json.toJson(x))

        }

      case SearchConflict(Some(state),_,_)=>
        val state = search.state.getOrElse(None).toString
        val query=document("stateOfCrisis"->state)
        ConflictReportDAO.getLatLang(query).map{x =>
          Ok(Json.toJson(x))

        }

      case SearchConflict(Some(state),Some(lga),_)=>
        val state = search.state.getOrElse(None).toString
        val lga = search.lga.getOrElse(None).toString
        val query=document("stateOfCrisis"->state, "lgaOfCrisis"->lga)
        ConflictReportDAO.getLatLang(query).map{x =>
          Ok(Json.toJson(x))

        }



    }



  }

  def onlyState(state:String,severity:String)= Action.async{

    val query=document("stateOfCrisis"->state, "levelOfCrisis"->severity)

    ConflictReportDAO.getLatLang(query).map{x =>
      Ok(Json.toJson(x))

    }
  }

  def onlySeverity(severity:String)= Action.async{

    val query=document( "levelOfCrisis"->severity)

    ConflictReportDAO.getLatLang(query).map{x =>
      Ok(Json.toJson(x))

    }
  }

}
