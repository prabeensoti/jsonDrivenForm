# JSON Driven Template
If no used grid then also send grid="" and gridindex=""
if used then look at employee json example 
for grid view 
"grid": {
          "columnsize": "11",
          "minrowheight": "10vh"
        }
        where columnsize is number of column
        and minrowheight is minimum height of row 
        both are optional if you supply as 

 "grid": {
           "columnsize": "",
           "minrowheight": ""
         }    
         then default columnsize is 12 
 
 for each feild to fixed grid position you can use
 
 "gridindex": {
               "column": "1,6",
               "row": "1,2"
             }
             
where 1,6 is for 1st line to 6th line of column |\*|\*|\*|\*|\*| | | | | | |
only fill (6-1) columns
where | is column divider and * is filled field in above case with 11 columnsize 
             
     
