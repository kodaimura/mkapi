(ns mkapi.handler
  (:require 
    [reitit.ring :as ring]
    [mkapi.db :refer [db sql-query]]
    [mkapi.middleware :as middleware]))


(defn get-test
  [req]
  {:status 200 :body "test"})

(defn post-inquiry
  [req]
  (let [params (req :params)
        sql [
        "insert into inquiry (
          corporation, name, name_ruby, mail_address, phone_no, body
        ) values (?, ?, ?, ?, ?, ?)"
        (params :corporation)
        (params :name)
        (params :name_ruby)
        (params :mail_address)
        (params :phone_no)
        (params :body)]]
    (if (sql-query db sql)
        {:status 200}
        {:status 500})))


(def api-routes
  (ring/ring-handler
    (ring/router
      ["/api"
        ["" {:get get-test}]
        ["/inquiry" {:post post-inquiry}]
      ])))

(def handler
  (middleware/wrap-base api-routes))
