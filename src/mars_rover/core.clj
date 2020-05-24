(ns mars-rover.core
  (:require [clojure.string :as str]))


;; domain
(def plateau {:x 5 :y 5})

(defrecord Position [x y compass])
(defrecord Plateau [x y])

(defn validate-position [position]
  (let [in-plateau (and (<= 0 (:x position) (:x plateau)) (<= 0 (:y position) (:y plateau)))]
    (if in-plateau
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

; converters
(defn plateau-converter [args]
  (let [[x y] args]
    (Plateau. (Integer. x) (Integer. y))))

(defn position-converter [args]
  (let [[x y compass] args]
    (Position. (Integer. x) (Integer. y) compass)))

(defn commands-converter [args] args)

;
;(defn parse [string] (case
;                       [x y] (Plateau. x y)
;                             [x y compass]
;                             ))

