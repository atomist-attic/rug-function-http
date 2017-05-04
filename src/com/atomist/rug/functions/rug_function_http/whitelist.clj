(ns com.atomist.rug.functions.rug-function-http.whitelist)

;
; Simple domain whitelisting for now
;

(def whitelist
  {
   :github         {:patterns #{#"^https://api\.github\.com/.*$"}}
   :google         {:patterns #{#"^https://www\.googleapis\.com/.*$"}}
   :heroku         {:patterns #{#"^https://api\.heroku\.com/.*$"}}
   :stack-overflow {:patterns #{#"^http://api\.stackexchange\.com/.*$"}}
   :yahoo          {:patterns #{#"^http://query\.yahooapis\.com/.*$"}}
   :lebowski       {:patterns #{#"^http://lebowski\.me/.*$"}}
   :xkcd           {:patterns #{#"^http://xkcd\.com/.*$"}}
   :npm            {:patterns #{#"^https://registry\.npmjs\.org/.*$"}}
   :jfrog          {:patterns #{#"^https://.+?\.jfrog\.io/.*$"}}
   :aws            {:patterns #{#"^https://.+?\.amazonaws\.com/.*$"}}})

(defn allowed-patterns
  "Return a list of all patterns"
  []
  (->>
    whitelist
    vals
    (map :patterns)
    (reduce concat)))

(defn allowed?
  "Return true if and only if the url matches a pattern"
  [^String url]
  (if (string? url)
    (->>
      (allowed-patterns)
      (some #(re-matches % url))
      boolean)
    false))
