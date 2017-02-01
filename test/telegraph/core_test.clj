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

(deftest test-get-page-result-should-be-true-and-has-return-content
  (testing "Get a page result should be true and has return content"
    (let [result (get-page "Sample-Page-12-15" true)]
      (is (and (= (:ok result) true)
               (some? (get-in result [:result :content])))))))

(deftest test-get-page-result-should-be-true-and-no-return-content
  (testing "Get a page result should be true and no return content"
    (let [result (get-page "Sample-Page-12-15")]
      (is (and (= (:ok result) true)
               (nil? (get-in result [:result :content])))))))

(deftest test-edit-page-result-should-be-true-and-has-return-content
  (testing "Edit a page result should be true and has return content"
    (let [token (get-in (create-account "Sandbox" "Anonymous") [:result :access_token])
          nodes (html-to-nodes "<p>Hello world!</p>")
          title "Sample Page"
          page-result (create-page token title nodes {:author-name "Anonymous"})
          path (get-in page-result [:result :path])
          new-nodes (html-to-nodes "<p>Hello,+world!</p>")
          result (edit-page token path title new-nodes {:return-content true})]
      (is (and (= (:ok result) true)
               (some? (get-in result [:result :content])))))))

(deftest test-get-account-info-result-should-be-true-and-name-should-be-same
  (testing "Get account info result should be true and name should be same"
    (let [short-name "Sandbox"
          author-name "Anonymous"
          token (get-in (create-account short-name author-name) [:result :access_token])
          result (get-account-info token :short-name :author-name)]
      (is (and (= (:ok result) true)
               (= (get-in result [:result :short_name]) short-name)
               (= (get-in result [:result :author_name]) author-name))))))
