# Atomist 'rug-function-http'

[![Build Status](https://travis-ci.org/atomist/rug-function-http.svg?branch=master)](https://travis-ci.org/atomist/rug-function-http)
[![Slack Status](https://join.atomist.com/badge.svg)](https://join.atomist.com)

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
   :aws           {:patterns #{#"^https://.+?\.amazonaws\.com/.*$"}}
  }
```

## Support

General support questions should be discussed in the `#support`
channel on our community Slack team
at [atomist-community.slack.com][slack].

If you find a problem, please create an [issue][].

[issue]: https://github.com/atomist/rug-function-http/issues

## Building

```
$ boot build
```

## Releasing

To create a new release of the project, simply push a tag of the form
`M.N.P` where `M`, `N`, and `P` are integers that form the next
appropriate [semantic version][semver] for release.  For example:

[semver]: http://semver.org

```
$ git tag -a 1.2.3
```

The Travis CI build (see badge at the top of this page) will
automatically create a GitHub release using the tag name for the
release and the comment provided on the annotated tag as the contents
of the release notes.  It will also automatically upload the needed
artifacts.

---
Created by [Atomist][atomist].
Need Help?  [Join our Slack team][slack].

[atomist]: https://www.atomist.com/
[slack]: https://join.atomist.com/
