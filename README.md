# nrepl-doc-inject

[![CircleCI](https://circleci.com/gh/tatut/nrepl-doc-inject.svg?style=svg)](https://circleci.com/gh/tatut/nrepl-doc-inject) ![PR welcome](https://img.shields.io/badge/pull%20requests-welcome-green.svg)

My stab at improving Clojure docstrings. NREPL middleware to load community
docstrings and inject them.

# How to use

* Clone this and `lein install` it (not on clojars yet)
* Add dependency to project dev or user profile `[nrepl-doc-inject "0.1-SNAPSHOT"]`
* Add middleware to :repl-options / :nrepl-middleware

```clojure
(defproject your-project "version"
  ;; deps etc
  :profiles {:dev {:dependencies [[nrepl-doc-inject "0.1-SNAPSHOT"]]}}
  :repl-options {:nrepl-middleware [nrepl-doc-inject.core/doc-middleware]})
```

![use in cider](https://raw.githubusercontent.com/tatut/nrepl-doc-inject/master/images/reify.png)


# How to contribute

All namespace vars are in the same file under `docs` folder with the `.edn` suffix.

Add new documentation for functions or totally new namespaces.

Send PRs.
