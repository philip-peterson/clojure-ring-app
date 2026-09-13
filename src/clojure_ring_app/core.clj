(ns clojure-ring-app.core
  (:gen-class)
  (:require [ring.adapter.jetty :as jetty]
            [clojure-ring-app.handler :refer [app]]))

(defn -main [& _]
  (println "Serving on http://localhost:3399")
  (jetty/run-jetty app {:port 3399 :join? true}))
