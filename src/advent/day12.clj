(ns advent.day12)

;part a

;(def state
;  [{:pos [+3 -6 +6] :vel [0 0 0]}
;   {:pos [+10 +7 -9] :vel [0 0 0]}
;   {:pos [-3 -7 +9] :vel [0 0 0]}
;   {:pos [-8 0 +4] :vel [0 0 0]}])

(def state
  [{:pos [-1 0 2] :vel [0 0 0]}
   {:pos [2 -10 -7] :vel [0 0 0]}
   {:pos [4 -8 8] :vel [0 0 0]}
   {:pos [3 5 -1] :vel [0 0 0]}])

(defn add [a b] (mapv + a b))

(defn step [state]
  (doall
    (for [m state
          :let [new-velocities
                (reduce
                  add
                  (:vel m)
                  (for [n state] (mapv compare (:pos n) (:pos m))))]]
      (-> m
          (assoc :vel new-velocities)
          (update :pos add new-velocities)))))

(identity (nth (iterate step state) 10))

(defn norm [xs] (transduce (map #(Math/abs (double %))) + xs))

(defn total [moon] (* (norm (:vel moon)) (norm (:pos moon))))

;(defn calc-pe []
;  (doseq [[_ {name :name, {x-pos :x, y-pos :y, z-pos :z} :pos}] @moon-meld]
;    (swap! moon-meld assoc-in [name :pot :x] (math/abs x-pos))
;    (swap! moon-meld assoc-in [name :pot :y] (math/abs y-pos))
;    (swap! moon-meld assoc-in [name :pot :z] (math/abs z-pos))))
;
;(defn calc-ke []
;  (doseq [[_ {name :name, {x-vel :x, y-vel :y, z-vel :z} :vel}] @moon-meld]
;    (swap! moon-meld assoc-in [name :kin :x] (math/abs x-vel))
;    (swap! moon-meld assoc-in [name :kin :y] (math/abs y-vel))
;    (swap! moon-meld assoc-in [name :kin :z] (math/abs z-vel))))
;
;(defn tot-pot []
;  (doseq [[_ {name :name, {x-pot :x, y-pot :y, z-pot :z} :pot}] @moon-meld]
;    (swap! moon-meld assoc-in [name :tot-pot] (+ x-pot y-pot z-pot))))
;
;(defn tot-kin []
;  (doseq [[_ {name :name, {x-kin :x, y-kin :y, z-kin :z} :kin}] @moon-meld]
;    (swap! moon-meld assoc-in [name :tot-kin] (+ x-kin y-kin z-kin))))
;
;(defn tot-tot []
;  (doseq [[_ {name :name, tot-pot :tot-pot, tot-kin :tot-kin}] @moon-meld]
;    (swap! moon-meld assoc-in [name :tot-tot] (* tot-pot tot-kin))))
;
;(def one-thousand-step-vec
;  (vec
;    (flatten
;      (take 1000
;            (repeat
;              (interleave [apply-gravity] [apply-velocity] [calc-pe] [calc-ke] [tot-pot] [tot-kin] [tot-tot]))))))
;
;(defn one-thousand-step []
;  (doall (map #(%) one-thousand-step-vec)))
;
;(defn calc-answer []
;  (doseq [[_ {tot-tot :tot-tot}] @moon-meld]
;    (swap! answer + tot-tot)))
;
;(defn step-n-calc []
;  (doall (map #(%) [one-thousand-step calc-answer])))
;
;(step-n-calc)
;
;(println @answer)

;;6849

;part b
;Your puzzle answer was 356658899375688.
