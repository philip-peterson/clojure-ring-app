(ns clojure-ring-app.handler
  (:require [ring.middleware.resource :refer [wrap-resource]]
            [clojure-ring-app.data :as data]
            [clojure-ring-app.views :as views]))

(defn- handle [req]
  (let [uri (:uri req)]
    (cond
      (= "/" uri)
      (views/home-page-response)

      :else
      (if-let [[_ username] (re-matches #"/in/([^/]+)" uri)]
        (if-let [user (data/find-user username)]
          (views/profile-page-response user)
          (views/not-found-response))
        (views/not-found-response)))))

;; Static assets (css/js) under resources/public/ are served first; everything
;; else falls through to `handle`.
(def app
  (wrap-resource handle "public"))
