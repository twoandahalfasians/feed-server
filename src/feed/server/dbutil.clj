(ns feed.server.dbutil
  (:require [clojure.java.jdbc :as j])
  (:import com.mchange.v2.c3p0.DataSources
           org.postgresql.ds.PGSimpleDataSource))

(defn- pg-url-map
  [s]
  (let [uri (java.net.URI. s)
        [user password] (clojure.string/split (.getUserInfo uri) #":")
        server (.getHost uri)
        port (.getPort uri)
        database (-> (.getPath uri) (clojure.string/split #"/") (last))]
    {:user user :password password :server server :port port
     :database database :ssl true
     :sslfactory "org.postgresql.ssl.NonValidatingFactory"}))

(defn pg-pool
  "Create clojure.java.jdbc-friendly PostgreSQL connection pool for
  passed URL."
  [url]
  {:datasource (DataSources/pooledDataSource
                (let [info (pg-url-map url)]
                  (doto (PGSimpleDataSource.)
                    (.setServerName (:server info))
                    (.setPortNumber (:port info))
                    (.setUser (:user info))
                    (.setDatabaseName (:database info))
                    (.setPassword (:password info))
                    (.setSsl (:ssl info))
                    (.setSslfactory (:sslfactory info)))))})
