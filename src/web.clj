(ns web
  (:require [compojure.core :refer [defroutes GET PUT POST DELETE ANY]]
            [compojure.handler :refer [site]]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [ring.adapter.jetty :as jetty]
            [environ.core :refer [env]]
            (:require [clojure.string :as str])))

(def Gibson '(" ES-335", " Les Paul", " Flying V", " SG", " Explorer", " Firebird", " ES-175"))

(def Fender '(" Stratocaster", " Telecaster", " Jaguar", " Mustang"))

(def Ibanez '(" RG", " RGA", " S", " Mikro", " RGD", " SA", " FR", " Talman", " AR", " ARZ", " Iceman", " Gio"))

(def Schecter '(" Damien", " Hellraiser", " Demon", " Blackjack", " Apocalypse", " Omen", " Retro", " Sustainiac", " Standard", " Special Edition", " Sun Valley Super Shredder"))

(def Jackson '(" X", " JS", " Soloist", " Dinky", " Rhoads", " Juggernaut", " Baritone", " Monarkh", " Warrior", " Demmelition", " Dominion", " Star"))

(defn default []
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body (repeatedly 5 (rand-nth Gibson))})

(defroutes app
  (GET "/" []
       (default))
  (ANY "*" []
       (route/not-found (slurp (io/resource "404.html")))))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 5000))]
    (jetty/run-jetty (site #'app) {:port port :join? false})))

;; For interactive development:
;; (.stop server)
;; (def server (-main))