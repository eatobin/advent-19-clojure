(ns advent-19-clojure.day01-test
  (:require [advent-19-clojure.day01 :as day01]
            [clojure.test :refer [deftest is testing]]
            [kaocha.repl :as k]))

((deftest self-test
   (testing "This just tests that tests are wired"
     (is (= 1 1)))))

(deftest gas-test
  (is (= 2
         (day01/gas 12)))
  (is (= 2
         (day01/gas 14)))
  (is (= 654
         (day01/gas 1969)))
  (is (= 33583
         (day01/gas 100756))))

(deftest gas-plus-test
  (is (= 2
         (day01/gas-plus 14)))
  (is (= 966
         (day01/gas-plus 1969)))
  (is (= 50346
         (day01/gas-plus 100756))))

(comment
  *ns*
  (k/run *ns*)
  (k/run-all)
  :rcf)
