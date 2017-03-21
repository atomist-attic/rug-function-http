(ns com.atomist.rug.functions.rug-function-http.t-core
  (:use midje.sweet)
  (:require [com.atomist.rug.functions.rug-function-http.core :as http])
  (:import (com.atomist.param SimpleParameterValue SimpleParameterValues)
           (scala.collection JavaConversions)))

;(facts "about http rug function"
;  (fact "parameters are parsed in to a nice structure"
;    (http/parse-params (SimpleParameterValues. (.toList
;                                                 (JavaConversions/asScalaBuffer
;                                                   [(SimpleParameterValue. "url" "http://google.com")
;                                                    (SimpleParameterValue. "method" "get")
;                                                    (SimpleParameterValue. "config" "{}")]))))
;    => {:url "http://google.com", :method "get", :config "{}"}
;    ))
