package akka.statsd.transport

import akka.actor._
import akka.io._
import akka.statsd.{TestKit, UdpListener}
import akka.testkit.ImplicitSender
import java.net.InetSocketAddress

import org.scalatest.Outcome
import org.scalatest.funspec.FixtureAnyFunSpecLike

class UdpUnconnectedSpec
  extends TestKit("udp-unconnected-spec")
  with FixtureAnyFunSpecLike
  with ImplicitSender {

  type FixtureParam = ActorRef

  override def withFixture(test: OneArgTest): Outcome = {
    val listener = system.actorOf(UdpListener.props(testActor), "other-end")
    val boundListenerAddress = expectMsgClass(classOf[InetSocketAddress])

    try
      test(system.actorOf(UdpUnconnected.props(boundListenerAddress)))
    finally
      listener ! Udp.Unbind

  }

  describe("Connection") {
    it("relays plain string messages") { connection =>
      connection ! "msg"
      expectMsg("msg")
    }
  }

}
