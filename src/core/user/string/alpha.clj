(ns user.string.alpha
  (:require
   [clojure.string :as str]
   ))


;; * basic shim


;; * trim


(defn trim-char
  [^CharSequence s ^Character c]
  (let [len (.length s)]
    (loop [rindex len]
      (if (zero? rindex)
        ""
        (if (== (unchecked-int c) (unchecked-int (.charAt s (dec rindex))))
          (recur (unchecked-dec-int rindex))
          ;; there is at least one non-whitespace char in the string,
          ;; so no need to check for lindex reaching len.
          (loop [lindex 0]
            (if (== (unchecked-int c) (unchecked-int (.charAt s lindex)))
              (recur (unchecked-inc-int lindex))
              (.. s (subSequence lindex rindex) toString))))))))


(defn ^String triml-0x30
  "Removes whitespace from the left side of string."
  {:added "1.2"}
  [^CharSequence s]
  (let [len (.length s)]
    (if (== len 0)
      ""
      (loop [index 0]
        (if (== 0x30 (unchecked-int (.charAt s index)))
          (recur (unchecked-inc index))
          (.. s (subSequence index len) toString))))))


;; * fixtures


(def non-alpha-num-chars
  #{(char 0x21) (char 0x22) (char 0x23) (char 0x24) (char 0x25)
    (char 0x26) (char 0x27) (char 0x28) (char 0x29) (char 0x2a)
    (char 0x2b) (char 0x2c) (char 0x2d) (char 0x2e) (char 0x2f)
    (char 0x3a) (char 0x3b) (char 0x3c) (char 0x3d) (char 0x3e)
    (char 0x3f) (char 0x60) (char 0x40) (char 0x5b) (char 0x5c)
    (char 0x5d) (char 0x5e) (char 0x5f) (char 0x7b) (char 0x7c)
    (char 0x7d) (char 0x7e)})


;; * rand-str


(defn- alnum-char-vec
  [exclude]
  (into []
    (comp
     (map char)
     (remove #(contains? exclude %)))
    (range 0x21 0x7f)))


(defn- rand-size
  [lower upper]
  {:pre [(>= upper lower)]}
  (+ lower (rand-int (- upper lower))))


(defn rand-str
  ":include overrides :exclude"
  {:style/indent [0]}
  ([]
   (rand-str {}))
  ([{:keys
     [length
      include
      exclude
      shell-escape]
     :or
     {length  [32 40]
      include #{}
      exclude #{\` \\ \' \"}}}]
   {:pre [(vector? length)
          (set? include)]}
   (let [[lower upper] length
         upper         (if upper upper lower)
         size          (- (rand-size lower upper) (count include))
         exclude       (if shell-escape
                         (apply disj non-alpha-num-chars include)
                         exclude)
         char-vec      (alnum-char-vec exclude)
         chars         (shuffle
                        (into (vec include)
                          (repeatedly size #(rand-nth char-vec))))]
     (apply str chars))))


(comment
  (repeatedly 10 #(rand-nth alnum-char-vec))

  (transduce
    (map count)
    +
    0
    (repeatedly
     1000
     #(rand-str
        {:shell-escape true
         :length [10]
         :include #{\. \, \- \_}})))

  (apply == (repeatedly 9999 #(rand-size 10 10)))
  )
