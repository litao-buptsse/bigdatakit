package com.sogou.bigdatakit.streaming.processor;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.streaming.api.java.JavaDStream;

/**
 * Created by Tao Li on 6/16/16.
 */
public abstract class JavaRDDProcessor extends JavaDStreamProcessor {
  public abstract void process(JavaRDD<String> rdd);

  @Override
  public void process(JavaDStream<String> dstream) {
    dstream.foreach(new Function<JavaRDD<String>, Void>() {
      @Override
      public Void call(JavaRDD<String> rdd) throws Exception {
        process(rdd);
        return null;
      }
    });
  }
}
