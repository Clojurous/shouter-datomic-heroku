(ns shouter.models.migration
  (:require
    [datomic.api :refer [q db] :as d]
    [datomic-schema.schema :as s]
    [shouter.models.shout :refer [uri]]
    ))

(def uri (System/getenv "HEROKU_DB_URI"))

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
          )))
    conn))

(defn migrate []

 (print "Creating database structure...") (flush)

 (setup-db uri)

 (println " done")

 )
