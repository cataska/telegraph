(ns telegraph.core
  (:require [clj-http.client :as http]
            [cheshire.core :refer :all]))

(def ^:private api-url "https://api.telegra.ph")

(defn- filter-nil-val [m]
  (into {} (filter (comp some? val) m)))

(defn- retrieve-body-with-keyword
  [resp]
  (-> resp
      :body
      (parse-string true)))

(defn create-account
  "Create a new Telegraph account"
  ([short-name]
   (create-account short-name nil nil))
  ([short-name author-name]
   (create-account short-name author-name nil))
  ([short-name author-name, author-url]
   (let [endpoint (str api-url "/createAccount")]
     (-> (http/get endpoint
                   {:query-params (filter-nil-val {:short_name short-name
                                                   :author_name author-name
                                                   :author_url author-url})})
         retrieve-body-with-keyword))))

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
      retrieve-body-with-keyword)))

(defn revoke-access-token
  "Revoke access token and generate a new one"
  [token]
  (let [endpoint (str api-url "/revokeAccessToken")]
    (-> (http/get endpoint
                  {:query-params {:access_token token}})
        retrieve-body-with-keyword)))

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
        retrieve-body-with-keyword)))

(defn- get-page*
  [path return-content]
  (let [endpoint (str api-url "/getPage/" path)]
    (http/get
      endpoint
      (when-not (nil? return-content)
        {:query-params {:return_content return-content}}))))

(defn get-page
  "Get a Telegraph page"
  ([path]
    (get-page path nil))
  ([path return-content]
   (-> (get-page* path return-content)
       retrieve-body-with-keyword)))
