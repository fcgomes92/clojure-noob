(ns clojure-noob.core
  (:gen-class))

(defn -main
  "I don't> do a whole lot ... yet."
  [& args]
  (println "I'm a teapot!"))



(defn multi-arity
    ;; 2-arity
  ([target, move]
   (str "I " move " " target))
  ([target]
   (multi-arity target "attack"))
  ([]
   (multi-arity "you" "attack")))

(defn calling-everyone-communication
  [person]
  (str "Calling " person))

(defn calling-everyone
  ([& people]
   (map calling-everyone-communication people)))

(defn calling-everyone-tog
  [& people]
  (str "Calling: " (clojure.string/join ", " people)))

(defn chooser
  ([l]
   (chooser "anony" l))
  ([name [fst-choice snd-choice & rest]]
   (println (str "Hello, " name))
   (println (str "First choice: " fst-choice))
   (println (str "Second choice: " snd-choice))
   (println (str "The rest: " (clojure.string/join ", " rest)))))

(map (fn [name] (str "Hi, mate " name "!"))
     ["Mate", "Mati", "Barney"])

((fn [x] (* x 3)) 8)

(defn inc-maker
  "Create a custom incrementor"
  [inc-by]
  #(+ % inc-by))

(def inc3 (inc-maker 3))

(inc3 10)

(def asym-hobbit-body-parts [{:name "head" :size 3}
                             {:name "left-eye" :size 1}
                             {:name "left-ear" :size 1}
                             {:name "mouth" :size 1}
                             {:name "nose" :size 1}
                             {:name "neck" :size 2}
                             {:name "left-shoulder" :size 3}
                             {:name "left-upper-arm" :size 3}
                             {:name "chest" :size 10}
                             {:name "back" :size 10}
                             {:name "left-forearm" :size 3}
                             {:name "abdomen" :size 6}
                             {:name "left-kidney" :size 1}
                             {:name "left-hand" :size 2}
                             {:name "left-knee" :size 2}
                             {:name "left-thigh" :size 4}
                             {:name "left-lower-leg" :size 3}
                             {:name "left-achilles" :size 1}
                             {:name "left-foot" :size 2}])

(defn symmetrize-body-parts
  "Expects a seq of maps that have a :name and a :size"
  [asym-body-parts]
  (loop [remaining-asym-parts asym-body-parts final-body-parts []]
    (if (empty? remaining-asym-parts)
      final-body-parts
      (let [[part & remaining] remaining-asym-parts]
        (recur remaining
               (into final-body-parts
                     (set [part (matching-part part)])))))))


(defn matching-part
  [part]
  {:name (clojure.string/replace (:name part) #"^left-" "right-")
   :size (:size part)})

(defn match-hobbit-parts [acc part]
  (into acc
        (set [part (matching-part part)])))

(defn symmetrize-body-parts-reduce
  [asym-body-parts]
  (reduce match-hobbit-parts [] asym-body-parts))

(loop [iteration 0]
  (println (str "Ieration: " iteration))
  (if (> iteration 3)
    (println "Goodbye")
    (recur (inc iteration))))

(defn recursive-printer
  ([]
   (recursive-printer 0))
  ([iteration]
   (println iteration)
   (if (> iteration 3)
     (println "Goodbye")
     (recursive-printer (inc iteration)))))

(defn hit
  [asym-body-parts]
  (let [sym-parts (symmetrize-body-parts-reduce asym-body-parts)
        body-part-size-sum (reduce + 0 (map :size sym-parts))
        target (rand body-part-size-sum)]
    (loop [[part & remaining] sym-parts
           accumulated-size (:size part)]
      (if (> accumulated-size target)
        {:accumulated-size accumulated-size
         :target target
         :part part}
        (recur remaining (+ accumulated-size (:size (first remaining))))))))
(hit asym-hobbit-body-parts)

; Exercicies

(defn add100 [value] (+ 100 value))

(defn dec-maker
  [val]
  (fn [ini]
    (- ini val)))

(defn mapset
  [fun values]
  (loop [[val & res] (set values)
         fn-rst (fun val)
         rst []]
    (if (empty? res)
      (set (conj rst fn-rst))
      (recur res (fun (first res)) (conj rst fn-rst)))))


(defn matching-part
  [part]
  {:name (clojure.string/replace (:name part) #"^left-" "right-")
   :size (:size part)})
?
(defn match-hobbit-parts [acc part]
  (into acc
        (set [part (matching-part part)])))

(defn symmetrize-body-parts-reduce
  [asym-body-parts]
  (reduce match-hobbit-parts [] asym-body-parts))