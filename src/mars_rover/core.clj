(ns mars-rover.core
  (:use clojure.pprint)
  (:require [clojure.string :as str]))


;; domain

(defrecord Position [x y compass])
(defrecord Plateau [x y])

(defn validate-position [position, plateau]
  (let [in-plateau (and (<= 0 (:x position) (:x plateau)) (<= 0 (:y position) (:y plateau)))]
    (if in-plateau
      position
      (throw (RuntimeException. "You have reach the end of plateau.")))))

(defn move [position plateau]
  (let [new-position (case (:compass position)
                       "N" (Position. (:x position) (+ (:y position) 1) (:compass position))
                       "S" (Position. (:x position) (- (:y position) 1) (:compass position))
                       "E" (Position. (+ (:x position) 1) (:y position) (:compass position))
                       "W" (Position. (- (:x position) 1) (:y position) (:compass position)))]
    (validate-position new-position plateau)))

(defn rotate-left [position]
  (let [new-compass ((keyword (:compass position)) {:N "W" :S "E" :E "N" :W "S"})]
    (Position. (:x position) (:y position) new-compass)))

(defn rotate-right [position]
  (let [new-compass ((keyword (:compass position)) {:N "E" :S "W" :E "S" :W "N"})]
    (Position. (:x position) (:y position) new-compass)))

(defn move-or-rotate [position command plateau]
  (case command
    "M" (move position plateau)
    "L" (rotate-left position)
    "R" (rotate-right position)
    (throw (RuntimeException. (format "Command %s is invalid." command)))))

(defn rover-service [position commands plateau]
  (reduce #(move-or-rotate %1 %2 plateau) position commands))

; converters
(defn plateau-converter [args]
  (let [[x y] args]
    (Plateau. (Integer. x) (Integer. y))))

(defn position-converter [args]
  (let [[x y compass] args]
    (Position. (Integer. x) (Integer. y) compass)))

;; external
(defn parse-line [line]
  (cond
    (re-matches #"^\d\s\d$" line) (plateau-converter (str/split line #" "))
    (re-matches #"^\d\s\d\s[N|S|E|W]$" line) (position-converter (str/split line #" "))
    (re-matches #"^[L|R|M]+$" line) (str/split line #"")
    :else (throw (RuntimeException. (format "Unrecognized expression: %s." line)))))


(defn -main [& args]
  (try
    (map #(parse-line %) args)
    (catch Exception e (str "Caught exception: " (.getMessage e)))))
