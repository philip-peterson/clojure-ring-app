(ns clojure-ring-app.client.core
  (:require ["react-dom/client" :as rdom]
            [helix.core :refer [$]]
            [clojure-ring-app.client.ui :as ui]))

(defn ^:export init
  "Hydrates the server-rendered profile page. Reads the initial data embedded by
  the server, then attaches React to the existing #app DOM node."
  []
  (when-let [data-el (.getElementById js/document "initial-data")]
    (let [app-el (.getElementById js/document "app")
          data (js->clj (js/JSON.parse (.-textContent data-el))
                        :keywordize-keys true)]
      (rdom/hydrateRoot app-el ($ ui/profile {:user (:user data)})))))
