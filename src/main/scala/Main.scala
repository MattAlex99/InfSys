object Main {
  def main(args: Array[String]): Unit = {

    val creationManager = new CreationManager
    creationManager.createArticleDB()
    creationManager.createAuthorDB()
    creationManager.createReferneceDB()
    creationManager.createAtricleAuthorDB()
    val writingManager = new WritingManager

    val timeBefore = System.currentTimeMillis()
    writingManager.readFileByLine("/home/alexander/IdeaProjects/large_files/dblp.v12.json/dblp.v12.json")
    val timeAfter=  System.currentTimeMillis()
    creationManager.addReferencesConstraint()
    println("Time spent on operations (in ms): "+ (timeAfter-timeBefore))
    TimeKeeper.checkFinalTime()
  }
}
