package models

import com.google.inject.Inject
import play.api.libs.json.Json
import play.modules.reactivemongo.ReactiveMongoApi

import scala.concurrent.ExecutionContext

/**
  * Created by Asmau Muktar on 3/1/2017.
  */

class VehiclesDAO @Inject()(val reactiveMongoApi: ReactiveMongoApi)(implicit ex : ExecutionContext) {

}

case class VehicleMakes(
                         _id: Option[Long],
                         name: Option[String]
                       )

case class Vehicle(
                   _id: Option[Long],
                   name: Option[String],
                   types: Seq[VehicleMakes],
                   vehicleType: Option[String]
                   )



object Vehicle {

  implicit val vehiclemake4json = Json.format[VehicleMakes]
  implicit val vehicle4json = Json.format[Vehicle]

}