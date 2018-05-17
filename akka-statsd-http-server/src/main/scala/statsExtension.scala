package akka.statsd.http.server

import akka.actor.{ActorRef, ExtendedActorSystem, Extension, ExtensionId}
import akka.statsd.Stats
import akka.statsd.{Config => StatsConfig, _}
import java.util.concurrent.ConcurrentHashMap

class StatsExtensionImpl(system: ExtendedActorSystem) extends Extension {
  private val actors = new ConcurrentHashMap[StatsConfig, ActorRef]()

  def statsActor(config: StatsConfig): ActorRef =
    actors.computeIfAbsent(config, c => system.actorOf(Stats.props(c)))
}

object StatsExtension extends ExtensionId[StatsExtensionImpl] {
  override def createExtension(system: ExtendedActorSystem) =
    new StatsExtensionImpl(system)
}
