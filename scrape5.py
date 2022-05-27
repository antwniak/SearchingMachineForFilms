import requests
from bs4 import BeautifulSoup as bs
import pandas as pd
from googletrans import Translator
from pprint import pprint

url = "https://www.imdb.com/list/ls062911411/?sort=list_order,asc&st_dt=&mode=detail&page=5"

page = requests.get(url)
		
translator = Translator()
soup = bs(page.content, 'html.parser')

fields_film = []

list(soup.children)
	
for film in soup.find_all('div',class_='lister-item-content'):
		
	headline = film.h3.a.get_text()
	title = translator.translate(headline,src='el')
	#print("Title:"+title.text)
	year = film.find('span',class_='lister-item-year text-muted unbold').get_text()
	#print("Year:",year)
	plot = film.find('p', class_='').get_text().replace('\n','')
	#print("plot:"+plot)
	runtime = film.find('span',class_='runtime').get_text().replace('\n','')
	#print("runtime:"+runtime)
	genre = film.find('span',class_='genre').get_text().replace('\n','')
	#print("genre :"+genre)
	#for d in soup.find_all('p',class_='text-muted text-small'):
	#director = d.a[0].get_text()
	imdb_rating = film.find('span',class_='ipl-rating-star__rating').get_text().replace('\n','')
	#print("imdb_rating:",imdb_rating)

	fields_film.append([title.text,year,plot,runtime,genre,imdb_rating])
	df = pd.DataFrame(fields_film,columns = ['Title','Year','Plot','Runtime','Genre','Imdb rating'])
df.to_csv('films_DataBase5.csv',sep="\t",index = False)				