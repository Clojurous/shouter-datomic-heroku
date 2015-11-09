(ns shouter.models.migration
  (:require
    ;[clojure.java.jdbc :as sql]
    ;[shouter.models.shout :as shout]
    [datomic.api :refer [q db] :as d]
    [datomic-schema.schema :as s]
    ))

(defn attr-exists? [attr db]
  (q '[:find ?e :in $ ?attr :where [?e :db/ident ?attr]] db attr)
  ;dbval.entity(attr).get(":db.install/_attribute")
  )

(defn migrated? [uri]
  (-> (attr-exists? :shouts/body (db (d/connect uri))) first count pos?)
  )

(defn dbparts []
  [(s/part "app")])

(defn dbschema []
  [
   ;(s/schema user
   ;  (fields
   ;    [username :string :indexed]
   ;    [pwd :string "Hashed password string"]
   ;    [email :string :indexed]
   ;    [status :enum [:pending :active :inactive :cancelled]]
   ;    [group :ref :many]))
   ;
   ;(s/schema group
   ;  (s/fields
   ;    [name :string]
   ;    [permission :string :many]))

   (s/schema shouts
     (s/fields
       [body :string]
       ))

   ])

(defn setup-db [url]
  (let [new? (d/create-database url)
        conn (d/connect url)]
    (if new?
      (d/transact
        conn
        (concat
          (s/generate-parts (dbparts))
          (s/generate-schema (dbschema))
          ;(s/dbfns->datomic dbinc)
          )))
    conn))

;(defn sample-data []
;  (let [gid (d/tempid :db.part/user)]
;    (d/transact
;      conn2
;      [{:db/id (d/tempid :db.part/user) :shouts/body "oh yeah"}
;       {:db/id (d/tempid :db.part/user) :shouts/body "bob"}])))
;
;(defn getem []
;  (q '[:find ?e ?v :where [?e :shouts/body ?v]] (db conn2))
;  (q '[:find ?v :where [_ :shouts/body ?v]] (db conn2))
;  (q '[:find ?e ?v :where [?e  ?v]] (db conn2))
;  )
;(declare conn)

;(defn migrate [uri]
;
;  (print "Creating database structure...") (flush)
;
;  ;(set! conn (setup-db uri))
;
;  (println " done")
;
;  conn
;
;  ;(when (not (migrated? uri))
;  ;  )
;  )

