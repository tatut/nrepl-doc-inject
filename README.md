# nrepl-doc-inject
My stab at improving Clojure docstrings

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

# How to contribute

All namespace vars are in the same file under `docs` folder with the `.edn` suffix.

Add new documentation for functions or totally new namespaces.

Send PRs.
