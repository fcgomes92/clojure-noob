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

