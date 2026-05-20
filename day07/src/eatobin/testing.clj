(ns eatobin.testing)

(def user-map-1 {:input 0 :output 0})
(def user-map-2 {:input 0 :output 0})
(def user-map-3 {:input 0 :output 0})
(def triple {1 user-map-1 2 user-map-2 3 user-map-3})

;; (let [user {:first "Jane" :last "Doe" :age 30}]
;;   (let [{:keys [first last]} user]
;;     (str first " " last)))
;;
;; (defn make-user [user-map]
;;   (let [{:keys [first last]} user-map]
;;     (str first " " last)))
;;
;; (defn ccc [{:keys [first last]}]
;;   (str first " " last))

;(defn increment-output [user-map]
;  (update user-map :output inc))

(defn increment-specific-output [index target-map input]
  (assoc-in target-map [index :output] (inc input)))

(increment-specific-output 3 triple 11)
(increment-specific-output 1 triple 99)


(defn increment-next-specific-output [index target-map]
  (assoc-in target-map [index :output] (inc (get-in target-map [(- index 1) :input]))))

(increment-next-specific-output 2 triple)


(defn increment-specific-output-and-input [index target-map input]
  (->
   (assoc-in target-map [index :output] (inc input))
   (assoc-in [(inc (mod index 3)) :input] (inc input))))

(defn increment-specific-output-and-input-2 [index target-map input]
  (assoc-in target-map [(inc (mod index 3)) :input] (inc input)))

(increment-specific-output-and-input 3 triple 11)

;(increment-output user-map-1)
;
;(defn till-3 [user-map]
;  (if (= (:output user-map) 3)
;    user-map
;    (recur
;     (increment-output user-map))))

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
;
;(three triple)

;(three triple)
;=> {1 {:input 1, :output 3}, 2 {:input 0, :output 3}, 3 {:input 0, :output 3}}

(defn three-1 [triple]
  (let [index   1
        current (get triple index)]
    (loop [index   index
           current current]
      (if (= (:output current) 3)
        current
        (recur
         index (update current :output inc))))))

(three-1 triple)
