package akka.statsd
package transport

import java.net.InetSocketAddress

import akka.io._
import akka.actor._
import akka.testkit.ImplicitSender
import org.scalatest.Outcome
import org.scalatest.funspec.FixtureAnyFunSpecLike

class ConnectionSpec
  extends TestKit("connection-spec")
  with FixtureAnyFunSpecLike
  with ImplicitSender {

  type FixtureParam = ActorRef

  override def withFixture(test: NoArgTest): Outcome = super.withFixture(test)

  override def withFixture(test: OneArgTest): Outcome = {
    val listener = system.actorOf(UdpListener.props(testActor), "other-end")
    val boundListenerAddress = expectMsgClass(classOf[InetSocketAddress])

    try
      test(system.actorOf(Connection.props(boundListenerAddress, 2)))
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
