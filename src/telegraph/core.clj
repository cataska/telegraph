(ns telegraph.core
  (:require [clj-http.client :as http]
            [cheshire.core :refer :all]))

(def api-url "https://api.telegra.ph")

(defn filter-nil-val [m]
  (into {} (filter (comp some? val) m)))

(defn create-account
  "Create a new Telegraph account"
  ([short-name]
   (create-account short-name nil nil))
  ([short-name author-name]
   (create-account short-name author-name nil))
  ([short-name author-name, author-url]
   (let [endpoint (str api-url "/createAccount")]
     (->
       (http/get endpoint
         {:query-params (filter-nil-val {:short_name short-name
                                         :author_name author-name
                                         :author_url author-url})})
       (:body)
       (parse-string true)))))

(defn str-or-nil
  [s]
  (if (nil? s)
    "nil"
    s))

(defn edit-account-info
  "Update information about a Telegraph account"
  [token {:keys [short-name author-name author-url]}]
  (let [endpoint (str api-url "/editAccountInfo")]
    (->
      (http/get endpoint
        {:query-params (filter-nil-val {:access_token token
                                        :short_name short-name
                                        :author_name author-name
                                        :author_url author-url})})
      (:body)
      (parse-string true))))

(defn revoke-access-token
  "Revoke access token and generate a new one"
  [token]
  (let [endpoint (str api-url "/revokeAccessToken")]
    (-> (http/get endpoint
                  {:query-params {:access_token token}})
        (:body)
        (parse-string true))))

(defn create-page
  "Create a new Telegraph page"
  [token title content {:keys [author-name author-url return-content]}]
  (let [endpoint (str api-url "/createPage")
        content (encode content)]
    (-> (http/get endpoint
                  {:query-params {:access_token token
                                  :title title
                                  :author_name author-name
                                  :author_url author-url
                                  :content content
                                  :return_content return-content}})
        (:body)
        (parse-string true))))
