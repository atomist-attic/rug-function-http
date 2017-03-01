(defproject com.atomist.rug/rug-function-http "0.0.2-SNAPSHOT"
  :description "HTTP Rug Function"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [com.atomist/rug "0.13.0-SNAPSHOT" :scope "provided"]
                 [org.clojure/data.json "0.2.6"]
                 [clj-http "3.4.1"]]
  :profiles {:dev {:dependencies [[midje "1.8.3"]]
                   :plugins [[lein-midje "3.1.3"]
                             [jonase/eastwood "0.2.1"]
                             [lein-cloverage "1.0.6"]
                             [lein-ancient "0.6.10" :exclusions [org.clojure/clojure]]]}}
  :aot [com.atomist.rug.functions.rug-function-http.core]
  :javac-options     ["-target" "1.8" "-source" "1.8"]
  :repositories [["public-atomist-release" {:url      "https://atomist.jfrog.io/atomist/libs-release"}]
                 ["releases" {:url      "https://atomist.jfrog.io/atomist/libs-release-local"
                              :sign-releases false
                              :username [:gpg :env/artifactory_user]
                              :password [:gpg :env/artifactory_pwd]}]
                 ["atomist-public" {:url "https://atomist.jfrog.io/atomist/libs-release"}]])
