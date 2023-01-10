(ns mkapi.db
  (:require
   [next.jdbc.sql :as sql]))


(def db {:dbtype "sqlite"
         :dbname "db/mkapi.db"})

(defn create-inquiry []
  (let [sql "create table if not exists inquiry (
              id integer primary key autoincrement,
              corporation text,
              name text,
              name_ruby text,
              mail_address text,
              phone_no text,
              body text,
              created_at text default (datetime('now','localtime')),
              updated_at text default (datetime('now','localtime')))"]
    (sql/query db [sql])))

(create-inquiry)
