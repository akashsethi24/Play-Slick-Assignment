package connection

/**
  * Created by akash on 8/3/16.
  */
trait PostgresComponent extends DBComponent {
  val driver = slick.driver.PostgresDriver

  import driver.api._

  val db: Database = PostgresDB.connectionPool
}

private[connection] object PostgresDB {
  import slick.driver.PostgresDriver.api._

  val connectionPool = Database.forConfig("postgres")
}
