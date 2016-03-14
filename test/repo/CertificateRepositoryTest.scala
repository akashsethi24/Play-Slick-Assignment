package repo

import models.Certificates
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
class CertificateRepositoryTest extends Specification{

  def certRepo(implicit app: Application) = Application.instanceCache[CertificatesRepository].apply(app)

  "Certificate repository" should {

    "get a certificate" in new WithApplication {
      val result = certRepo.getCertificatesByUser(1)
      val response = Await.result(result,Duration.Inf)
      response.head.name === "Test Certificate"
      response.head.description === "Test Desc"
    }

    "Insert a certificate" in new WithApplication {
      val result = certRepo.insertCertificate(Certificates(2,1,"Another Certificate","Another Desc",2015))
      val response = Await.result(result,Duration.Inf)
      response === 2
    }

    "Delete a certificate" in new WithApplication {
      val result = certRepo.deleteCertificate(1)
      val response = Await.result(result,Duration.Inf)
      response === 1
    }

    "Delete a invalid certificate" in new WithApplication {
      val result = certRepo.deleteCertificate(122)
      val response = Await.result(result,Duration.Inf)
      response === 0
    }

    "Update certificate" in new WithApplication {
      val result = certRepo.updateCertificate(Certificates(1,1,"Another Certificate","Another Desc",2015))
      val response = Await.result(result,Duration.Inf)
      response === 1
    }

  }

}


