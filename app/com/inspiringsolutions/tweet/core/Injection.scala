package com.inspiringsolutions.tweet.core

import play.api.Application

import scala.reflect.ClassTag

/**
  * Created by mtomanski on 06.04.16.
  */

object GlobalInjector {
  var app: Application = null
}

trait Injection {
  /**
    * Injects given dependency by type.
    */
  protected def inject[T: ClassTag]: T = getInstance

  private def getInstance[T](implicit tag: ClassTag[T]): T = GlobalInjector.app.injector.instanceOf[T]
}