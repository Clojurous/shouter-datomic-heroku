# shouter-datomic-heroku

The key to this working is running the Datomic Transactor in the same JVM as the app.
This is an unsupported way to run Datomic and prevents scaling to multiple peers per transactor.
It may be useful for rapid development or microservices based on Datomic Pro backed by PostgreSQL.

You'll need to deploy the datomic jars to a private repo that's accesible by Heroku
(see https://devcenter.heroku.com/articles/deploying-clojure and https://github.com/technomancy/s3-wagon-private)

```
mvn deploy:deploy-file -DpomFile=pom.xml \
  -Dfile=datomic-pro-0.9.5327.jar \
  -Durl=file:repo

mvn deploy:deploy-file -DpomFile=transactor-pom.xml \
  -Dfile=datomic-transactor-pro-0.9.5327.jar \
  -Durl=file:repo  
```

## License

Copyright Aaron Bedra, Thomas Spellman

Distributed under the Eclipse Public License, the same as Clojure.
