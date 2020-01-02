(ns advent.day4)

(<= 1 2 3 3)
;=> true
(<= 1 2 3 2)
;=> false
(<= 1 2 3 9)
;=> true

(loop [v [1 2 3]]
  (if (empty? v)
    false
    (if (= (first v) (second v))
      true
      (recur (rest v)))))
;=> false
(loop [v []]
  (if (empty? v)
    false
    (if (= (first v) (second v))
      true
      (recur (rest v)))))
;=> false
(loop [v [1 2 2]]
  (if (empty? v)
    false
    (if (= (first v) (second v))
      true
      (recur (rest v)))))
;=> true
(loop [v [1 2 1]]
  (if (empty? v)
    false
    (if (= (first v) (second v))
      true
      (recur (rest v)))))
;=> false
(loop [v [1 1 1]]
  (if (empty? v)
    false
    (if (= (first v) (second v))
      true
      (recur (rest v)))))
;=> true
(loop [v [1 3 6 2 8 4 7 7 3 0 8 1]]
  (if (empty? v)
    false
    (if (= (first v) (second v))
      true
      (recur (rest v)))))
;=> true

(def pass (range 234208 765870))

(for [n (str 123456)]
  (- (byte n) 48))
;=> (1 2 3 4 5 6)

(loop [v (for [n (str 1234566)]
           (- (byte n) 48))]
  (if (empty? v)
    false
    (if (= (first v) (second v))
      true
      (recur (rest v)))))
;=> true

(defn doubles? [candidate]
  (loop [v (for [n (str candidate)]
             (- (byte n) 48))]
    (if (empty? v)
      false
      (if (= (first v) (second v))
        true
        (recur (rest v))))))

(defn increasing? [candidate]
  (apply <= (vec (for [n (str candidate)]
                   (- (byte n) 48)))))
