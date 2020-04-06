(ns clojure-noob.ch4
  (:gen-class))

(defn titleize
  [topic]
  (str topic " for the Brave and True"))

(map titleize ["Hamsters" "Ragnarok"])

(map titleize '("Empathy" "Decorating"))

(map titleize #{"Elbows" "Soap Carving"})

(map #(titleize (second %)) {:uncomfortable-thing "Winking"
                             :comfortable-thing "Playing"})


;; seq is used to manage indirection (using Polymorphism) on
;; elements that should be treated as a sequence

(seq '(1 2 3))
(seq [1 2 3])
(seq #{"Lala" "Land"})
(seq {:info "Cats are cool" :confirm true})

(into {} (map #(vector (first %1) (+ 1 (second %1))) (seq {:a 1 :b 2 :c 3})))

(map str ["a" "b" "c"] ["A" "B" "C"])

(def human-consumption   [8.1 7.3 6.6 5.0])
(def critter-consumption [0.0 0.2 0.3 1.1])
(defn unify-diet-data
  [human critter]
  {:human human
   :critter critter})
(map unify-diet-data human-consumption critter-consumption)

(def sum #(reduce +  %))
(def avg #(/ (sum %) (count %)))
(defn stats
  [numbers]
  (map #(% numbers) [sum count avg]))

(stats [3 4 10])
(stats [80 1 44 13 6])

(def identities
  [{:alias "Batman" :real "Bruce Wayne"}
   {:alias "Spider-Man" :real "Peter Parker"}
   {:alias "Santa" :real "Your mom"}
   {:alias "Easter Bunny" :real "Your dad"}])

(map :real identities)

(reduce (fn [new-map [key val]]
          (assoc new-map key (inc val)))
        {}
        {:max 30 :min 10})


(reduce (fn [new-map [key val]]
          (if (> val 4)
            (assoc new-map key val)
            new-map))
        {}
        {:human 4.1
         :critter 3.9})

;; exercise: implement map, filter, some using reduce