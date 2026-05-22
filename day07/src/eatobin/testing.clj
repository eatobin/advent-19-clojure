(ns eatobin.testing)

(def user-map-1 {:input         0
                 :output        0
                 :phase         0
                 :pointer       0
                 :relative-base 0
                 :memory        0
                 :stopped?      false
                 :recur?        true})
(def user-map-2 {:input         0
                 :output        0
                 :phase         0
                 :pointer       0
                 :relative-base 0
                 :memory        0
                 :stopped?      false
                 :recur?        true})
(def user-map-3 {:input         0
                 :output        0
                 :phase         0
                 :pointer       0
                 :relative-base 0
                 :memory        0
                 :stopped?      false
                 :recur?        true})
(def user-map-4 {:input         0
                 :output        0
                 :phase         0
                 :pointer       0
                 :relative-base 0
                 :memory        0
                 :stopped?      false
                 :recur?        true})
(def user-map-5 {:input         0
                 :output        60
                 :phase         0
                 :pointer       0
                 :relative-base 0
                 :memory        0
                 :stopped?      false
                 :recur?        true})
(def triple {1 user-map-1 2 user-map-2 3 user-map-3 4 user-map-4 5 user-map-5})

(defn grab-my-input-from-prior-output [index target-map]
  (if (= index 1)
    (assoc-in target-map [index :input] (get-in target-map [5 :output]))
    (assoc-in target-map [index :input] (get-in target-map [(dec index) :output]))))

(defn increment-my-output-from-my-input [index target-map]
  (assoc-in target-map [index :output] (inc (get-in target-map [index :input]))))

(defn grab-and-increment [index target-map]
  (->>
   target-map
   (grab-my-input-from-prior-output index)
   (increment-my-output-from-my-input index)))

(defn map-of-five [triple]
  (loop [index  1
         triple triple]
    (if (= (get-in triple [5 :output]) 220)
      triple
      (recur
       (inc (mod index 5)) (grab-and-increment index triple)))))

(comment
  (map-of-five triple)
  :rcf)
