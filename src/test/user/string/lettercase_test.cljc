(ns user.string.lettercase-test
  (:require
   [clojure.test :as test :refer [deftest is are]]
   [user.string.lettercase :as letter]
   ))

(deftest main
  #?(:clj
     (is
      (and
       (re-matches #".*memoize.*" (str letter/kebab->camel))
       (re-matches #".*memoize.*" (str letter/snake->camel))
       (re-matches #".*memoize.*" (str letter/camel->dash))
       (re-matches #".*memoize.*" (str letter/camel->snake)))))
  (are [x y] (= x y)
    "ThisIsAKebab"    (letter/kebab->camel "this-is-a-kebab")
    "thisIsAKebab"    (letter/kebab->camel2 "this-is-a-kebab")
    "font"            (letter/kebab->camel2 "font")
    "this-is-a-kebab" (letter/camel->kebab->lc "ThisIsAKebab")
    "this-is-a-kebab" (letter/camel->kebab->lc "thisIsAKebab")))
