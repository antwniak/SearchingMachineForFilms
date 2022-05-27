import pandas as pd
import os, glob 

path = 'C:/Users/tania/Desktop/University/Epiloghs/Information_Retrieval'
all_files = glob.glob(os.path.join(path,"films_DataBase*.csv"))
df_from_each_file = (pd.read_csv(f, sep = '/t',index_col = False) for f in all_files)
df_merged = pd.concat(df_from_each_file)
df_merged.to_csv ("films_DataBase.csv", index = False)
