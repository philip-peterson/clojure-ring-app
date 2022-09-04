(ns clojure-ring-app.routes.handler
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [clojure-ring-app.routes.api-routes :refer [api-routes]]
            [clojure-ring-app.routes.resource-routes :refer [resource-routes]]))

(defn wrap-dir-index
  "Middleware to force request for / to return index.html"
  [handler]
  (fn [req]
    (handler (update-in req [:uri] #(if (= "/" %) "/index.html" %)))))

(defapi app
  {:api {:invalid-routes-fn nil}
   :ring-swagger {:ignore-missing-mappings? true}}

  (middleware [wrap-dir-index] resource-routes api-routes))
