(ns telegraph.utils-test
  (:require [clojure.test :refer :all]
            [telegraph.utils :refer :all]))

(deftest test-html-to-nodes-result-should-be-nodes
  (testing "Test html to nodes"
    ((let [html "<p>Hello world!</p>"]
       (is (= (html-to-nodes html)
              [{:tag "p" :children "Hello world!"}]))))))
