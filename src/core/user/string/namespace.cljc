(ns user.string.namespace)


(defn- str-length
  [s]
  #?(:clj
     (.length ^String s)
     :cljs
     (.-length ^string s)))


(defn-
  ^{:tag #?(:clj "[Ljava.lang.String;"
            :cljs array)}
  str-split
  [s regex]
  #?(:clj
     (.split ^java.util.regex.Pattern regex ^String s)
     :cljs
     (.split ^string s regex)))


(defn shorten-ns-str
  "Abbreviate package name

  https://github.com/qos-ch/logback/blob/master/logback-classic/src/main/java/ch/qos/logback/classic/pattern/TargetLengthBasedClassNameAbbreviator.java"
  [ns-str threshold]
  (let [length (str-length ns-str)]
    (if (< threshold length)
      (let [splitted (str-split ns-str #"\.")
            endi     (dec (alength splitted))
            lengthy? (volatile! false)]
        (areduce splitted i ret ""
          (let [j      (- endi i)
                comp   (aget splitted j)
                length (+ (str-length ret) (str-length comp) 1)
                comp   (cond
                         @lengthy?            (subs comp 0 1)
                         (== i 0)             comp
                         (< threshold length) (do
                                                (vreset! lengthy? true)
                                                (subs comp 0 1))
                         :else                comp)]
            (str (when-not (== j 0) ".") comp ret))))
      ns-str)))
