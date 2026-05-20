(ns eatobin.testing)

(def user-map-1 {:input 1 :output 11})
(def user-map-2 {:input 2 :output 22})
(def user-map-3 {:input 3 :output 33})
(def triple {1 user-map-1 2 user-map-2 3 user-map-3})

(defn grab-my-input-from-prior-output [index target-map]
  (if (= index 1)
    (assoc-in target-map [index :input] (get-in target-map [3 :output]))
    (assoc-in target-map [index :input] (get-in target-map [(dec index) :output]))))

(defn increment-my-output-from-my-input [index target-map]
  (assoc-in target-map [index :output] (inc (get-in target-map [index :input]))))

(->>
 triple
 (grab-my-input-from-prior-output 1)
 (increment-my-output-from-my-input 1)
 (grab-my-input-from-prior-output 2)
 (increment-my-output-from-my-input 2)
 (grab-my-input-from-prior-output 3)
 (increment-my-output-from-my-input 3))
;=> {1 {:input 33, :output 34}, 2 {:input 34, :output 35}, 3 {:input 35, :output 36}}

(->>
 triple
 (grab-my-input-from-prior-output 2)
 (increment-my-output-from-my-input 2)
 (grab-my-input-from-prior-output 3)
 (increment-my-output-from-my-input 3)
 (grab-my-input-from-prior-output 1)
 (increment-my-output-from-my-input 1))
;=> {1 {:input 13, :output 14}, 2 {:input 11, :output 12}, 3 {:input 12, :output 13}}

(->>
 triple
 (grab-my-input-from-prior-output 3)
 (increment-my-output-from-my-input 3)
 (grab-my-input-from-prior-output 1)
 (increment-my-output-from-my-input 1)
 (grab-my-input-from-prior-output 2)
 (increment-my-output-from-my-input 2))
;=> {1 {:input 23, :output 24}, 2 {:input 24, :output 25}, 3 {:input 22, :output 23}}



;(defn increment-next-specific-output [index target-map]
;  (assoc-in target-map [index :output] (inc (get-in target-map [(- index 1) :input]))))
;
;(increment-next-specific-output 2 triple)
;
;
;(defn increment-specific-output-and-input [index target-map input]
;  (->
;   (assoc-in target-map [index :output] (inc input))
;   (assoc-in [(inc (mod index 3)) :input] (inc input))))
;
;(defn increment-specific-output-and-input-2 [index target-map input]
;  (assoc-in target-map [(inc (mod index 3)) :input] (inc input)))
;
;(increment-specific-output-and-input 1 triple 1)






;(increment-output user-map-1)

;(defn till-3 [index target-map]
;  (if (= (get-in target-map [3 :output]) 3)
;    target-map
;    (recur
;     6 (increment-specific-output-and-input index tr) 8)))

;(defn till-3 [target-map]
;  (if (= (get-in target-map [3 :output]) 3)
;    target-map
;    (loop [index          1
;           new-target-map (increment-my-output-from-my-input index target-map)]
;      (recur
;       (inc (mod index 3)) new-target-map))))

;(till-3 user-map-1)

;(defn three [triple]
;  (loop [index  1
;         triple triple]
;    (if (= (get-in triple [index :output]) 3)
;      triple
;      (recur
;       index (update-in triple [index :output] inc)))))

;(defn three [triple]
;  (loop [index  1
;         triple triple]
;    (if (= (get-in triple [3 :output]) 9)
;      triple
;      (recur
;       (inc index)
;       (loop [index  index
;              triple triple]
;         (if (= (get-in triple [index :output]) 3)
;           triple
;           (recur
;            index (update-in triple [index :output] inc))))))))

;(three triple)

;(three triple)
;=> {1 {:input 1, :output 3}, 2 {:input 0, :output 3}, 3 {:input 0, :output 3}}

;(defn three-1 [triple]
;  (let [index   1
;        current (get triple index)]
;    (loop [index   index
;           current current]
;      (if (= (:output current) 3)
;        current
;        (recur
;         index (update current :output inc))))))
;
;(three-1 triple)
