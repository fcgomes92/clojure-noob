(ns clojure-noob.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
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