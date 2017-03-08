package models

import play.api.Play._
import play.modules.reactivemongo.ReactiveMongoApi

/**
  * Created by Asmau Muktar on 3/4/2017.
  */
class CorrelationMatrix {

}

object CorrelationMatrixDAO {

  lazy val reactiveMongoApi = current.injector.instanceOf[ReactiveMongoApi]




}
