(ns advent.day01-test
  (:require [advent.day01 :as day01]
            [clojure.test :as t]))

(t/deftest gas-test
  (t/is (= 2
           (day01/gas 12)))
  (t/is (= 2
           (day01/gas 14)))
  (t/is (= 654
           (day01/gas 1969)))
  (t/is (= 33583
           (day01/gas 100756))))

(t/deftest gas-plus-test
  (t/is (= 2
           (day01/gas-plus 14)))
  (t/is (= 966
           (day01/gas-plus 1969)))
  (t/is (= 50346
           (day01/gas-plus 100756))))
