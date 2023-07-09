(ns mkapi.util
  (:require
    [next.jdbc.sql :as sql]
    [postal.core :as postal]
    [mkapi.db :refer [db]]
    [mkapi.env :refer [env]]))


;(defn make-inquiry-mail-body
;  [params]
;  (str "会社名:　　" (:corporation params) "\n"
;       "お名前:　　" (:name params) " (" (:name_ruby params) ") 様\n"
;       "メール:　　" (:mail_address params) "\n"
;       "電話番号:　" (:phone_no params) "\n\n"
;       (:body params) "\n"]))

(defn make-inquiry-mail-body
  [params]
  (str "お名前:　　" (:name params) " 様\n"
       "メール:　　" (:mail_address params) "\n"
       "電話番号:　" (:phone_no params) "\n\n"
       (:body params) "\n"))

(defn send-inquiry-mail
  [params]
  (postal/send-message
    {:host (:mail-server-host env)
     :user "contact@murakamikodai.com"
     :pass (:mail-server-password env)
     :port 587
     :tls true}
    {:from "form@murakamikodai.com"
     :to "contact@murakamikodai.com"
     :subject "【お問い合わせフォーム】"
     :body (make-inquiry-mail-body params)}))

(defn send-inquiry-confirm-mail
  [params]
  (postal/send-message
    {:host (:mail-server-host env)
     :user "contact@murakamikodai.com"
     :pass (:mail-server-password env)
     :port 587
     :tls true}
    {:from "contact@murakamikodai.com"
     :to (:mail_address params)
     :subject "【お問合せ確認】村上晃大"
     :body (str (:name params) "様\n\n"
                "お世話になっております。村上晃大です。\n"
                "お問合せありがとうございます。下記内容で受信致しました。\n"
                "2~3日以内に【contact@murakamikodai.com】より、ご連絡いたします。\n"
                "\n"
                "-------------------------------------------\n"
                (make-inquiry-mail-body params)
                "-------------------------------------------\n"
                "よろしくお願いいたします。")
    }))

(defn insert-inquiry!
  [params]
  (let [sql [
      "insert into inquiry (
        corporation, name, name_ruby, mail_address, phone_no, body
      ) values (?, ?, ?, ?, ?, ?)"
      (:corporation params)
      (:name params)
      (:name_ruby params)
      (:mail_address params)
      (:phone_no params)
      (:body params)]]
    (sql/query db sql)))

