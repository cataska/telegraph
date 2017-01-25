(ns telegraph.core-test
  (:require [clojure.test :refer :all]
            [telegraph.core :refer :all]))

(deftest test-create-account-result-should-be-true
  (testing "Create an account result should be true"
    (let [result (:ok (create-account "Sandbox" "Anonymous"))]
      (is (= result true)))))

(deftest test-edit-account-info-result-should-be-true
  (testing "Edit account info result should be true"
    (let [token (get-in (create-account "Sandbox" "Anonymous") [:result :access_token])
          result (:ok (edit-account-info token {:short-name "Sandbox"
                                                :author-name "Anonymous"}))]
      (is (= result true)))))

(deftest test-revoke-access-token-result-should-be-true
  (testing "Revoke access toke result should be true"
    (let [token (get-in (create-account "Sandbox" "Anonymous") [:result :access_token])
          result (:ok (revoke-access-token token))]
      (is (= result true)))))
