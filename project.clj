(defproject com.atomist.rug.functions/rug-function-http "0.0.1-SNAPSHOT"
  :description "HTTP Rug Function"
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :profiles {:dev {:dependencies [[midje "1.8.3"]]
                   :plugins [[lein-midje "3.1.3"]]}})
