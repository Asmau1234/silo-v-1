package controllers

import javax.inject.Inject

import play.api.mvc.{Action, Controller}

/**
  * Created by Asmau Muktar on 2/22/2017.
  */
class LoginController @Inject() extends Controller {


  def index(any: String) = Action {
    Ok(views.html.index())
  }

  def login=Action {
    Ok(views.html.index())
  }
}
