(ns advent.day2)

(defn int-code [ic]
  (loop [offset 0
         ic ic]
    (let [ops (ic (+ 0 offset))]
      (case ops
        99 ic
        1 (recur
            (+ 4 offset)
            (assoc ic (ic (+ 3 offset)) (+ (ic (ic (+ 1 offset))) (ic (ic (+ 2 offset))))))
        2 (recur
            (+ 4 offset)
            (assoc ic (ic (+ 3 offset)) (* (ic (ic (+ 1 offset))) (ic (ic (+ 2 offset))))))))))
