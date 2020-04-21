(ns clojure-noob.fwpd)

(def filename "suspects.csv")

(def vamp-keys [:name :glitter-index])

(defn str->int
  [str]
  (Integer. str))

(def conversions
  {:name          identity
   :glitter-index str->int})

(defn parse
  "Convert a CSV into a rows of columns"
  [string]
  (map #(clojure.string/split % #",")
       (clojure.string/split string #"\n")))

(defn convert
  [vamp-key value]
  ((get conversions vamp-key) value))

(defn mapify
  [[headers & rows]]
  (map
    (fn [unmapped-row]
      (reduce
        (fn [row-map [vamp-key value]]
          (assoc row-map vamp-key (convert vamp-key value)))
        {}
        (map vector vamp-keys unmapped-row)))
    rows))

(defn glitter-filter
  [minimum-glitter records]
  (filter #(>= (:glitter-index %) minimum-glitter) records))

; (fwpd/glitter-filter 3 (fwpd/mapify (fwpd/parse (slurp fwpd/filename))))

(defn glitter-filter-names
  [minimum-glitter records]
  (map #(:name %) (glitter-filter minimum-glitter records)))

; (fwpd/glitter-filter-names 3 (fwpd/mapify (fwpd/parse (slurp fwpd/filename))))

(def vamp-database (mapify (parse (slurp filename))))

(defn append
  "Append a lists of items into to the database
    Expected a list of item in the format: {:name name :glitter-index index}"
  [db & items]
  (let [add-to-db (partial conj db)]
    (apply add-to-db items)))

; (fwpd/append fwpd/vamp-database {:name "Cullen" :glitter-index 10} {:name "Cullen 2" :glitter-index 12})

(def input-validator
  {:name          (partial string?)
   :glitter-index (partial int?)})

(defn validate-input
  [validation-keys record]
  (let [validation-list (map (fn [[el check?]] (check? (el record))) validation-keys)]
    (reduce (fn [acc record] (and acc record)) true validation-list)))

(defn append-and-validate
  "Append a lists of items into to the database
          Expected a list of item in the format: {:name name :glitter-index index}"
  [db & items]
  (reduce
    (fn [acc item]
      (if (validate-input input-validator item)
        (conj acc item)
        acc))
    db items))

(defn map-hash-to-stirng-of-values
  [headers el]
  (clojure.string/join "," (map (fn [header-key] (str (header-key el))) (seq headers))))

(defn to-csv
  [items headers]
  (str (clojure.string/join "," headers) "\n"
       (clojure.string/join "\n" (map (partial map-hash-to-stirng-of-values headers) items))))

(defn write-to-file
  [filename data]
  (with-open [w (clojure.java.io/writer filename)]
    (.write w data)))