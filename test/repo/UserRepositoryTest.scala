package repo

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.Application
import play.api.test.WithApplication
import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by prabhat on 9/3/16.
  */


@RunWith(classOf[JUnitRunner])
class UserRepositoryTest extends Specification{

  def userRepo(implicit app: Application) = Application.instanceCache[UserRepository].apply(app)
  "User repository" should {

    "get a record" in new WithApplication {
      val result = userRepo.getUser("prabhatkashyap33@gmail.com")
      val response = Await.result(result,Duration.Inf)
      response.head.name === "Prabhat Kashyap"
    }

    "get a invalid record" in new WithApplication {
      val result = userRepo.getUser("someone@knoldus.com")
      val response = Await.result(result,Duration.Inf)
      response === None
    }

  }

}


