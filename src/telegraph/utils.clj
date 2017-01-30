(ns telegraph.utils
  (:require [clojure.xml :as xml]
            [clojure.zip :as zip]
            [cheshire.core :as c]
            [pl.danieljanus.tagsoup :as t]
            [hickory.core :as h]
            [net.cgrand.enlive-html :as html]))

(def ALLOWED-TAGS
  {:a :aside :b :blockquote :br
   :code :em :figcaption :figure :h3
   :h4 :hr :i :iframe :img
   :li :ol :p :pre :s
   :strong :u :ul :video})

(defn- generate-nodes [xml-nodes]
  (loop [nodes xml-nodes
         result []]
    (if (empty? nodes)
      result
      (let [node (first xml-nodes)
            {tag :tag children :content} node]
        (println node tag)
        (recur (rest nodes) (conj result {"tag" (name tag) "children" children}))))))

(def HTML-STR "<p>Hello, world!</p><p><a href=\"https://telegra.ph/\">Test link</a></p>")

(defn to-java-iputstream
  [h]
  (java.io.ByteArrayInputStream. (.getBytes h "UTF-8")))

(xml/parse (to-java-iputstream HTML-STR))

(def parsed-html-elements-tagsoup
  (nthrest ((t/parse-string HTML-STR) 2) 2))

(def parsed-html-elements-hickory
  (:content ((:content
      (first (:content (h/as-hickory (h/parse HTML-STR)))))
     1)))

(for [node parsed-html-elements-hickory]
  (println node))

(def html-node (t/parse-string HTML-STR))
(t/children html-node)
(t/tag html-node)
(t/attributes html-node)
(def body-node (first (t/children html-node)))
(t/children body-node)

(defn fast-forward [loc]
  (if (zip/end? loc)
    (zip/node loc)
    (recur (zip/next loc))))

(def zzz (-> html-node zip/vector-zip))
(-> zzz zip/down zip/node)
(-> zzz zip/down zip/right zip/right)
(-> zzz zip/down zip/down zip/node)
(-> zzz zip/down zip/right zip/node)

(t/parse-string HTML-STR)
(t/children (t/parse-string HTML-STR))
(def aaa (t/children (first (t/children (t/parse-string HTML-STR)))))
(t/tag (first (t/children aaa)))
(flatten (t/parse-string HTML-STR))
(h/as-hiccup (h/parse HTML-STR))

(h/as-hickory (h/parse HTML-STR))

(html/html-resource (to-java-iputstream HTML-STR))

(defn to-input-stream
  [s]
  (java.io.ByteArrayInputStream. (.getBytes s "UTF-8")))

(defn- parsed-html [html]
  (t/parse-string html))

(defn- parsed-body [html]
  (first (t/children (parsed-html html))))

(defn- parsed-body-children [html]
  (t/children (parsed-body html)))

;(defn- to-node [[tag attrs children]]
;  (if (empty? attrs)
;    {:tag (name tag) :children children}
;    {:tag (name tag) :attrs attrs :children children}))

;(defn- to-node [[tag attrs children]]
;  (let [common {:tag (name tag) :children (conj [] children)}]
;    (if (or (contains? attrs :src)
;            (contains? attrs :href))
;      (assoc common :attrs attrs)
;      common)))

(defn assoc-attrs [m attrs]
  (if (or (contains? attrs :src)
          (contains? attrs :href))
    (assoc m :attrs attrs)
    m))

(defn to-node
  [[tag attrs children]]
  (if (vector? children)
    (assoc-attrs {:tag (name tag) :children (conj [] (to-node children))} attrs)
    (assoc-attrs {:tag (name tag) :children (conj [] children)} attrs)))

;(defn- build-nodes
;  [html]
;  (let [xml-nodes (xml/parse (java.io.ByteArrayInputStream. (.getBytes html)))]
;    (generate-nodes (conj [] (xml-nodes)))))

(defn- build-nodes
  [html]
  (let [html-elements (parsed-body-children html)]
    (loop [elements html-elements
           nodes []]
      (if (empty? elements)
        nodes
        (recur (rest elements) (conj nodes (to-node (first elements))))))))

(build-nodes "<p>Hello world</p>")
(build-nodes HTML-STR)
(build-nodes "<a href=\"https://telegra.ph\">Test Link&lt;/a&gt;</a>")

(defn html-to-nodes [html]
  (throw (UnsupportedOperationException.)))
