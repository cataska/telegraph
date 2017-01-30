(ns telegraph.core-test
  (:require [clojure.test :refer :all]
            [telegraph.core :refer :all]
            [telegraph.utils :refer :all]))

(deftest test-create-account-result-should-be-true
  (testing "Create an account result should be true"
    (let [result (create-account "Sandbox" "Anonymous")]
      (is (= (:ok result) true)))))

(deftest test-edit-account-info-result-should-be-true
  (testing "Edit account info result should be true"
    (let [token (get-in (create-account "Sandbox" "Anonymous") [:result :access_token])
          result (edit-account-info token {:short-name  "Sandbox"
                                           :author-name "Anonymous"})]
      (is (= (:ok result) true)))))

(deftest test-revoke-access-token-result-should-be-true
  (testing "Revoke access token result should be true"
    (let [token (get-in (create-account "Sandbox" "Anonymous") [:result :access_token])
          result (revoke-access-token token)]
      (is (= (:ok result) true)))))

(deftest test-create-page-result-should-be-true
  (testing "Create a new page result should be true"
    (let [token (get-in (create-account "Sandbox" "Anonymous") [:result :access_token])
          nodes (html-to-nodes "<p>Hello world!</p>")
          result (create-page token "Sample Page" nodes {:author-name "Anonymous"})]
      (is (= (:ok result) true)))))

(deftest test-get-page-result-should-be-true
  (testing "Get a page result should be true"
    (let [result (get-page "Sample-Page-12-15" true)]
      (is (= (:ok result) true)))))
