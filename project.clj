(defproject shouter-datomic-heroku "0.0.3"
  :description "Shouter app with Datomic Pro and Postgresql on Heroku"
  :url "https://github.com/Clojurous/shouter-datomic-heroku"
  :min-lein-version "2.5.0"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :resource-paths ["resources"]

  ;:repositories {"my.datomic.com" {:url "https://my.datomic.com/repo"
  ;                                 :username (System/getenv "DATOMIC_REPO_USERNAME")
  ;                                 :password (System/getenv "DATOMIC_REPO_PASSWORD")}}
  :repositories {"private.repo" {:url "http://my.private.repo/repo"
                                 :username ~(System/getenv "PRIVATE_REPO_USERNAME")
                                 :password ~(System/getenv "PRIVATE_REPO_PASSWORD")}}

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/java.jdbc "0.4.2"]
                 [org.postgresql/postgresql "9.4-1201-jdbc41"]
                 [ring/ring-jetty-adapter "1.4.0"]
                 [compojure "1.4.0"]
                 [ring/ring-defaults "0.1.2"]
                 [hiccup "1.0.5"]
                 [com.datomic/datomic-transactor-pro "0.9.5327" :exclusions [ch.qos.logback/logback-classic org.slf4j/slf4j-nop joda-time]]
                 [com.datomic/datomic-pro    "0.9.5327" :exclusions [org.slf4j/slf4j-nop org.slf4j/slf4j-log4j12 joda-time]]
                 ;[org.clojure/data.json              "0.2.6"]
                 [datomic-schema "1.3.0"]
                 [ch.qos.logback/logback-classic "1.1.3"]]

  :main ^:skip-aot shouter.core
  :uberjar-name "shouter-standalone.jar"
  :plugins [[lein-ring "0.8.13"]]
  :ring {:handler shouter.web/application
         :init    shouter.models.migration/migrate}
  :profiles {:dev     {:dependencies [[javax.servlet/servlet-api "2.5"]
                                      [ring-mock "0.1.5"]]}
             :uberjar {:aot :all}})
