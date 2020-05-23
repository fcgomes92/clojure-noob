(ns pegthing.core
  (:require [clojure.set :as set])
  (:gen-class))

(declare successful-move prompt-move game-over prompt-rows)


(defn tri*
  "Generates lazy sequence aof triangular numbers"
  ([] (tri* 0 1))
  ([sum n]
   (let [new-sum (+ sum n)]
     (cons new-sum (lazy-seq (tri* new-sum (inc n)))))))

(def tri (tri*))

(defn triangular?
  "Is the number triangular? In the tri* seq"
  [n]
  (= n (last (take-while #(>= n %) tri))))

(defn row-tri
  "Take the number at the end of a row n"
  [n]
  (last (take n tri)))

(defn row-num
  "Returns row number the position belog to: pos 1 in row 1"
  [pos]
  (inc (count (take-while #(> pos %) tri))))

(defn connect
  "Form a mutual connection between two positions"
  [board max-pos pos neighbor destination]
  (if (<= destination max-pos)
    (reduce (fn [new-board [p1 p2]]
              (assoc-in new-board [p1 :connections p2] neighbor))
            board
            [[pos destination] [destination pos]])
    board))

(defn connect-right
  [board max-pos pos]
  (let [neighbor (inc pos)
        destination (inc neighbor)]
    (if-not (or (triangular? neighbor) (triangular? pos))
      (connect board max-pos pos neighbor destination)
      board)))

(defn connect-donw-left
  [board max-pos pos]
  (let [row (row-num pos)
        neighbor (+ row pos)
        destination (+ 1 row neighbor)]
    (connect board max-pos pos neighbor destination)))

(defn connect-donw-right
  [board max-pos pos]
  (let [row (row-num pos)
        neighbor (+ 1 row pos)
        destination (+ 2 row neighbor)]
    (connect board max-pos pos neighbor destination)))

(defn add-pos
  "Pegs the position and performs connections"
  [board max-pos pos]
  (let [pegged-board (assoc-in board [pos :pegged] true)]
    (reduce (fn [new-board connection-creation-fn]
              (connection-creation-fn new-board max-pos pos))
            pegged-board
            [connect-right connect-donw-left connect-donw-right])))

(defn new-board
  "Creates a new board w/ the given number of rows"
  [rows]
  (let [initial-board {:rows rows}
        max-pos (row-tri rows)]
    (reduce (fn [board pos] (add-pos board max-pos pos))
            initial-board
            (range 1 (inc max-pos)))))

(defn pegged?
  "Does the position have a peg in it?"
  [board pos]
  (get-in board [pos :pegged]))

(defn remove-peg
  "Take the peg at a given position out of the board"
  [board pos]
  (assoc-in board [pos :pegged] false))

(defn place-peg
  "Put a peg in the board at a given position"
  [board pos]
  (assoc-in board [pos :pegged] true))

(defn move-peg
  "Take peg out of p1 and place it in p2"
  [board p1 p2]
  (place-peg (remove-peg board p1) p2))


(defn valid-moves
  "Returns a map of all valid moves for pos, where they key is the
  destination and the value is the jumped position"
  [board pos]
  (into {}
        (filter (fn [[destination jumped]]
                  (and (not (pegged? board destination))
                       (pegged? board jumped)))
                (get-in board [pos :connections]))))

(defn valid-move?
  "Return jumped position if the move from p1 to p2 is valid, nil otherwise"
  [board p1 p2]
  (get (valid-moves board p1) p2))

(defn make-move
  "Move peg from p1 to p2, removing jumped peg"
  [board p1 p2]
  (if-let [jumped (valid-move? board p1 p2)]
    (move-peg (remove-peg board jumped) p1 p2)))

(defn can-move?
  "Do any of the pegged positions have valid moves?"
  [board]
  (some (comp not-empty (partial valid-moves board))
        (map first (filter #(get (second %) :pegged) board))))

;;  Rendering and Printing the Board

(defn letters-needed
  "Get how many letters is goind to be needed for the game"
  [total-lines]
  (last (take total-lines tri)))


(def alpha-start 97)
(def alpha-end 123)
(def all-letters (map (comp str char) (range alpha-start alpha-end)))
(def total-letters (count all-letters))

(defn get-letter-by-index
  [index]
  (if (>= index total-letters)
    (let [new-index (mod index total-letters)
          letter (nth all-letters new-index)
          letter-sufix (quot index total-letters)]
      (str letter letter-sufix))
    (nth all-letters index)))

(defn letters*
  "Creates a lazy function to get an array of letters needed for the game"
  ([] (letters* 0))
  ([index]
   (let [l (get-letter-by-index index)]
     (cons l (lazy-seq (letters* (inc index)))))))

(def letters (letters*))

(defn list-letters
  [total-lines]
  (let [needs (letters-needed total-lines)]
    (take needs letters)))

(def pos-chars 4)

(def ansi-styles
  {:red "[31m"
   :green "[32m"
   :blue "[34m"
   :reset "[0m"})

(defn ansi
  [style]
  (str \u001b (style ansi-styles)))

(defn colorize
  [text color]
  (str (ansi color) text (ansi :reset)))

(defn render-place
  [board pos]
  (str (nth (list-letters (:rows board)) (dec pos))
       (if (get-in board [pos :pegged])
         (colorize "+" :blue)
         (colorize "-" :red))))

(defn render-pos
  [board pos]
  (let [place (render-place board pos)
        size (- (count place) 8)]
    (str "[" (apply str (take (- 5 size) (repeat " "))) place "]")))

(defn row-positions
  "Return all positions in the given row"
  [row-num]
  (range (inc (or (row-tri (dec row-num)) 0))
         (inc (row-tri row-num))))

(defn row-padding
  "String opf spaces to add to the beginning of a row to center it"
  [row-num rows]
  (let [pad-length (/ (* (- rows row-num) pos-chars) 2)]
    (apply str (take pad-length (repeat " ")))))

(defn render-row
  [board row-num]
  (str (row-padding row-num (:rows board))
       (clojure.string/join " " (map (partial render-pos board)
                                     (row-positions row-num)))))

(defn print-board
  [board]
  (doseq [row-num (range 1 (inc (:rows board)))]
    (println (render-row board row-num))))

;; Player interaction

;; TODO: fix this function in case where we have letters and numbers
(defn get-letter-muiltipl
  [letter]
  (let [multipl (clojure.string/join (rest letter))]
    (if (= multipl "")
      0
      (Integer/parseInt multipl))))
(defn letter->pos
  "converts a letter string to hte corresponding position number"
  [letter]
  (inc (+ (- (int (first letter)) alpha-start) (* total-letters (get-letter-muiltipl letter)))))

(defn get-input
  "Waits for user to enter text and hit enter, then cleans the input"
  ([] (get-input nil))
  ([default]
   (let [input (clojure.string/trim (read-line))]
     (if (empty? input)
       default
       (clojure.string/lower-case input)))))


(defn characters-as-strings
  "Given a string, return a collection consisting of each indivisual character"
  [string]
  (re-seq #"\w\d?" string))

(defn user-entered-invalid-move
  "Handles the next step after a user has entered an invalid move"
  [board]
  (println "\n!!! That was an invalid move :(\n")
  (prompt-move board))

(defn user-entered-valid-move
  "Handles the next step after a user has entered a valid move"
  [board]
  (if (can-move? board)
    (prompt-move board)
    (game-over board)))


(defn prompt-move
  [board]
  (println "\n Here's your board:")
  (print-board board)
  (println "Move from where to where? Enter two letters:")
  (let [input (map letter->pos  (characters-as-strings (get-input)))]
    (if-let [new-board (make-move board (first input) (second input))]
      (user-entered-valid-move new-board)
      (user-entered-invalid-move board))))

(defn game-over
  "Announce the game is over and prompt to play again"
  [board]
  (let [remaining-pegs (count (filter :pegged (vals board)))]
    (println "Game over! You had" remaining-pegs "pegs left:")
    (print-board board)
    (println "Play again? y/n [y]")
    (let [input (get-input "y")]
      (if (= "y" input)
        (prompt-rows)
        (do
          (println "Bye!")
          (System/exit 0))))))

(defn prompt-empty-peg
  [board]
  (println "Here's your board:")
  (print-board board)
  (println "Remove which peg? [e]")
  (prompt-move (remove-peg board (letter->pos (get-input "e")))))



(defn prompt-rows
  []
  (println "How many rows? [5]")
  (let [rows (Integer. (get-input 5))
        board (new-board rows)]
    (if (<= rows 15)
      (prompt-empty-peg board)
      (do
        (println "Max rows: 15")
        (prompt-rows)))))
