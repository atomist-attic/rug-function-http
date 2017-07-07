(ns com.atomist.rug.functions.rug-function-http.whitelist
  (:require [clojure.edn :as edn]
            [clojure.core.memoize :as mem]))

;
; Simple domain whitelisting for now
;

(def static-whitelist
  {
   :github           {:patterns #{"^https://api\\.github\\.com/.*$"}}
   :google           {:patterns #{"^https://www\\.googleapis\\.com/.*$"}}
   :heroku           {:patterns #{"^https://api\\.heroku\\.com/.*$"}}
   :stack-overflow   {:patterns #{"^http://api\\.stackexchange\\.com/.*$"}}
   :yahoo            {:patterns #{"^http://query\\.yahooapis\\.com/.*$"}}
   :lebowski         {:patterns #{"^http://lebowski\\.me/.*$"}}
   :travis-com       {:patterns #{"^https://api\\.travis-ci\\.com/.*$"}}
   :travis-org       {:patterns #{"^https://api\\.travis-ci\\.org/.*$"}}
   :xkcd             {:patterns #{"^http://xkcd\\.com/.*$"}}
   :npm              {:patterns #{"^https://registry\\.npmjs\\.org/.*$"}}
   :jfrog            {:patterns #{"^https://[^/]+?\\.jfrog\\.io/.*$"}}
   :aws              {:patterns #{"^https://[^/]+?\\.amazonaws\\.com/.*$"}}
   :slack            {:patterns #{"^https://slack\\.com/api/.*$"}}
   :atomist-webhooks {:patterns #{"^https://webhook\\.atomist\\.com/.*$"}}})



(defn whitelist!
  "Calculate the combined whitelist"
  []
  (if-let [additional (System/getProperty "http.whitelist.file")]
    (merge static-whitelist (edn/read-string (slurp additional)))
    static-whitelist))

(def whitelist
  (mem/ttl whitelist!))

(defn allowed-patterns
  "Return a list of all patterns"
  []
  (->>
    (whitelist)
    vals
    (map :patterns)
    (reduce concat)))

(defn allowed?
  "Return true if and only if the url matches a pattern"
  [url]
  (->>
    (allowed-patterns)
    (some #(re-matches (re-pattern %) (.toString url)))
    boolean))
