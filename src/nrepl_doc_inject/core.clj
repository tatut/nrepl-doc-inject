(ns nrepl-doc-inject.core
  (:require [clojure.tools.nrepl.transport :as t]
            [clojure.java.io :as io])
  (:import (java.io FileNotFoundException)))


(def loaded-namespace-docs (atom {}))

(defn- load-namespace-docs! [ns-name]
  (let [ns-docs (try
                  (binding [*read-eval* false]
                    (with-open [ns-doc-in (io/reader (str "https://tatut.github.io/nrepl-doc-inject/" ns-name ".edn"))]

                      (read-string (slurp ns-doc-in))))
                  (catch FileNotFoundException fnfe
                    (println "No improved docs found for ns: " ns-name ". Maybe contribute some!")
                    ::not-found))]
    (swap! loaded-namespace-docs assoc ns-name ns-docs)
    ns-docs))

(defn- docs-for [ns-name symbol-name]
  (let [ns-docs (if (contains? @loaded-namespace-docs ns-name)
                  (get @loaded-namespace-docs ns-name)
                  (load-namespace-docs! ns-name))]
    (get ns-docs symbol-name)))


(defn doc-middleware [handler]
  (fn [msg]
    (if (= "info" (:op msg))
      ;; Give some "arround" advice to "info" message handling
      (let [reply (volatile! nil)]
        (handler (assoc msg
                        :transport (reify t/Transport
                                     (send [this msg]
                                       (vreset! reply msg)))))
        (t/send (:transport msg)
                (let [{:strs [ns name] :as r} @reply]
                  ;; Merge our improved docs
                  (merge r
                         (when (and ns name)
                           (docs-for ns name))))))

      ;; If this is not an info request, pass it along as is
      (handler msg))))

(comment

  ;; The request
  {:op "info"
   :ns "some.project.ns"
   :symbol "assoc"}

  ;; The reply
  {"ns" "clojure.core"
   "name" "assoc"
   "arglists-str" "[map key val]\n[map key val & kvs]"
   "doc" "assoc[iate]. When applied to a map, returns a new map of the\n    same (hashed/sorted) type, that contains the mapping of key(s) to\n    val(s). When applied to a vector, returns a new vector that\n    contains val at index. Note - index must be <= (count vector)."

   ;; don't care about the rest
   "resource" "clojure/core.clj"
   "static" "true"
   "added" "1.0"
   :status #{:done}
   "line" 181,
   :id "13"
   "column" 1
   "file" "jar:file:/Users/tatuta/.m2/repository/org/clojure/clojure/1.9.0-alpha19/clojure-1.9.0-alpha19.jar!/clojure/core.clj"
   "see-also" ("clojure.core/assoc-in" "clojure.core/dissoc" "clojure.core/merge")
   :session "230e0365-36f6-41c6-9471-25b8206af066"}

  ;; The plan:
  ;; - fetch EDN file for namespace, like.

  )
