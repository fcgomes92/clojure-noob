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
   {:alias "Spider-Man" :real "Peter Parker"}])
(def identities-2
  [{:alias "Santa" :real "Your mom"}
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

(def food-journal
  [{:month 1 :day 1 :human 5.3 :critter 2.3}
   {:month 1 :day 2 :human 5.1 :critter 2.0}
   {:month 2 :day 1 :human 4.9 :critter 2.1}
   {:month 2 :day 2 :human 5.0 :critter 2.5}
   {:month 3 :day 1 :human 4.2 :critter 3.3}
   {:month 3 :day 2 :human 4.0 :critter 3.8}
   {:month 4 :day 1 :human 3.7 :critter 3.9}
   {:month 4 :day 2 :human 3.7 :critter 3.6}])

(take-while #(< (:month %) 3) food-journal)
(drop-while #(< (:month %) 3) food-journal)

(filter #(< (:human %) 5) food-journal)
(some #(> (:critter %) 5) food-journal)
(some #(and (> (:critter %) 3) %) food-journal)

(def vampire-database
  {0 {:makes-blood-puns? false, :has-pulse? true  :name "McFishwich"}
   1 {:makes-blood-puns? false, :has-pulse? true  :name "McMackson"}
   2 {:makes-blood-puns? true,  :has-pulse? false :name "Damon Salvatore"}
   3 {:makes-blood-puns? true,  :has-pulse? true  :name "Mickey Mouse"}})

(defn vampire-related-details
  [social-security-number]
  (Thread/sleep 1000)
  (get vampire-database social-security-number))

(defn vampire?
  [record]
  (and (:makes-blood-puns? record)
       (not (:has-pulse? record))
       record))

(defn identify-vampire
  [social-security-numbers]
  (first (filter vampire?
                 (map vampire-related-details social-security-numbers))))

(time (vampire-related-details 0))
(time (def mapped-details (map vampire-related-details (range 0 1000000))))
(time (first mapped-details))


(concat (take 8 (repeat "na")) ["Batman!"])
(take 3 (repeatedly (fn [] (rand-int 1000))))

(defn even-number-generator
    ([] (even-number-generator 0))
    ([n] (cons n (lazy-seq (even-number-generator (+ n 2))))))

(defn myrepeat
    ([n] (cons n (lazy-seq (myrepeat n)))))
(concat (take 8 (myrepeat "na")) ["Batman!"])

(take 10 (even-number-generator))
;; exercise: implement map, filter, some using reduce
; (defn map-reduce
;   ([f coll] (seq (reduce #(conj %1 (f %2)) [] coll)))
;   ([f coll & colls]
;    (let [colls (cons coll colls)]
;      (map-reduce (partial apply f)
;                  (partition (count colls)
;                             (apply interleave colls))))))
; (defn reorder
;     ([coll] (reduce #(cons %1 %2)))
;     ([& [fst & rst]] (cons (reorder rst)))

; (defn map-reduce
;     ([f coll] (seq (reduce #(conj %1 (f %2)) [] coll)))
;     ([f coll & colls]
;      (let [colls (cons coll colls)]
;        (map-reduce (partial apply f)
;                    (reorder coll colls)))))
