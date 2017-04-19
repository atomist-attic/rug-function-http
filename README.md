# rug-function-http

[![Build Status](https://travis-ci.org/atomist/rug-function-http.svg?branch=master)](https://travis-ci.org/atomist/rug-function-http)

HTTP Rug Function based on https://github.com/dakrone/clj-http

## Parameters

*   `url`: string - Used for all methods
*   `method`: string - One of `get`, `post`, `put`, `head` and `delete`
*   `config`: object(map) - JSON serializable datastructure representing the rest of the request
    * e.g. {body: "some string", headers: {content-type: "application/json"}} - as per https://github.com/dakrone/clj-http

## Whitelisting

Currently we employ strict URL whitelisting to _all_ requests. Below is the current whitelist:

```clojure
  {
   :github         {:patterns #{#"^https://api\.github\.com/.*$"}}
   :google         {:patterns #{#"^https://www\.googleapis\.com/.*$"}}
   :stack-overflow {:patterns #{#"^http://api\.stackexchange\.com/.*$"}}
   :yahoo          {:patterns #{#"^http://query\.yahooapis\.com/.*$"}}
   :lebowski       {:patterns #{#"^http://lebowski\.me/.*$"}}
   :xkcd           {:patterns #{#"^http://xkcd\.com/.*$"}}
   :aws           {:patterns #{#"^https://.*?\.amazonaws\.com/.*$"}}
  }
```

```shell
lein build
```

---
Created by Atomist. Need Help? <a href="https://join.atomist.com/">Join our Slack team</a>
