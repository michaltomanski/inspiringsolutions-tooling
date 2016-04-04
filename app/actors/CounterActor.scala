package actors

import akka.actor.Actor

/**
  * Created by mtomanski on 04.04.16.
  */
class CounterActor extends Actor {

  var charCounter = 0

  override def receive: Receive = {
    case Message(text: String) =>
      sender() ! handleMessage(text)
    case GetCounter =>
      sender() ! getCounter
  }

  private def handleMessage(text: String) = {
    charCounter += text.length
    text
  }

  private def getCounter = {
    charCounter
  }


}

case class Message(text: String)

case object GetCounter

