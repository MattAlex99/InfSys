object Main {
  def main(args: Array[String]): Unit = {

    val creationManager = new CreationManager
    creationManager.createArticleDB()
    creationManager.createAuthorDB()
    creationManager.createReferneceDB()
    creationManager.createAtricleAuthorDB()
    val writingManager = new WritingManager

    val timeBefore = System.currentTimeMillis()
    writingManager.readFileByLine("D:\\Uni\\Inf-Sys\\dblp.v12.json\\dblp.v12.json")
    val timeAfter=  System.currentTimeMillis()

    println("All lines have been processed")
    creationManager.addReferencesConstraint()
    println("Time spent on operations (in ms): "+ (timeAfter-timeBefore))
    TimeKeeper.checkFinalTime()
  }
}
