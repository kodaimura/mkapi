(ns mkapi.handler
  (:require 
    [reitit.ring :as ring]
    [mkapi.util :as util]
    [mkapi.middleware :as middleware]))


(defn get-test
  [req]
  {:status 200 :body "test"})

(defn post-inquiry
  [req]
  (let [params (:params req)]
    (util/send-inquiry-mail params)
    (util/send-inquiry-confirm-mail params)
    (try (util/insert-inquiry! params)
      {:status 200}
      (catch Exception _
        {:status 500}))))


(def api-routes
  (ring/ring-handler
    (ring/router
      ["/api"
        ["" {:get get-test}]
        ["/inquiry" {:post post-inquiry}]
      ])))

(def handler
  (middleware/wrap-base api-routes))
