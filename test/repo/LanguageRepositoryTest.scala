package repo

import models.Languages
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
class LanguageRepositoryTest extends Specification{

  def langRepo(implicit app: Application) = Application.instanceCache[LanguageRepository].apply(app)
  "User repository" should {

    "get a record" in new WithApplication {
      val result = langRepo.getLanguageByUser(1)
      val response = Await.result(result,Duration.Inf)
      response.head.name === "Hindi"
    }

    "get an invalid record" in new WithApplication {
      val result = langRepo.getLanguageByUser(11)
      val response = Await.result(result,Duration.Inf)
      response === Nil
    }

    "Delete a Langauge" in new WithApplication {
      val result = langRepo.deleteLanguage(1)
      val response = Await.result(result,Duration.Inf)
      response === 1
    }

    "Delete a Invalid Langauge" in new WithApplication {
      val result = langRepo.deleteLanguage(11)
      val response = Await.result(result,Duration.Inf)
      response === 0
    }

    "Add a Langauge" in new WithApplication {
      val result = langRepo.insertLanguage(Languages(2,1,"English","Good"))
      val response = Await.result(result,Duration.Inf)
      response === 2
    }

    "Update a Language" in new WithApplication {
      val result = langRepo.updateLanguage(Languages(1,1,"English","Good"))
      val response = Await.result(result,Duration.Inf)
      response === 1
    }

    "Update an Invalid Language" in new WithApplication {
      val result = langRepo.updateLanguage(Languages(3,1,"English","Good"))
      val response = Await.result(result,Duration.Inf)
      response === 0
    }

  }

}


