(ns telegraph.utils-test
  (:require [clojure.test :refer :all]
            [telegraph.utils :refer :all]))

(deftest test-html-to-nodes-result-should-be-nodes
  (testing "Test html to nodes"
    (let [html "<p>Hello world!</p>"]
      (is (= (html-to-nodes html)
             [{:tag "p" :children ["Hello world!"]}])))))

(deftest test-two-paragraph-one-achor-html
  (testing
    (let [html "<p>Hello, world!</p><p><a href=\"https://telegra.ph/\">Test link</a></p>"]
      (is (= (html-to-nodes html))
          [{:tag "p" :children ["Hello world!"]}
           {:tag "p" :children [{:tag "p"
                                 :children [{:tag "a"
                                             :attrs {:href "https://telegra.ph/"}
                                             :children ["Test link"]}]}]}]))))
