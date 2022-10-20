import spray.json.{DefaultJsonProtocol, jsonReader}

import java.util
import java.util.LinkedList

case class Line(id:Long,
                authors:util.LinkedList[Author],
                title:String,
                year:Int, //
                n_citation:Int, //
                doc_type:String,
                page_start:String,
                page_end:String,
                publisher:String,
                volume:String,
                issue:String,
                doi:String,
                references: Array[Long]
               )
