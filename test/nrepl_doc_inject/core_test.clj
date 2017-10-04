(ns nrepl-doc-inject.core-test
  (:require [nrepl-doc-inject.core :as sut]
            [clojure.test :as t :refer [deftest is]]
            [clojure.java.io :as io]))

(deftest all-files-are-valid
  (binding [*read-eval* false]
    (let [files (-> "docs" java.io.File. .listFiles seq)]
      (doseq [f files]
        (try
          (let [ns-docs (read-string (slurp (io/reader f)))]
            (is (map? ns-docs) "ns docs is a map")
            (is (every? string? (keys ns-docs)) "all keys are strings (symbol names)")
            (is (every? map? (vals ns-docs)) "all values are maps"))
          (catch Exception e
            (is false
                (str "Unable to read " f ": " e))))))))
