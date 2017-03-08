package models

import com.google.inject.Inject
import play.api.libs.json.Json
import play.modules.reactivemongo.ReactiveMongoApi

import scala.concurrent.ExecutionContext


/**
  * Created by Asmau on 2/25/2016.
  */

class StatesDAO @Inject()(val reactiveMongoApi: ReactiveMongoApi)(implicit ex : ExecutionContext) {

}

case class Lga(_id:Option[String], name: Option[String], code:Option[String])


case class States(
                   _id: Option[String],
                   code: Option[String],
                   name: Option[String],
                   lgas: Seq[Lga]

                 )

object States {

  implicit val lga4json = Json.format[Lga]
  implicit val state4json = Json.format[States]

}
