(ns mkapi.main
  (:require 
    [postal.core :as postal]
    [ring.adapter.jetty9 :as jetty]
    [unilog.config :as unilog]
    [mkapi.handler :refer [handler]]
    [mkapi.config :as config]))


(defn init-logging! 
  [config]
  (unilog/start-logging! (:logging config)))

(defn -main 
  [& args]
  (let [config (config/read-config :dev)]
    (init-logging! config)
    (jetty/run-jetty 
      handler 
      {:host (get-in config [:webserver :host]) 
       :port (get-in config [:webserver :port])})))

(-main)
