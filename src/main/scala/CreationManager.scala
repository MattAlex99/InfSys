import java.sql.{Connection, DriverManager, PreparedStatement}
import scala.io.Source._
import java.io.FileInputStream
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.util.Scanner
import com.google.gson.Gson
import java.util.Date
import scala.annotation.tailrec


class CreationManager{
  val connection: Connection = DriverManager.getConnection("jdbc:h2:./demo")
  def createArticleDB(): Unit = {
    val statement = connection.createStatement()
    val createArticleTable =
      """CREATE TABLE if not exists article(
        |id long primary key,
        |title varchar(600) NOT NULL,
        |`year` int NOT NULL,
        |n_citation int NOT NULL,
        |page_start varchar(20),
        |page_end varchar(20),
        |doc_type varchar(32),
        |publisher varchar(512),
        |volume varchar(32),
        |issue varchar(32),
        |doi varchar(200 )
        )""".stripMargin
    statement.execute(createArticleTable)
    statement.close()
    println("BD has been created")
  }

  def createAuthorDB(): Unit = {
    println("Creatign AuthorDB!")
    val statement = connection.createStatement()
    val createTable =
      """CREATE TABLE if not exists author(
        |id long primary key,
        |name varchar(100) NOT NULL,
        |org varchar(300)
        )""".stripMargin
    statement.execute(createTable)
    statement.close()
  }

  def createAtricleAuthorDB(): Unit = {
    println("Creatign ArticleAuthorDB!")
    val statement = connection.createStatement()
    val createArticleAuthorTable =
      """CREATE TABLE if not exists articleAuthor(
        |articleId long  references article ,
        |authorId long references author,
        |PRIMARY KEY (articleID,authorID)
        )""".stripMargin
    statement.execute(createArticleAuthorTable)
    statement.close()
  }

  def createReferneceDB(): Unit = {
    println("Creatign DB!")
    val statement = connection.createStatement()
    //
    val createTable =
      """CREATE TABLE if not exists reference(
        |referencingArticleId long,
        |referencedArticleId long,
        |PRIMARY KEY (referencingArticleId,referencedArticleId)
        )""".stripMargin
    statement.execute(createTable)
    statement.close()
  }

  def addReferencesConstraint():Unit={
    println("Creatign DB!")
    val statement = connection.createStatement()
    val alter_table1 =
      """ALTER TABLE Reference
        |ADD FOREIGN KEY (referencingArticleId) REFERENCES Article(id);""".stripMargin
    val alter_table2 =
      """ALTER TABLE Reference
        |ADD FOREIGN KEY (referencedArticleId) REFERENCES Article(id);""".stripMargin

    statement.execute(alter_table1)
    statement.execute(alter_table2)



    statement.close()
  }


}
