package repo

import models.Assignments
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
class AssignmentRepositoryTest extends Specification{

  def assignRepo(implicit app: Application) = Application.instanceCache[AssignmentRepository].apply(app)
  "User repository" should {

    "get a assignment" in new WithApplication {
      val result = assignRepo.geAssignmentByUser(1)
      val response = Await.result(result,Duration.Inf)
      response.head.name === "Scala Assignment"
    }

    "get an invalid assignment" in new WithApplication {
      val result = assignRepo.geAssignmentByUser(11)
      val response = Await.result(result,Duration.Inf)
      response === Nil
    }

    "delete an assignment" in new WithApplication {
      val result = assignRepo.deleteAssignment(1)
      val response = Await.result(result,Duration.Inf)
      response === 1
    }

    "delete an invalid assignment" in new WithApplication {
      val result = assignRepo.deleteAssignment(111)
      val response = Await.result(result,Duration.Inf)
      response === 0
    }

    "Add an assignment" in new WithApplication {
      val result = assignRepo.insertAssignment(Assignments(2,1,"Play Assignment","34/13/2014","Very Bad",1))
      val response = Await.result(result,Duration.Inf)
      response === 2
    }

    "Update an assignment" in new WithApplication {
      val result = assignRepo.updateAssignment(Assignments(1,1,"Play Assignment","34/13/2014","Very Bad",1))
      val response = Await.result(result,Duration.Inf)
      response === 1
    }

    "Update an invalid assignment" in new WithApplication {
      val result = assignRepo.updateAssignment(Assignments(3,1,"Play Assignment","34/13/2014","Very Bad",1))
      val response = Await.result(result,Duration.Inf)
      response === 0
    }

  }

}


