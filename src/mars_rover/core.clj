(ns mars-rover.core)

(def plateau {:x 5 :y 5})

(defrecord Position [x y compass])

(defn validate-position [position]
  (let [valid (and
                (<= (:x position) (:x plateau))
                (<= (:y position) (:y plateau))
                (>= (:x position) 0)
                (>= (:y position) 0)
                )]
    (if valid
      position
      (throw (RuntimeException. "You have reach the end of plateau.")))))

(defn move [position]
  (let [new-position (case (:compass position)
                       "N" (Position. (:x position) (+ (:y position) 1) (:compass position))
                       "S" (Position. (:x position) (- (:y position) 1) (:compass position))
                       "E" (Position. (+ (:x position) 1) (:y position) (:compass position))
                       "W" (Position. (- (:x position) 1) (:y position) (:compass position)))]
    (validate-position new-position)))

(defn rotate-left [position]
  (let [new-compass ((keyword (:compass position)) {:N "W" :S "E" :E "N" :W "S"})]
    (Position. (:x position) (:y position) new-compass)))

(defn rotate-right [position]
  (let [new-compass ((keyword (:compass position)) {:N "E" :S "W" :E "S" :W "N"})]
    (Position. (:x position) (:y position) new-compass)))

(defn move-or-rotate [position command]
  (case command
    "M" (move position)
    "L" (rotate-left position)
    "R" (rotate-right position)
    (throw (RuntimeException. (format "Command %s is invalid." command)))))

(defn rover-service [position commands]
  (reduce #(move-or-rotate %1 %2) position commands))