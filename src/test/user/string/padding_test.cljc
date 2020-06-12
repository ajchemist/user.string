(ns user.string.padding-test
  (:require
   [clojure.test :as test :refer [deftest is are]]
   [user.string.padding :refer
    #?(:clj :all
       :cljs [paddingr to-fixed fixed-padding x48-padding])]
   #?(:clj
      [taoensso.encore :as encore])
   )
  #?(:clj
     (:import
      java.text.DecimalFormat
      )))

(deftest main
  (is (= (paddingr \space 8 "ESM16") "ESM16   "))
  (is (= (to-fixed 1700 2) "1700.00"))
  (is (= (fixed-padding \0 10 2 "1700") "0001700.00")))

#?(:clj
   (deftest clojure-test
     (comment
       (def deprecated-padding
         (fn
           [char size ^String s]
           (let [pad (apply str (repeat size char))]
             (subs (str pad s) (. s length)))))

       (encore/qb
        [3 5e5]
        (padding \space 16 "ESM16")
        (deprecated-padding \space 16 "ESM16"))
       )
     ))

(comment
  (time (dotimes [n 1000000] (p/to-fixed 1700 2)))

  ;; format version
  (format (format "%%.%df" fixed) (float n))

  ;; cached
  #(:clj
    (defn ^:private decimal-format-case [fixed]
      (case fixed
        0 (DecimalFormat. "0.0")
        1 (DecimalFormat. "0.0")
        2 (DecimalFormat. "0.00")
        3 (DecimalFormat. "0.000")
        4 (DecimalFormat. "0.0000")
        5 (DecimalFormat. "0.00000")
        6 (DecimalFormat. "0.000000")
        7 (DecimalFormat. "0.0000000")
        8 (DecimalFormat. "0.00000000")
        9 (DecimalFormat. "0.000000000")
        (DecimalFormat.
         (str "0." (apply str (repeat fixed \0)))))))
  )
