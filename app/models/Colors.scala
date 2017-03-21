package models

import com.google.inject.Inject
import play.api.libs.json.Json
import play.modules.reactivemongo._
import reactivemongo.bson.BSONObjectID
import reactivemongo.bson._


import scala.concurrent.ExecutionContext


/**
  * Created by Asmau Muktar on 3/21/2017.
  */
class ColorsDAO @Inject()(val reactiveMongoApi: ReactiveMongoApi)(implicit ex : ExecutionContext) {

}

case class Color(
                _id: Option[Int],
                hex: String,
                name: String,
                rgb: String
                )

object Color{

  implicit val color4Json= Json.format[Color]
}
