(ns clojure-ring-app.views
  "Server-side HTML rendering (SSR) with Hiccup.

  This renders the same markup the client Helix components produce, so React
  can hydrate the page in the browser. Crawlers see fully rendered HTML."
  (:require [hiccup2.core :as h]
            [cheshire.core :as json]
            [clojure-ring-app.data :as data]))

(defn- profile-content [user]
  [:div {:class "profile"}
   [:h1 {:class "profile-name"} (:name user)]
   [:p {:class "profile-headline"} (:headline user)]
   [:p {:class "profile-location"} (:location user)]
   [:h2 {:class "section-title"} "About"]
   [:p {:class "profile-about"} (:about user)]
   [:h2 {:class "section-title"} "Experience"]
   [:ul {:class "experience-list"}
    (for [e (:experience user)]
      [:li {:class "experience-item"}
       [:strong (:title e)]
       [:span {:class "experience-company"} (:company e)]
       [:span {:class "experience-years"} (:years e)]])]
   [:h2 {:class "section-title"} "Skills"]
   [:ul {:class "skills-list"}
    (for [s (:skills user)]
      [:li {:class "skill"} s])]])

(defn- home-content []
  [:div {:class "home"}
   [:h1 "People"]
   [:ul {:class "people-list"}
    (for [u data/users]
      [:li [:a {:href (str "/in/" (:username u))} (:name u)]])]])

(defn- app-shell
  "Wraps page content in the HTML document. When initial-data-json is supplied,
  it is embedded so the client can hydrate the page."
  [title content & [initial-data-json]]
  (str (h/html
        {:mode :html}
        [:html
         [:head
          [:meta {:charset "utf-8"}]
          [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
          [:title title]
          [:link {:rel "stylesheet" :href "/css/app.css"}]]
         [:body
          [:div#app content]
          (when initial-data-json
            [:script {:type "application/json" :id "initial-data"}
             (h/raw initial-data-json)])
          [:script {:src "/js/main.js" :defer true}]]])))

(defn home-page-response []
  {:status 200
   :headers {"Content-Type" "text/html; charset=utf-8"}
   :body (app-shell "People" (home-content))})

(defn profile-page-response [user]
  {:status 200
   :headers {"Content-Type" "text/html; charset=utf-8"}
   :body (app-shell (:name user)
                    (profile-content user)
                    (json/generate-string {:user user}))})

(defn not-found-response []
  {:status 404
   :headers {"Content-Type" "text/html; charset=utf-8"}
   :body (app-shell "Not found"
                    [:div {:class "not-found"} "User not found"])})
