(ns telegraph.core-test
  (:require [clojure.test :refer :all]
            [telegraph.core :refer :all]))

(deftest create-account-test
  (testing "Create account"
    (let [result (:ok (create-account "Sandbox" "Anonymous"))]
      (is (= result true)))))
