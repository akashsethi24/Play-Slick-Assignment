package connection

import slick.driver.JdbcProfile
/**
  * Created by akash on 8/3/16.
  */
trait DBComponent {

  val driver: JdbcProfile

  import driver.api._

  val db: Database

}
