(ns mars-rover.core-test
  (:require [clojure.test :refer :all]
            [mars-rover.core :refer :all])
  (:import (mars_rover.core Position)))


(deftest move-test
  (testing "it should move the rover to north"
    (is (= (Position. 1 3 "N") (move (Position. 1 2 "N")))))
  (testing "it should move the rover to south"
    (is (= (Position. 1 1 "S") (move (Position. 1 2 "S")))))
  (testing "it should move the rover to east"
    (is (= (Position. 2 2 "E") (move (Position. 1 2 "E")))))
  (testing "it should move the rover to west"
    (is (= (Position. 0 2 "W") (move (Position. 1 2 "W")))))
  (testing "it should move the rover to bar"
    (is (= "Error. I dont know what is bar." (move (Position. 1 2 "bar")))))
  )


(deftest rotate-test
  (testing "it should rotate the rover to north"
    (is (= (Position. 1 2 "W") (rotate "L" (Position. 1 2 "N")))))
  (testing "it should rotate the rover to south"
    (is (= (Position. 1 2 "E") (rotate "R" (Position. 1 2 "N")))))
  (testing "it should rotate the rover to east"
    (is (= (Position. 1 2 "S") (rotate "L" (Position. 1 2 "W")))))
  (testing "it should rotate the rover to west"
    (is (= (Position. 1 2 "S") (rotate "R" (Position. 1 2 "E")))))
  )