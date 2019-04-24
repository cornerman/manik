import Module.ActorMaterializerProvider
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.google.inject.{AbstractModule, Inject, Provider}

class Module extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[ActorMaterializer]).toProvider(classOf[ActorMaterializerProvider])
  }
}

object Module {
  class ActorMaterializerProvider @Inject()(implicit system: ActorSystem) extends Provider[ActorMaterializer] {
    val materializer = ActorMaterializer()

    override def get(): ActorMaterializer = materializer
  }
}
