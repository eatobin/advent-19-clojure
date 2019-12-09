(ns advent.day2)

(defn int-code [ic]
  (loop [offset 0
         ic ic]
    (let [ops (ic (+ 0 offset))]
      (if (= ops 99)
        ic
        (let [a-pos (ic (+ 1 offset))
              b-pos (ic (+ 2 offset))
              place-pos (ic (+ 3 offset))]
          (if (= ops 1)
            (assoc ic place-pos (+ (ic a-pos) (ic b-pos)))
            (assoc ic place-pos (* (ic a-pos) (ic b-pos))))
          (recur (+ 4 offset)
                 ic))))))
