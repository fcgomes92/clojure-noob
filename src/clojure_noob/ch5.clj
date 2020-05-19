(ns clojure-noob.ch5)

(def character
  {:name       "Smooches McCutes"
   :attributes {:intelligence 10
                :strength     4
                :dexterity    5}})

(def c-int (comp :intelligence :attributes))

(c-int character)

(def c-str (comp :strength :attributes))

(c-str character)

(def c-dex (comp :dexterity :attributes))

(c-dex character)

(def spell-slots-comp (comp int inc #(/ % 2) c-int))
(spell-slots-comp character)

(defn my-comp
  ([] identity)
  ([f] f)
  ([f & fs]
   (fn [& args]
     (f (apply (apply my-comp fs) args)))))

;  ((my-comp int inc #(/ % 2) c-int) character)
;  (int & [inc divide c-int]) -> (int (apply (apply my-comp [inc divide c-int]) character))
;  (inc & [divide c-int]) -> (int (apply (apply my-comp [divide c-int]) character))
;  (divide & [c-int]) -> (divide (apply c-int character))
;  (c-int & []) -> (c-int)

(defn sum
  ([vals] (sum vals 0))
  ([vals accumulating-total]
   (if (empty? vals)
     accumulating-total
     (sum (rest vals) (+ (first vals) accumulating-total)))))
(sum [1 2 3])

(defn sum-2
  ([vals] (sum-2 vals 0))
  ([vals accumulating-total] (if (empty? vals)
                               accumulating-total
                               (recur (rest vals) (+ (first vals) accumulating-total)))))
(sum-2 [1 2 3])


((comp inc *) 2 3)