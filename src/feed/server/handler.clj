(ns feed.server.handler
  (:require [clj-http.client :as client]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [compojure.handler :refer [site]]
            [ring.middleware.json :refer [wrap-json-body]] 
            [org.httpkit.server :as httpd]
            [feed.server.incoming :as in]))

(defonce server (atom nil))

(defn process-event [request]
  (in/enqueue-event (:body request))
  {:status 200})

(def app-routes
  (routes
   (GET  "/gettest" [] "YaBoiL got dis x2!")
   (POST "/newevent" [] process-event)
   (route/not-found "Not Found")))

(def app
  (-> app-routes
      site
      wrap-json-body))

(defn start-web-server [opts]
  (httpd/run-server #'app opts))

(defn -main [& args]
  (in/-main)
  (reset! server
          (start-web-server {:port 3030
                             :thread 4
                             :queue-size 20000})))

;; (GET "/upv/:e" [e :as eid]
;;        (do (->> eid
;;                 (str "UPDATE events SET votes = votes + 1 WHERE eid = ")
;;                 (vector)
;;                 (db/execute! pg-conn))
;;            {:status 200}))
;;   (GET "/dnv/:e" [e :as eid]
;;        (do (->> eid
;;               (str "UPDATE events SET votes = votes - 1 WHERE eid = ")
;;               (vector)
;;               (db/execute! pg-conn))
;;            {:status 200}))
