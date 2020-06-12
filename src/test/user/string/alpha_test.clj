(ns user.string.alpha-test
  (:require
   [clojure.test :as test :refer [deftest is are testing]]
   [clojure.string :as str]
   [taoensso.encore :as encore]
   [user.string.alpha :refer :all]
   ))

(def strings-0x30-padded
  ["000000000011"
   "000000000300"
   "000000000600"
   "000000001914"
   "000000000027"
   "000000000010"
   "000000000031"
   "000000000140"
   "000000000020"
   "000000000088"
   "000000000040"
   "000000000328"
   "000000000040"
   "000000000500"
   "000000000800"
   "000000000050"
   "000000000055"
   "000000000300"
   "000000000010"
   "000000000005"
   "000000000015"
   "000000000012"
   "000000000028"
   "000000000005"
   "000000000061"
   "000000000080"
   "000000000310"
   "000000000428"
   "000000000010"
   "000000000206"])

(deftest main
  (is
   (==
    (hash (map triml-0x30 strings-0x30-padded))
    (hash '("11" "300" "600" "1914" "27" "10" "31" "140" "20" "88"
            "40" "328" "40" "500" "800" "50" "55" "300" "10" "5"
            "15" "12" "28" "5" "61" "80" "310" "428" "10" "206"))))

  (is
   (empty?
    (filter #(contains? non-alpha-num-chars %)
            (rand-str {:exclude non-alpha-num-chars}))))
  )

(comment

  (encore/qb
   [4 3e4]
   (doseq [s strings-0x30-padded]
     (triml-0x30 s))
   (doseq [s strings-0x30-padded]
     (str (Integer/parseInt s))))

  ;; [255.38 340.74]

  )
