(set-env!
  :resource-paths #{"src"}
  :dependencies '[[org.clojure/clojure "1.8.0"]
                  [com.atomist/rug "0.25.3" :scope "provided"]
                  [org.clojure/data.json "0.2.6" :exclusions [org.clojure/clojure ]]
                  [clj-http "3.4.1" :exclusions [org.clojure/clojure ]]]
  :repositories [["public-atomist-release" {:url      "https://atomist.jfrog.io/atomist/libs-release"}]
                 ["release" {:url      "https://atomist.jfrog.io/atomist/libs-release-local"
                              :username (System/getenv "ATOMIST_REPO_USER")
                              :password (System/getenv "ATOMIST_REPO_PWD")}]
                 ["timestamp" {:url      "https://atomist.jfrog.io/atomist/libs-dev-local"
                              :username (System/getenv "ATOMIST_REPO_USER")
                              :password (System/getenv "ATOMIST_REPO_PWD")}]]
  :version "0.5.0-SNAPSHOT"
  :repo "timestamp")

(task-options!
  pom {:project 'com.atomist.rug/rug-function-http
       :version (get-env :version)
       :description "HTTP Rug Function"}
  aot {:namespace #{'com.atomist.rug.functions.rug-function-http.core}}
  push {:repo (get-env :repo)
        :gpg-sign true
        :ensure-release false
        :gpg-user-id "DA85ED8F"
        :gpg-passphrase (System/getenv "GPG_PASSPHRASE")})

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
