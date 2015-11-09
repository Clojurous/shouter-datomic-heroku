(ns shouter.models.shout
  (:require                                                 ;[clojure.java.jdbc :as sql]
    [datomic.api :refer [q db] :as d]
    [shouter.models.migration :as mig]
    ))

(def uri (System/getenv "HEROKU_DB_URI"))


(defn all []
  (let [conn (d/connect uri)]
    (into {} (into [] (q '[:find ?e ?v :where [?e :shouts/body ?v]] (db conn))))))

(defn create [shout]
  (let [conn (d/connect uri)]
    (d/transact
     conn
     [{:db/id (d/tempid :db.part/user) :shouts/body shout}])))
