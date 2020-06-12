(ns user.string.lettercase
  {:doc "https://en.wikipedia.org/wiki/Letter_case"}
  (:require
   [clojure.string :as str]
   #?(:clj [user.string.clojure.patch :refer [fast-memoize]])
   ))


(def ^:private -memoize
  #?(:clj  fast-memoize
     :cljs cljs.core/memoize))


(defn split-on-hyphen "Splits a string on hyphens."
  [s] (str/split s #"-"))


(defn split-on-underscore "Splits a string on underscore."
  [s] (str/split s #"_"))


(defn replace-all [^String s regex replacement]
  #?(:clj  (. s (replaceAll (str regex) replacement))
     :cljs (. s (replace (js/RegExp. (str regex) "g") replacement))))


(defn separate-camel-humps
  "Splits a camel case string into tokens. Consecutive captial lets,
  except for the last one, become a single token."
  [s]
  (-> s
    (replace-all "([A-Z]+)([A-Z][a-z])" "$1-$2")
    (replace-all "([a-z\\d])([A-Z])"    "$1-$2")
    (split-on-hyphen)))


(def camel->dash     (-memoize (fn [s] (str/join "-" (separate-camel-humps s)))))
(def camel->dash->lc (-memoize (fn [s] (str/lower-case (camel->dash s)))))
(def camel->dash->uc (-memoize (fn [s] (str/upper-case (camel->dash s)))))
(def camel->underscore     (-memoize (fn [s] (str/join "_" (separate-camel-humps s)))))
(def camel->underscore->lc (-memoize (fn [s] (str/lower-case (camel->underscore s)))))
(def camel->underscore->uc (-memoize (fn [s] (str/upper-case (camel->underscore s)))))


(def dash->camel
  (-memoize
   (fn [s]
     (->> s
       (split-on-hyphen)
       (map str/capitalize)
       (apply str)))))


(def dash->camel2
  (-memoize
   (fn [s]
     (let [[f & next] (split-on-hyphen s)]
       (apply str f (map str/capitalize next))))))


(def underscore->camel
  (-memoize
   (fn [s]
     (->> s
       (split-on-underscore)
       (map str/capitalize)
       (apply str)))))


(def underscore->camel2
  (-memoize
   (fn [s]
     (let [[f & next] (split-on-underscore s)]
       (apply str f (map str/capitalize next))))))


(def camel->kebab     camel->dash)
(def camel->kebab->lc camel->dash->lc)
(def camel->kebab->uc camel->dash->uc)
(def camel->snake     camel->underscore)
(def camel->snake->lc camel->underscore->lc)
(def camel->snake->uc camel->underscore->uc)
(def kebab->camel     dash->camel)
(def kebab->camel2    dash->camel2)
(def snake->camel     underscore->camel)
(def snake->camel2    underscore->camel2)
