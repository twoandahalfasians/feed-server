(defproject feed.server "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[cheshire "5.5.0"]
                 [compojure "1.3.1"]
                 [c3p0/c3p0 "0.9.1.2"]
                 [org.clojure/clojure "1.6.0"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [org.clojure/java.jdbc "0.4.2"]
                 [http-kit "2.1.19"]
                 [ring "1.4.0"]
                 [clj-http "2.0.0"]
                 [clj-time "0.11.0"]
                 [org.postgresql/postgresql "9.4-1201-jdbc41"]
                 [ring/ring-json "0.4.0"]]
  :plugins [[lein-ring "0.8.13"]]
  :ring {:handler feed.server.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}})
