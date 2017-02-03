(ns telegraph.utils
  (:require [pl.danieljanus.tagsoup :as tag]))

(def ALLOWED-TAGS
  [:a :aside :b :blockquote :br
   :code :em :figcaption :figure :h3
   :h4 :hr :i :iframe :img
   :li :ol :p :pre :s
   :strong :u :ul :video])

(defn- key-in? [coll key]
  (some #(= % key) (flatten coll)))

(defn- parsed-html [html]
  (let [parsed (tag/parse-string html)]
    (if (key-in? parsed :body)
      parsed
      [:html {} [:body {} parsed]])))

(defn- parsed-body [html]
  (first (tag/children (parsed-html html))))

(defn- parsed-body-children [html]
  (tag/children (parsed-body html)))

(declare to-node)

(defn- assoc-children [m children]
  (cond
    (vector? children) (assoc m :children (conj [] (to-node children)))
    (nil? children) m
    :else (assoc m :children (conj [] children))))

(defn- assoc-attrs [m attrs]
  (if (or (contains? attrs :src)
          (contains? attrs :href))
    (assoc m :attrs (dissoc attrs :shape))
    m))

(defn- to-node
  [[tag attrs children]]
  (if (some (conj #{} tag) ALLOWED-TAGS)
    (-> {:tag (name tag)}
        (assoc-children children)
        (assoc-attrs attrs))
    (throw (UnsupportedOperationException.))))

;(defn- to-node
;  [[tag attrs children]]
;  (if (some (conj #{} tag) ALLOWED-TAGS)
;    (if (vector? children)
;     (assoc-attrs {:tag (name tag) :children (conj [] (to-node children))} attrs)
;     (assoc-attrs {:tag (name tag) :children (conj [] children)} attrs))
;    (throw (UnsupportedOperationException.))))

(defn html-to-nodes
  [html]
  (let [html-elements (parsed-body-children html)]
    (loop [elements html-elements
           nodes []]
      (if (empty? elements)
        nodes
        (recur (rest elements) (conj nodes (to-node (first elements))))))))
