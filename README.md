# rug-function-http

[![Build Status](https://travis-ci.org/atomist/rug-function-http.svg?branch=master)](https://travis-ci.org/atomist/rug-function-http)

HTTP Rug Function based on https://github.com/dakrone/clj-http

## Parameters

*   `url`: string - Used for all methods
*   `method`: string - One of `get`, `post`, `put`, `head` and `delete`
*   `config`: object(map) - JSON serializable datastructure representing the rest of the request
    * e.g. {body: "some string", headers: {content-type: "application/json"}} - as per https://github.com/dakrone/clj-http


```
lein build
```

---
Created by Atomist. Need Help? <a href="https://join.atomist.com/">Join our Slack team</a>
