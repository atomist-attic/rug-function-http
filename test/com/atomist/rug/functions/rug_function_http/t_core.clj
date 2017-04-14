(ns com.atomist.rug.functions.rug-function-http.t-core
  (:use midje.sweet)
  (:require [com.atomist.rug.functions.rug-function-http.core :as http])
  (:import (com.atomist.param SimpleParameterValue SimpleParameterValues)
           (scala.collection JavaConversions)))


(fact "exceptions are thrown for non-whitelisted urls"
  (http/run "get" "http://google.com" "{}") => (throws))

(fact "no exeptions are thrown for a whitelisted url"
  (http/run "get" "http://xkcd.com" "{}") => (fn [res] (= 200 (:status res))))

