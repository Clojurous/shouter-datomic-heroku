(ns shouter.core
  (:use [compojure.core :only (defroutes)]
        [ring.adapter.jetty :as ring])
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [shouter.controllers.shouts :as shouts]
            [shouter.views.layout :as layout]
            [shouter.models.migration :as schema]
            [datomic.transactor]
            )
  (:gen-class))

(defroutes routes
  shouts/routes
  (route/resources "/")
  (route/not-found (layout/four-oh-four)))

(defn wrap-connection [handler conn]
  (fn [req] (handler (assoc req :dbconn conn))))

;(defn application [conn] #(handler/site (wrap-connection routes conn)))

(def application (handler/site routes))


;(defn start [port conn]
;  (run-jetty (application conn) {:port  port
;                          :join? false}))

(defn start [port]
  (run-jetty application {:port  port
                          :join? false}))


(def ^:private transactor-defaults
  {:license-key            (System/getenv "DATOMIC_LICENSE_KEY")
   :protocol               "sql"
   :host                   "localhost"
   :port                   "4334"
   :memory-index-max       "128m"
   :memory-index-threshold "32m"
   :object-cache-max       "128m"
   ;:log-dir                "/tmp"
   ;:data-dir               "/tmp"
   ;:sql-url                "jdbc:postgresql://localhost:5432/datomic"
   ;:sql-user               "datomic"
   ;:sql-password           "datomic"
   :sql-driver-class       "org.postgresql.Driver"
   :sql-url                (System/getenv "HEROKU_DB_URL")
   :sql-user               (System/getenv "HEROKU_DB_USER")
   :sql-password           (System/getenv "HEROKU_DB_PASSWORD")
   ;:sql-sslmode            "require"

   })

;:data-dir (.getPath dir)


(defn -main []
  (println "about to run datomic.transactor ...")
  (datomic.transactor/run transactor-defaults "datomic boot task options")
  (println "just ran datomic.transactor")

  (start (Integer/parseInt (or (System/getenv "PORT") "8080")))
  )
