package repo

import models.ProgrammingLanguages
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
class ProgrammingLangRepositoryTest extends Specification{

  def programLangRepo(implicit app: Application) = Application.instanceCache[ProgrammingLangRepository].apply(app)
  "Programming repository" should {

    "get a record" in new WithApplication {
      val result = programLangRepo.getProgrammingLangByUser(1)
      val response = Await.result(result,Duration.Inf)
      response.head.name === "Scala"
    }

    "get an invalid record" in new WithApplication {
      val result = programLangRepo.getProgrammingLangByUser(111)
      val response = Await.result(result,Duration.Inf)
      response === Nil
    }

    "Delete a record" in new WithApplication {
      val result = programLangRepo.deleteProgrammingLang(1)
      val response = Await.result(result,Duration.Inf)
      response === 1
    }

    "Delete an invalid record" in new WithApplication {
      val result = programLangRepo.deleteProgrammingLang(1111)
      val response = Await.result(result,Duration.Inf)
      response === 0
    }

    "Insert a record" in new WithApplication {
      val result = programLangRepo.insertProgrammingLang(ProgrammingLanguages(2,1,"Java","Bad"))
      val response = Await.result(result,Duration.Inf)
      response === 2
    }

    "Update a record" in new WithApplication {
      val result = programLangRepo.updateProgrammingLang(ProgrammingLanguages(1,1,"Java","Bad"))
      val response = Await.result(result,Duration.Inf)
      response === 1
    }

    "Update a invalid record" in new WithApplication {
      val result = programLangRepo.updateProgrammingLang(ProgrammingLanguages(3,1,"Java","Bad"))
      val response = Await.result(result,Duration.Inf)
      response === 0
    }

  }

}


