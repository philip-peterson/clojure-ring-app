(ns clojure-ring-app.client.ui
  "Helix (React) components. Must render the same markup as the server-side
  Hiccup views in clojure-ring-app.views so React can hydrate the page."
  (:require [helix.core :refer [defnc $]]
            [helix.dom :as d]))

(defnc profile [{:keys [user]}]
  (d/div {:class "profile"}
    (d/h1 {:class "profile-name"} (:name user))
    (d/p {:class "profile-headline"} (:headline user))
    (d/p {:class "profile-location"} (:location user))
    (d/h2 {:class "section-title"} "About")
    (d/p {:class "profile-about"} (:about user))
    (d/h2 {:class "section-title"} "Experience")
    (d/ul {:class "experience-list"}
      (for [e (:experience user)]
        (d/li {:class "experience-item" :key (:title e)}
          (d/strong (:title e))
          (d/span {:class "experience-company"} (:company e))
          (d/span {:class "experience-years"} (:years e)))))
    (d/h2 {:class "section-title"} "Skills")
    (d/ul {:class "skills-list"}
      (for [s (:skills user)]
        (d/li {:class "skill" :key s} s)))))
