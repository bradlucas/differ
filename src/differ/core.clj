(ns differ.core
  (:gen-class)
  (:require [clojure-csv.core :as csv]
            [clojure.java.io :as io]
            [clojure.string :as string])   ;; [lower-case replace]]
  (:import [java.io StringReader])
  (:use [clojure.pprint]))
        

(defn get-lines [fname]
  (with-open [r (io/reader fname)]
    (doall (line-seq r))))

(defn build-keyword
  [s]
  (keyword (string/replace (string/lower-case s) " " "_")))
    
(defn zipmap-keywords
  "Returns a map with the keys mapped to the corresponding vals.
https://github.com/clojure/clojure/blob/master/src/clj/clojure/core.clj#L2296
BWL 09-20-2011 Modified to turn the keys into keywords which are lowercase with no spaces
"
  {:added "1.0"
   :static true}
  [keys vals]
    (loop [map {}
           ks (seq keys)
           vs (seq vals)]
      (if (and ks vs)
        (recur (assoc map (build-keyword (first ks)) (first vs))
               (next ks)
               (next vs))
        map)))

(defn load-csv
  [file-name]
  (let [rows (csv/parse-csv (slurp file-name))
        header (first rows)
        data (rest rows)]
    (map #(zipmap-keywords header %) data)))


(defn find-new-members
  "Return the new people are only in the new-list but not in the current-list.
Both lists are lists of maps where they have at least :first_name and :last_name keys.
"
  [current-list new-list]
  (let [existing (set (remove #(nil? (second %)) (map (juxt :first_name :last_name) current-list)))]
    (remove #(existing ((juxt :first_name :last_name) %)) new-list)))


(defn get-new-entries
  [prev-file new-file]
  (find-new-members (load-csv prev-file) (load-csv new-file)))

(defn -main [& args]
  (doseq [e (get-new-entries (first args) (second args))]
    (clojure.pprint/pprint e)))
