package com.sogou.bigdatakit.streaming.processor

import org.apache.spark.rdd.RDD

/**
  * Created by Tao Li on 2015/8/28.
  */
abstract class LineProcessor extends RDDProcessor {
  def beforeBatch() = init()

  def afterBatch() = close()

  // for api compatibility
  def init() = {

  }

  // for api compatibility
  def close() = {

  }

  def process(message: String)

  override def process(rdd: RDD[String]): Unit = {
    rdd.foreachPartition { iter =>
      beforeBatch()
      iter.foreach(process(_))
      afterBatch()
    }
  }
}
