(set-env!
  :resource-paths #{"src"}
  :dependencies '[[org.clojure/clojure "1.8.0"]
                  [com.atomist/rug "0.25.3" :scope "provided"]
                  [org.clojure/data.json "0.2.6" :exclusions [org.clojure/clojure ]]
                  [clj-http "3.4.1" :exclusions [org.clojure/clojure ]]
                  [org.clojure/core.memoize "0.5.8"]
                  [zilti/boot-midje "0.2.2-SNAPSHOT" :scope "test"]
                  [onetom/boot-lein-generate "0.1.3" :scope "test"]
                  [midje "1.8.3" :scope "test"]]
  :repositories [["public-atomist-release" {:url      "https://atomist.jfrog.io/atomist/libs-release"}]
                 ["release" {:url      "https://atomist.jfrog.io/atomist/libs-release-local"
                              :username (System/getenv "ATOMIST_REPO_USER")
                              :password (System/getenv "ATOMIST_REPO_PWD")}]
                 ["timestamp" {:url      "https://atomist.jfrog.io/atomist/libs-dev-local"
                              :username (System/getenv "ATOMIST_REPO_USER")
                              :password (System/getenv "ATOMIST_REPO_PWD")}]]
  :version "0.8.0-SNAPSHOT"
  :repo "timestamp"
  :resource-paths #{"src" "resources"}
  :source-paths #{"src"})

(require
  '[zilti.boot-midje :refer [midje]]
  '[boot.lein])

(boot.lein/generate)

(task-options!
  pom {:project 'com.atomist.rug/rug-function-http
       :version (get-env :version)
       :description "HTTP Rug Function"}
  aot {:namespace #{'com.atomist.rug.functions.rug-function-http.core
                    'com.atomist.rug.functions.rug-function-http.whitelist}}
  push {:repo (get-env :repo)
        :gpg-sign true
        :ensure-release false
        :gpg-user-id "DA85ED8F"
        :gpg-passphrase (System/getenv "GPG_PASSPHRASE")}
  midje {:test-paths #{"test"}})

(deftask build
  "Build my project."
  [v version V str "The version to build."]
  (comp
    (aot)
    (pom :version (if version version (get-env :version)))
    (jar)
    (install)))

(deftask deploy
  "Deploy to maven repo"
  [v version V str "The version to build."
   r repo R str "The repo to deploy to."]
  (comp
    (build :version (if version version (get-env :version)))
    (push :repo (if repo repo (get-env :repo)))))

(deftask print-version
  "Print out the current version"
  []
  (println (get-env :version))
  identity)
