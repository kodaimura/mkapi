(ns mkapi.util
  (:require
    [postal.core :as postal]
    [mkapi.db :refer [db sql-query]]
    [env :refer [env]]))


(defn make-inquiry-mail-body
  [params]
  (str "会社名:　　" (params :corporation) "\n"
       "お名前:　　" (params :name) " (" (params :name_ruby) ") 様\n"
       "メール:　　" (params :mail_address) "\n"
       "電話番号:　" (params :phone_no) "\n\n"
       (params :body) "\n"))

(defn send-inquiry-mail
  [params]
  (postal/send-message
    {:from "iquiry@murakamikodai.com"
     :to "murakami@pgkodai.com"
     :subject "【お問い合わせフォーム】"
     :body (make-inquiry-mail-body params)}))

(defn send-inquiry-confirm-mail
  [params]
  (postal/send-message
    {:host (env :mail-server-host)
     :user "murakami@pgkodai.com"
     :pass (env :mail-server-password)
     :port 587
     :tls true}
    {:from "murakami@pgkodai.com"
     :to (params :mail_address)
     :subject "【お問合せ確認】村上晃大"
     :body (str "お世話になっております。村上晃大です。\n"
                "この度はお問合せいただき、ありがとうございます。\n"
                "下記内容で受信致しました。\n"
                "2~3日以内に【murakami@pgkodai.com】より、ご連絡いたします。\n"
                "\n"
                "-------------------------------------------\n"
                (make-inquiry-mail-body params)
                "-------------------------------------------\n"
                "よろしくお願いいたします。")
    }))

(defn insert-inquiry
  [params]
  (let [sql [
      "insert into inquiry (
        corporation, name, name_ruby, mail_address, phone_no, body
      ) values (?, ?, ?, ?, ?, ?)"
      (params :corporation)
      (params :name)
      (params :name_ruby)
      (params :mail_address)
      (params :phone_no)
      (params :body)]]
    (sql-query db sql)))

