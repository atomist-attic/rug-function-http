(defproject com.atomist.rug/rug-function-http "0.2.0"
  :description "HTTP Rug Function"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [com.atomist/rug "0.17.1" :scope "provided"]
                 [org.clojure/data.json "0.2.6"]
                 [clj-http "3.4.1"]]
  :profiles {:dev {:dependencies [[midje "1.8.3"]]
                   :plugins [[lein-midje "3.1.3"]
                             [jonase/eastwood "0.2.1"]
                             [lein-cloverage "1.0.6"]
                             [lein-ancient "0.6.10" :exclusions [org.clojure/clojure]]]}}
  :aot [com.atomist.rug.functions.rug-function-http.core]
  :javac-options     ["-target" "1.8" "-source" "1.8"]
  :repositories [["public-atomist-dev" {:url "https://atomist.jfrog.io/atomist/libs-dev"
                                        :snapshots false
                                        :releases {:checksum :fail :update :always}}]
                 ["public-atomist-release" {:url      "https://atomist.jfrog.io/atomist/libs-release"
                                            :snapshots false}]
                 ["releases" {:url      "https://atomist.jfrog.io/atomist/libs-release-local"
                              :sign-releases false
                              :snapshots false
                              :username [:gpg :env/atomist_repo_user]
                              :password [:gpg :env/atomist_repo_pwd]}]])
