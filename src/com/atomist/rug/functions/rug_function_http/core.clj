(ns com.atomist.rug.functions.rug-function-http.core
  (:gen-class
    :name com.atomist.rug.functions.HttpRugFunction
    :init init
    :constructors {[] []}
    :implements [com.atomist.rug.spi.RugFunction])
  (:require [clj-http.client :as client]
            [clojure.data.json :as json])
  (:import (com.atomist.param ParameterValues Parameter)
           (scala.collection Seq JavaConversions)
           (com.atomist.rug.spi Handlers$Response Handlers$ Handlers$Status$Success$)
           (scala None Option Some)
           (com.atomist.rug.runtime InstructionResponse)))

;
; HTTP Rug Function
;

(defn- scalarize
  "Convert clojure collections to scala ones"
  [list]
  (if (sequential? list)
    (.toList
      (JavaConversions/asScalaBuffer list))))

(defn- javarize
  [coll]
  (JavaConversions/mapAsJavaMap coll))

(defn response-body
  "Create a complicated response body"
  [response]
  (Some.
    (InstructionResponse. nil, (:status response) (:body response))))

(defn -init
  []
  [[] nil])

(defn ?assoc
  "Same as assoc, but skip the assoc if v is nil, and keywordize key"
  [params nice-map str-key]
  (let [val (some->
              params
              (.parameterValueMap)
              (javarize)
              (get str-key)
              (.getValue))]
    (if val
      (assoc nice-map (keyword str-key) val)
      nice-map)))

(defn parse-params
  "Extract make them nicer to access"
  [^ParameterValues params]
  (reduce #(?assoc params %1 %2) {} ["url" "method" "config"]))


(defn -run
  "Dispatch HTTP requrest and return a HandlerResponse"
  [this ^ParameterValues params]

  (let [parsed-params (parse-params params)
        url           (:url parsed-params)
        config        (json/read-str (get parsed-params "config" "{}"))]

    (->>
      (case (:method parsed-params)
           :get (client/get url config)
           :post (client/post url config)
           :put (client/put url config)
           :head (client/post url config))
      (response-body)
      (Handlers$Response. Handlers$Status$Success$/MODULE$, nil, nil))))

(defn -name
  [this]
  "http")

(defn- tags
  [this]
  (scalarize ["http" "rug" "function" "clojure" "clj-http"]))

(defn -description
  [this]
  "HTTP Rug Function based on clj-http")

(defn -secrets
  "No secrets for now"
  [this]
  (scalarize []))

(defn -parameters
  "A single one containing the full spec"
  [this]
  (scalarize
    [(->
       (Parameter. "url")
       (.setPattern "@url")
       (.describedAs "The HTTP URL to call"))
     (->
       (Parameter. "method")
       (.setPattern "^get|put|post|head$")
       (.describedAs "The HTTP method to use. One of get|put|post|head"))

     (->
       (Parameter. "config")
       (.setPattern "^@any")
       (.setRequired false)
       (.describedAs "JSON string that represents everything we send to clj-http"))]))
