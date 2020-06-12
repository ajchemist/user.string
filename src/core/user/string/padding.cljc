(ns user.string.padding
  #?(:clj
     (:import
      java.text.DecimalFormat
      )))

(defn- parse-int
  [x]
  #?(:clj
     (when-let [^String x (re-find #"\d+" x)]
       (Integer/parseInt x))
     :cljs
     (js/parseInt x)))

(defn to-fixed
  [n fixed]
  #?(:clj
     (-> (apply str "0." (repeat fixed \0))
       (DecimalFormat.)
       (.format n))
     :cljs
     (.toFixed n fixed)))

(defn- strlen
  [s]
  #?(:clj  (.length ^String s)
     :cljs (.-length s)))

(defn- fixed-size-pad
  [char size]
  #?(:clj
     (let [^StringBuilder sb (StringBuilder.)]
       (dotimes [_ size]
         (. sb append char))
       (. sb toString))

     :cljs
     (apply str (repeat size char))))

(defn padding
  ([size s]
   (padding \space size s))
  ([char size s]
   (subs (str (fixed-size-pad char size) s) (strlen s))))

(def x48-padding
  "zero char `0' padding"
  (partial padding \0))

(defn paddingr
  ([size s]
   (paddingr \space size s))
  ([char size s]
   (subs (str s (fixed-size-pad char size)) 0 size)))

(defn fixed-padding
  ([size fixed s]
   (fixed-padding \space size fixed s))
  ([char size fixed s]
   (let [i   (parse-int s)
         s   (to-fixed i fixed)]
     (subs (str (fixed-size-pad char size) s) (strlen s)))))

(comment

  (time
   (dotimes [n 1000000]
     (paddingr \space 16 "ESM16")))

  (def paddingr-memo (memoize (partial paddingr \space 16)))

  (time
   (dotimes [n 1000000]
     (paddingr-memo "ESM16")))

  (.toPrecision this size)

  (format (format "%%.%df" 2) (float 38.045))
  (format (format "%%.%df" 2) 38.045)

  )
