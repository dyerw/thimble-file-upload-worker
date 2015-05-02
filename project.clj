(defproject thimble-file-upload-worker "0.1.0-SNAPSHOT"
  :description "Uploads files to S3"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [clj-aws-s3 "0.3.10"]
                 [environ "1.0.0"]
                 [com.novemberain/langohr "3.0.1"]]
  :main ^:skip-aot thimble-file-upload-worker.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
