package com.inspiringsolutions.tweet.controllers

import play.api.mvc._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class HomeController extends Controller {

  def index = Action.async {
    Future(Ok(views.html.index()))
  }
}
