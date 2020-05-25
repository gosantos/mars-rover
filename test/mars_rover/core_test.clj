(ns mars-rover.core-test
  (:require [clojure.test :refer :all]
            [mars-rover.core :refer :all]
            [clojure.string :as str])
  (:import (mars_rover.core Position Plateau)))



(def plateau (Plateau. 5 5))

(deftest validate-new-position-test
  (testing "should throw an exception when the movement is invalid"
    (is (thrown? RuntimeException (validate-position (Position. 5 6 "N") plateau)))
    (is (thrown? RuntimeException (validate-position (Position. 5 -1 "S") plateau)))
    (is (thrown? RuntimeException (validate-position (Position. 6 -1 "E") plateau)))
    (is (thrown? RuntimeException (validate-position (Position. -2 0 "W") plateau)))))

(deftest move-test
  (testing "it should move the rover to north"
    (is (= (Position. 1 3 "N") (move (Position. 1 2 "N") plateau))))
  (testing "it should move the rover to south"
    (is (= (Position. 1 1 "S") (move (Position. 1 2 "S") plateau))))
  (testing "it should move the rover to east"
    (is (= (Position. 2 2 "E") (move (Position. 1 2 "E") plateau))))
  (testing "it should move the rover to west"
    (is (= (Position. 0 2 "W") (move (Position. 1 2 "W") plateau)))))

(deftest rotate-test
  (testing "it should rotate the rover to north"
    (is (= (Position. 1 2 "W") (rotate-left (Position. 1 2 "N")))))
  (testing "it should rotate the rover to south"
    (is (= (Position. 1 2 "E") (rotate-right (Position. 1 2 "N")))))
  (testing "it should rotate the rover to east"
    (is (= (Position. 1 2 "S") (rotate-left (Position. 1 2 "W")))))
  (testing "it should rotate the rover to west"
    (is (= (Position. 1 2 "S") (rotate-right (Position. 1 2 "E"))))))

(deftest move-or-rotate-test
  (testing "it should move"
    (is (= (Position. 1 3 "N") (move-or-rotate (Position. 1 2 "N") "M" plateau))))
  (testing "it should rotate left"
    (is (= (Position. 1 2 "W") (move-or-rotate (Position. 1 2 "N") "L" plateau))))
  (testing "it should rotate right"
    (is (= (Position. 1 2 "N") (move-or-rotate (Position. 1 2 "W") "R" plateau))))
  (testing "it should thrown an exception when the movement is invalid"
    (is (thrown? RuntimeException (move-or-rotate (Position. 1 2 "E") "F" plateau)))))

(deftest rover-run-service-test
  (testing "given a list of commands and a initial position it should move the rover the correct position"
    (is (= (Position. 1 3 "N") (rover-service (Position. 1 2 "N") (list "L" "M" "L" "M" "L" "M" "L" "M" "M") plateau))))
  (testing "given a list of commands and a initial position it should move the rover the correct position 2"
    (is (= (Position. 5 1 "E") (rover-service (Position. 3 3 "E") (list "M" "M" "R" "M" "M" "R" "M" "R" "R" "M") plateau)))))

(deftest plateau-converter-test
  (testing "should convert to a plateau"
    (is (= (Plateau. 5 5) (plateau-converter ["5" "5"])))))

(deftest position-converter-test
  (testing "should convert to a position"
    (is (= (Position. 1 2 "N") (position-converter ["1" "2" "N"])))))

(deftest parser-test
  (testing "it should parse a plateau"
    (is (= (Plateau. 5 5) (parse-line "5 5"))))
  (testing "it should parse a position"
    (is (= (Position. 1 2 "N") (parse-line "1 2 N"))))
  (testing "it should parse commands"
    (is (= ["L" "M" "L" "M"] (parse-line "LMLM"))))
  (testing "it should raise an exception when the format is wrong"
    (is (thrown? RuntimeException (parse-line "DHUSAHUDSAHUDHUAS")))))

(deftest main-test
  (testing "it should parse a plateau"
    (is (= [(Plateau. 5 5)
            (Position. 1 2 "N")
            ["L" "M" "L" "M" "L" "M" "L" "M" "M"]
            (Position. 3 3 "E")
            ["M" "M" "R" "M" "M" "R" "M" "R" "R" "M"]]
           (-main "5 5" "1 2 N" "LMLMLMLMM" "3 3 E" "MMRMMRMRRM"))))
  )