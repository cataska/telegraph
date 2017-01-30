(ns telegraph.utils
  (:require [pl.danieljanus.tagsoup :as tag]))

(def ALLOWED-TAGS
  {:a :aside :b :blockquote :br
   :code :em :figcaption :figure :h3
   :h4 :hr :i :iframe :img
   :li :ol :p :pre :s
   :strong :u :ul :video})

(defn- parsed-html [html]
  (tag/parse-string html))

(defn- parsed-body [html]
  (first (tag/children (parsed-html html))))

(defn- parsed-body-children [html]
  (tag/children (parsed-body html)))

(defn- assoc-attrs [m attrs]
  (if (or (contains? attrs :src)
          (contains? attrs :href))
    (assoc m :attrs (dissoc attrs :shape))
    m))

(defn- to-node
  [[tag attrs children]]
  (if (vector? children)
    (assoc-attrs {:tag (name tag) :children (conj [] (to-node children))} attrs)
    (assoc-attrs {:tag (name tag) :children (conj [] children)} attrs)))

(defn html-to-nodes
  [html]
  (let [html-elements (parsed-body-children html)]
    (loop [elements html-elements
           nodes []]
      (if (empty? elements)
        nodes
        (recur (rest elements) (conj nodes (to-node (first elements))))))))
