object TimeKeeper {

  var linesRead= 1L
  var lastTimestamp = System.currentTimeMillis()


  def checkTime(): Unit ={
    linesRead=linesRead+1
    if (linesRead % 489408 == 0) {
      println("Current Line:" + linesRead)
      println("Duration in ms:" + (System.currentTimeMillis() - lastTimestamp))

      lastTimestamp = System.currentTimeMillis()
    }
  }

  def checkFinalTime():Unit={
    println ("Current Line:" + linesRead)
    println ("Duration in ms:" + (System.currentTimeMillis () - lastTimestamp) )
    lastTimestamp = System.currentTimeMillis ()
  }
}
