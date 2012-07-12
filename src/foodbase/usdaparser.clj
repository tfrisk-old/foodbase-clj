(ns foodbase.usdaparser
  (:require [clojure.java.io :as jio])
  (:require [clojure.string :as str])
  (use korma.db)
  (use korma.core))

; KORMA ----------------------------------------------
(defdb devel (sqlite3 {:db "foodbase.db"
                      :user "foodbase"
                      :password "foodpass"}))

(defentity FOOD_DES
  (pk :NDB_No)
  (table :FOOD_DES)
  (database devel))

(defentity FD_GROUP
  (pk :FdGrp_Cd)
  (table :FD_GROUP)
  (database devel))

(defentity NUT_DATA
  (pk :id)
  (table :NUT_DATA)
  (database devel))

; insert data to table 
(defn insert-db-entry [entry table]
  (insert table (values entry)))

; ------------------------------------------------------
;database test commands

;sqlite3 foodbase.db "select * from FOOD_DES where NDB_No = 1222"
;sqlite3 foodbase.db "select * from FD_GROUP where FdGrp_Cd = 0800"

; ------------------------------------------------------
; database column names
(defn get-dbcolumns [table]
  (cond
    (= table "FOOD_DES")
      '("NDB_No","FdGrp_Cd","Long_Desc","Short_Desc","ComName","ManufacName","Survey",
        "Ref_desc","Refuse","SciName","N_Factor","Pro_Factor","Fat_Factor","CHO_Factor")
    (= table "FD_GROUP")
      '("FdGrp_Cd","FdGrp_Desc") ;FdGrp_Cd linked to FOOD_DES FdGrp_Cd
    (= table "NUT_DATA")
      '("NDB_No","Nutr_No","Nutr_Val","Num_Data_Pts","Std_Error","Src_Cd","Deriv_Cd","Ref_NDB_No",
        "Add_Nutr_Mark","Num_Studies","Min","Max","DF","Low_EB","Up_EB","Stat_cmt","CC")
    ))
; ------------------------------------------------------

;put values to list
;"11790^1100^Kale, cooked, boiled, drained, with salt^KALE,CKD,BLD,DRND,W/SALT^^^^^0^^6.25^2.44^8.37^3.57"
(defn split-line [line]
  (str/split line #"\^"))

;trim ~ characters
;"~11790~^~1100~^~Kale, cooked, boiled, drained, with salt~^~KALE,CKD,BLD,DRND,W/SALT~^~~^~~^~~^~~^0^~~^6.25^2.44^8.37^3.57"
(defn trim-tildes [line]
  (str/replace line #"~" ""))

; map keys to values
(defn map-raw-lines [line table]
    (zipmap (get-dbcolumns table)
    (split-line
      (trim-tildes line))))

(defn -main [& args]
  ; FOOD_DES
  (with-open [rdr (jio/reader "./materials/FOOD_DES.txt")]
    (doseq [line (line-seq rdr)]
      (insert-db-entry (map-raw-lines line "FOOD_DES") FOOD_DES)))
  
  ; FD_GROUP
  (with-open [rdr (jio/reader "./materials/FD_GROUP.txt")]
    (doseq [line (line-seq rdr)]
      (insert-db-entry (map-raw-lines line "FD_GROUP") FD_GROUP)))
  
  ; NUT_DATA
  (with-open [rdr (jio/reader "./materials/NUT_DATA.txt")]
    (doseq [line (line-seq rdr)]
      (insert-db-entry (map-raw-lines line "NUT_DATA") NUT_DATA)))
)
