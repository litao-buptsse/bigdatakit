package com.sogou.bigdatakit.streaming.processor

import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.DStream

/**
  * Created by Tao Li on 6/16/16.
  */
abstract class RDDProcessor extends DStreamProcessor {
  def process(rdd: RDD[String])

  override def process(dstream: DStream[String]): Unit = {
    dstream.foreachRDD(process(_))
  }
}
