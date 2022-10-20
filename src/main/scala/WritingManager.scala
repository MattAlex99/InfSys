import spray.json.{JsString, JsonParser, enrichAny}

import java.sql.{Connection, DriverManager, PreparedStatement}
import java.io.FileInputStream
import java.nio.charset.StandardCharsets
import java.util.Scanner
import com.google.gson.Gson
import scala.annotation.tailrec


class WritingManager {
  val connection: Connection = DriverManager.getConnection("jdbc:h2:./demo")

  def readFileByLine(path:String):Unit={
    println("Start reading files")
    val is = new FileInputStream(path)

    try {
      //Vars nur für Lastentest

      val gson = new Gson
      val scanner = new Scanner(is, StandardCharsets.UTF_8.name())
      scanner.nextLine() //skipps first empty line
      while (scanner.hasNextLine) { // Printing the content of file
        TimeKeeper.checkTime()

        //writing and deserialisation from here on
        val rawLineString=scanner.nextLine.replaceAll("[^\\x00-\\x7F]", "?")
        rawLineString match {
          case "]" =>
          case "[" =>
          case _ => val clearedLineString = removeLeadingComma(rawLineString)
                    val currentArticle: Line = gson.fromJson(clearedLineString, classOf[Line])
                    //writing starts here

                    insertIntoArticle(currentArticle)
                    currentArticle.authors.forEach(author => insertIntoAuthor(author)) //insert into authors
                    currentArticle.authors.forEach(author => insertIntoArticleAuthor(currentArticle.id, author.id)) //insert into articleauthor

                    if (!(currentArticle.references == null)){
                      currentArticle.references.foreach(ref => insertIntoReference(currentArticle.id, ref))
                    }




        }
        }
    }
  }

  @tailrec
  final def removeLeadingComma(string: String):String = {
    string.take (1) match {
    case "," => removeLeadingComma(string.substring (1))
    case _ => string
    }
  }

  val insertIntoArticlePreparedString = "insert into article values(?,?,?,?,?,?,?,?,?,?,?)"
  val insertIntoArticleStatement: PreparedStatement = connection.prepareStatement(insertIntoArticlePreparedString)
  def insertIntoArticle(line:Line): Unit = {
    insertIntoArticleStatement.setLong(1, line.id)
    insertIntoArticleStatement.setString(2, line.title)
    insertIntoArticleStatement.setInt(3, line.year)
    insertIntoArticleStatement.setInt(4,line.n_citation)
    insertIntoArticleStatement.setString(5,line.page_start)
    insertIntoArticleStatement.setString(6,line.page_end)
    insertIntoArticleStatement.setString(7,line.doc_type)
    insertIntoArticleStatement.setString(8,line.publisher)
    insertIntoArticleStatement.setString(9,line.volume)
    insertIntoArticleStatement.setString(10,line.issue)
    insertIntoArticleStatement.setString(11,line.doi)

    try {
      insertIntoArticleStatement.execute()
    } catch {
      case e: java.sql.SQLException => print(e)
    }
  }

  val insertIntoAuthorPrepared="insert into author values(?,?,?)"
  val insertIntoAuthorStatement: PreparedStatement = connection.prepareStatement(insertIntoAuthorPrepared)
  def insertIntoAuthor(author:Author):Unit={
    insertIntoAuthorStatement.setLong(1,author.id)
    insertIntoAuthorStatement.setString(2,author.name)
    insertIntoAuthorStatement.setString(3,author.org)
    try {
      insertIntoAuthorStatement.execute()
    } catch {
      case e: java.sql.SQLException =>
    }
  }


  val insertIntoArticleAuthorPrepared = "insert into articleauthor values(?,?)"
  val insertIntoArticleAuthorStatement: PreparedStatement = connection.prepareStatement(insertIntoArticleAuthorPrepared)
  def insertIntoArticleAuthor(articleId:Long,authorId:Long): Unit = {
    insertIntoArticleAuthorStatement.setLong(1,articleId)
    insertIntoArticleAuthorStatement.setLong(2,authorId)
    insertIntoArticleAuthorStatement.execute()
    try {
    } catch {
      case e: java.sql.SQLException => print (e)
    }
  }

  val insertIntoRecerence = "insert into reference values(?,?)"
  val insertIntoReferenceStatement: PreparedStatement = connection.prepareStatement(insertIntoRecerence)

  def insertIntoReference(referencingArticleId:Long, referencedArticleId:Long): Unit = {
    insertIntoReferenceStatement.setLong(1, referencingArticleId)
    insertIntoReferenceStatement.setLong(2, referencedArticleId)
    try {
      insertIntoReferenceStatement.execute()
    } catch {
      case e: java.sql.SQLException => println(e)
    }
  }

}
