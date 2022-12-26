(ns env)

(def env
  {:mail-server-host (System/getenv "MAIL_SERVER_HOST")
   :mail-server-password (System/getenv "MAIL_SERVER_PASSWORD")})

;check env
(defn check-env 
  [envhm]
  (loop [ks (keys envhm)]
    (cond 
      (empty? ks) true
      (nil? (get envhm (first ks))) 
        (throw (Exception. (str "error: required env " (first ks))))
      :else (recur (rest ks)))))

