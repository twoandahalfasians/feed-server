(ns feed.server.incoming
  (:require [clj-time.coerce :as c] 
            [cheshire.core :as json]
            [clojure.java.jdbc :as db]
            [clojure.core.async :refer [go >! <!! chan thread close!]]
            [feed.server.dbutil :as dbutil]))

(def pg-conn (atom nil))
(def current-chan (atom nil))

(defn text->datetime [fieldv m]
  (map #(assoc m fieldv (c/from-string (% m))) fieldv))

(defn insert-event [j]
  (->> (json/parse-string j true)
       (text->datetime [:tstart :tstop])
       (db/insert! @pg-conn :events)))

(defn enqueue-event [e]
  (go (>! current-chan e)))

(defn start-looping-take [channel]
  "Spins up a new thread to process wins."
  (thread (loop [e (<!! channel)]
            (if e
              (do (insert-event e)
                  (recur (<!! channel)))
              (println "Channel is closed. Returning thread.")))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn open-channel! []
  (reset! current-chan (chan 2000)))

(defn close-channel! []
  (close! current-chan))

(defn reset-channel! []
  (close-channel!)
  (open-channel!))

(defn reset-pg! []
  (reset! pg-conn
          (dbutil/pg-pool (str "postgres://"
                                   "powerusr"
                                   ":5eguridad"
                                   "@efeedpostgres.cegkih7lu9yt.us-east-1.rds.amazonaws.com"
                                   ":5432"
                                   "/coredb"))))

(defn -main [& args]
  (reset-pg!)
  (open-channel!)
  (start-looping-take @current-chan))

