(ns thimble-file-upload-worker.core
  (:require [aws.sdk.s3 :as s3]
            [clojure.java.io :as io]
            [langohr.core :as rmq]
            [langohr.consumers :as lc]
            [langohr.channel :as lch]
            [langohr.queue :as lq]
            [environ.core :refer [env]]))

;;; AWS Config
(def cred {:access-key (env :awsaccesskey)
           :secret-key (env :awssecretkey)})

(def audio-bucket "thimble-audio")

;;; RabbitMQ Config
(def rmq-conn (rmq/connect {:username (env :rmquser)
                            :password (env :rmqpass)}))
(def rmq-ch (lch/open rmq-conn))
(def qname "thimble.jobs.file-upload")
(def default-exchange "")
(lq/declare rmq-ch qname {:exclusive false})

(defn upload-to-s3
  "Uploads a file from a given file path to AWS S3 and generates
   a unique id for it, then returns that unique id."
  [file-path]
  (let [s3-file-id (str (java.util.UUID/randomUUID))]
    (do
      (s3/put-object cred audio-bucket s3-file-id
        (slurp (io/file file-path)))
      s3-file-id)))

(defn upload-job-handler
  "Handles jobs coming in on the RabbitMQ queue."
  [ch metadata ^bytes payload]
  (println (String. payload)))  

(defn -main
  [& args]
  (lc/subscribe rmq-ch qname upload-job-handler {:auto-ack true}))

