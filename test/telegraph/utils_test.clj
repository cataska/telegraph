(ns telegraph.utils-test
  (:require [clojure.test :refer :all]
            [telegraph.utils :refer :all]))

(deftest test-html-to-nodes-result-should-be-nodes
  (testing "Test html to nodes"
    (let [html "<p>Hello world!</p>"]
      (is (= (html-to-nodes html)
             [{:tag "p" :children ["Hello world!"]}])))))

(deftest test-two-paragraph-one-achor-html-to-nodes
  (testing "Test two paragraph and on anchor html to nodes"
    (let [html "<p>Hello, world!</p><p><a href=\"https://telegra.ph/\">Test link</a></p>"]
      (is (= (html-to-nodes html)
             [{:tag "p" :children ["Hello, world!"]}
              {:tag "p" :children [{:tag      "a"
                                    :attrs    {:href "https://telegra.ph/"}
                                    :children ["Test link"]}]}])))))

(deftest test-invalid-tag-should-throw-exception
  (testing "Test invalid tag should throw exception"
    (let [html "<script src=\"localhost\"></script>"]
      (is (thrown? UnsupportedOperationException (html-to-nodes html))))))

(deftest test-figure-img
  (testing "Test figure and img"
    (let [html "<figure><img src=\"/file/6a5b15e7eb4d7329ca7af.jpg\"/></figure>"]
      (is (= (html-to-nodes html)
             [{:tag "figure" :children [{:tag "img" :attrs {:src "/file/6a5b15e7eb4d7329ca7af.jpg"}}]}])))))
