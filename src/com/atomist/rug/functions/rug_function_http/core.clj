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
           (com.atomist.rug.spi Handlers$ Handlers$Status$Success$ Handlers$Status$Failure$ StringBodyOption FunctionResponse)
           (scala Option Some)))

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

(def success #{200 201 202 203 204 205 206 207 300 301 302 303 307})

(defn response-body
  "Create a complicated response body"
  [response]
  (FunctionResponse.
    (if (contains? success (:status response))
      Handlers$Status$Success$/MODULE$
      Handlers$Status$Failure$/MODULE$)
    (Some/apply nil)
    (Some. (:status response))
    (StringBodyOption/apply(:body response))))

(defn -init
  []
  [[] nil])

(defn get-value
  "Extract a value from the parameters by name"
  [params str-key & default]
  (if-let [val (some->
                 params
                 (.parameterValueMap)
                 (javarize)
                 (get str-key)
                 (.getValue))]
    val
    (first default)))

(def valid-methods #{"get" "put" "post" "head" "delete"})

(defn validate-method
  "Is the method allowed?. Throw exceptions if not"
  [method]
  (let [lower (.toLowerCase method)]
    (if (contains? valid-methods lower)
      lower
      (throw (RuntimeException. (str "Method must be one of: " (pr-str valid-methods)))))))

(defn run
  "Clean version of -run"
  [method url config]
  (apply
    (resolve (symbol (str "clj-http.client/" (validate-method method))))
    [url
     (-> config (json/read-str :key-fn keyword) (assoc :throw-exceptions false))]))

(defn -run
  "Dispatch HTTP requrest and return a HandlerResponse"
  [this ^ParameterValues params]
  (-> (run
        (get-value params "method")
        (get-value params "url")
        (get-value params "config" "{}"))
    (response-body)))

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
       (.setPattern "^get|put|post|head|delete$")
       (.describedAs "The HTTP method to use. One of get|put|post|head|delete"))

     (->
       (Parameter. "config")
       (.setPattern "^@any")
       (.setRequired false)
       (.describedAs "JSON string that represents everything we send to clj-http"))]))
