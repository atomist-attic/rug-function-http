(ns com.atomist.rug.functions.rug-function-http.t-whitelist
  (:use midje.sweet)
  (:require [com.atomist.rug.functions.rug-function-http.whitelist :as w]))

(fact "items in the whitelist should match"
  (w/allowed? "http://xkcd.com/blahd?blahdy-blah") => true)

(fact "carriage returns aren't matched"
  (w/allowed? "http://xkcd.com/blahd\n?blahdy-blah") => false)

(fact "non string args are not allowed"
  (w/allowed? {}) => false)

(fact "items not in the whitelist don't match"
  (w/allowed? "http://showmethekitties.com") => false)
