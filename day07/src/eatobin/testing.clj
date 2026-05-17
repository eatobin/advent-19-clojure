(ns eatobin.testing)

(def user-map-1 {:message "one" :count 0})
(def user-map-2 {:message "two" :count 0})
(def pair {1 user-map-1 2 user-map-2})

(let [user {:first "Jane" :last "Doe" :age 30}]
  (let [{:keys [first last]} user]
    (str first " " last)))

(defn make-user [user-map]
  (let [{:keys [first last]} user-map]
    (str first " " last)))

(defn ccc [{:keys [first last]}]
  (str first " " last))

(defn increment-count [user-map]
  (update user-map :count inc))

(println (increment-count user-map-1))

(defn till-3 [user-map]
  (if (= (:count user-map) 3)
    user-map
    (recur
     (increment-count user-map))))

(till-3 user-map-1)

(defn both [index pair]
  (if (= (get-in pair [index :count]) 3)
    pair
    (recur
     index (update-in pair [index :count] inc))))

(both 1 pair)
