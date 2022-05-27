package com.searchMachine.lucene;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

public class Indexer 
{
	private IndexWriter writer;
	private IndexSearcher searcher;
	private MultiFieldQueryParser queryParser;
	private Query query;
	Directory dir;
	Analyzer analyzer;
	String[] fields;
	List<String> displayResults;
	
	public Indexer() throws IOException {

	}

	public void makeIndex(String indexDirectoryPath,boolean create) throws IOException
	{
		analyzer = new StandardAnalyzer();
		dir = FSDirectory.open(Paths.get(indexDirectoryPath));
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		config.setOpenMode(OpenMode.CREATE);
		writer = new IndexWriter(dir,config);
	    File csvMovieBase = new File("src\\films_DataBase.csv");
		BufferedReader br = new BufferedReader(new FileReader(csvMovieBase));
		String line = "";
		int count = 0;
		try {
			while((line = br.readLine()) != null) {
				if(count == 0) {
					count++;
					continue;
				}
				String[] row = line.split("\t");
				
				Document doc = getDocument(row[0],row[1],row[2],row[3],row[4],row[5]);
				writer.addDocument(doc);
			}	
		}catch (FileNotFoundException e) {
				e.printStackTrace();
		}
		writer.close();
	}

	public Document getDocument(String title,String year,String plot,String runtime,String genre,String imdb_rating)
	{
		Document document = new Document();
		document.add(new TextField("Title",title,Field.Store.YES));
		document.add(new TextField("Year",year.toString(),Field.Store.YES));
		document.add(new TextField("Plot",plot,Field.Store.YES));
		document.add(new TextField("Runtime",runtime.toString(),Field.Store.YES));
		document.add(new TextField("Genre",genre,Field.Store.YES));
		document.add(new TextField("Imdb Rating",imdb_rating.toString(),Field.Store.YES));
		document.add(new SortedDocValuesField ("Year",new BytesRef(year)));
		document.add(new StoredField("Year",year));
		document.add(new SortedDocValuesField ("Imdb Rating",new BytesRef(imdb_rating)));
		document.add(new StoredField("Imdb Rating",imdb_rating));

		return document;
	}
	
	public List<String> searchIndex(String queryS, int maxHits,String[] field, String sortField) throws ParseException, IOException {
		displayResults = new ArrayList();
		DirectoryReader reader = DirectoryReader.open(dir);
		searcher = new IndexSearcher(reader); //reader points to index folder	
		if(field.length==0) {
			queryParser = new MultiFieldQueryParser(new String[] {"Title","Year","Plot","Runtime","Genre","Imdb Rating","Drama"} , analyzer);
			
		}else {
			queryParser = new MultiFieldQueryParser(field, analyzer); 
		}
		queryParser.setAllowLeadingWildcard(true);
		TopDocs hits = null;
		//create a query object by parsing the search expression through QueryParse
		query = queryParser.parse(queryS); 
		try {
			if(sortField=="") {
				hits = searcher.search(query, maxHits);
			}else if(sortField=="Year" || sortField=="Imdb Rating") {
				Sort sort = new Sort(new SortField(sortField, SortField.Type.STRING));
				hits = searcher.search(query, maxHits,sort);

			}
			//make the search which will return the TopDocs
			//TopDocs points to the top N search result which matches the search criteria
			for(ScoreDoc scoreDoc : hits.scoreDocs){
		       Document doc = searcher.doc(scoreDoc.doc);
		       displayResults.add("<h1>"+doc.get("Title")+"</h1>"+"\n");
		 	   displayResults.add("Year:"+doc.get("Year")+"\n");
		 	   displayResults.add("Plot:"+doc.get("Plot")+"\n");
		 	   displayResults.add("Runtime:"+doc.get("Runtime")+"\n");
		 	   displayResults.add("Genre:"+doc.get("Genre")+"\n");
		 	   displayResults.add("Imdb Rating:"+doc.get("Imdb Rating")+"\n");
		 	   displayResults.add(doc.get("Drama")+"\n");   	
		      }
   
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		reader.close();
		return displayResults;
		
		
		
		
//		
	}
	
}
	




