(ns clojure-ring-app.data)

;; Static fake data shared between the server (JVM Clojure) and the client
;; (ClojureScript). A real app would swap this for a database-backed lookup.

(def users
  [{:username "ada-lovelace"
    :name "Ada Lovelace"
    :headline "Mathematician & Writer | First Programmer"
    :location "London, United Kingdom"
    :about "Analytical Engine enthusiast. I write notes that are basically code."
    :skills ["Mathematics" "Algorithms" "Analytical Engines"]
    :experience [{:title "Countess of Lovelace"
                  :company "Self-employed"
                  :years "1835 - 1852"}]
    :education [{:school "University of Imagination" :degree "Mathematics"}]}
   {:username "grace-hopper"
    :name "Grace Hopper"
    :headline "Rear Admiral | Compiler Pioneer"
    :location "New York, NY"
    :about "It's easier to ask forgiveness than it is to get permission."
    :skills ["COBOL" "Compilers" "Debugging"]
    :experience [{:title "Rear Admiral"
                  :company "United States Navy"
                  :years "1943 - 1986"}]
    :education [{:school "Yale University" :degree "Mathematics"}]}
   {:username "alan-turing"
    :name "Alan Turing"
    :headline "Mathematician | Cryptanalyst"
    :location "London, United Kingdom"
    :about "Sometimes it is the people no one imagines anything of who do the things that no one can imagine."
    :skills ["Computability" "Cryptography" "Machine Intelligence"]
    :experience [{:title "Fellow"
                  :company "King's College, Cambridge"
                  :years "1935 - 1954"}]
    :education [{:school "University of Cambridge" :degree "Mathematics"}]}])

(defn find-user
  "Returns the user map for a username, or nil if not found."
  [username]
  (some #(when (= username (:username %)) %) users))
