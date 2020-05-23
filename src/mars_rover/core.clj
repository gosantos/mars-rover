(ns mars-rover.core)

(def plateau {:x 5 :y 5})

(defrecord Position [x y compass])

(defn move [position]
  (case (:compass position)
    "N" (Position. (:x position) (+ (:y position) 1) (:compass position))
    "S" (Position. (:x position) (- (:y position) 1) (:compass position))
    "E" (Position. (+ (:x position) 1) (:y position) (:compass position))
    "W" (Position. (- (:x position) 1) (:y position) (:compass position))
    (format "Error. I dont know what is %s." (:compass position))))


(def compass-after-rotation
  {:LN "W" :LS "E" :LE "N" :LW "S"
   :RN "E" :RS "W" :RE "S" :RW "N"})

(defn rotate [direction position]
  (let [new-compass (get compass-after-rotation (keyword (str direction (:compass position))))]
    (Position.
      (:x position)
      (:y position)
      new-compass)))
