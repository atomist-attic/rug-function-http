(ns com.atomist.rug.functions.rug-function-http.core
  (:gen-class
    :name com.atomist.rug.functions.HttpRugFunction
    :init init
    :constructors {[] []}
    :implements [com.atomist.rug.spi.RugFunction])
  (:require [clj-http.client :as client])
  (:import (com.atomist.param ParameterValues Parameter)
           (scala.collection Seq JavaConversions)
           (com.atomist.rug.spi Handlers$Response Handlers$ Handlers$Status$Success$)
           (scala None Option Some)))

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

(defn -init
  []
  [[] nil])

(defn -run
  "For now, just extract url and use GET"
  [this ^ParameterValues params]
  (let [results (client/get (-> params (.parameterValueMap) (javarize) (get "url") (.getValue)))]
    (Handlers$Response. Handlers$Status$Success$/MODULE$, nil, nil, (Some. results))))

(defn -name
  [this]
  "http")

(defn- tags
  [this]
  (scalarize ["http" "rug" "function" "clojure"]))

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
       (.setPattern "@any")
       (.setRequired false)
       (.describedAs "Config for the HTTP client as per: https://github.com/dakrone/clj-http"))]))
