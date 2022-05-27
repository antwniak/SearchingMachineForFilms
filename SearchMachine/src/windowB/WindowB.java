package windowB;

import java.awt.EventQueue;
import com.searchMachine.lucene.Indexer;


import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.Color;


import javax.swing.JButton;
import javax.swing.JTextField;


import org.apache.lucene.queryparser.classic.ParseException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.JEditorPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class WindowB {

	private JFrame frame;
	private JTextField textField;
	private JButton search_button;
	Indexer writer;
	private static String queryS = "";
	List<String> displayResults; 
	static WindowB window;
	ArrayList <String> fields = new ArrayList<String>();
	Vector <String> history = new Vector<String>();
	String sort = "";
	StringBuilder sb = new StringBuilder();
	/**
	 * Launch the application.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException  {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new WindowB();
					window.frame.setVisible(true);
					window.createIndex();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});	
	}
	
	/**
	 * Create the application.
	 * @throws IOException 
	 */
	public WindowB() throws IOException {
		
		initialize();
		
	}
	
	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 */
	private void initialize() throws IOException {
		frame = new JFrame();
		frame.getContentPane().setFont(new Font("Yu Gothic UI Semilight", Font.PLAIN, 10));
		frame.setBounds(564, 564, 999, 606);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel historyOfSearching = new JLabel("HISTORY");
		historyOfSearching.setForeground(new Color(199, 21, 133));
		historyOfSearching.setFont(new Font("Poor Richard", Font.BOLD, 19));
		historyOfSearching.setBounds(364, 219, 79, 46);
		frame.getContentPane().add(historyOfSearching);
		
		JLabel lblNewLabel = new JLabel("KINOsearching");
		lblNewLabel.setLabelFor(frame.getContentPane());
		lblNewLabel.setForeground(new Color(153, 0, 102));
		lblNewLabel.setBackground(new Color(255, 51, 0));
		lblNewLabel.setFont(new Font("Poor Richard", Font.PLAIN, 39));
		lblNewLabel.setBounds(25, 187, 464, 66);
		frame.getContentPane().add(lblNewLabel);
		
		textField = new JTextField();
		textField.setFont(new Font("DialogInput", Font.BOLD, 15));
		textField.setBounds(30, 259, 324, 29);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		displayResults = new ArrayList<String>();
		
		history = holdHistory();
		JComboBox comboBox = new JComboBox(history);
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED) {
					String t = (String) comboBox.getSelectedItem();
					textField.setText(t);
				}
			}
		});
		comboBox.setBounds(351, 259, 125, 29);
		frame.getContentPane().add(comboBox);
		JEditorPane editorPane = new JEditorPane();
		editorPane.setBounds(10, 0, 329, 488);
		editorPane.setContentType("text/html");
		frame.getContentPane().add(editorPane);
		frame.setVisible(true);
	    frame.getContentPane().add(editorPane);	
	    
	    JScrollPane scrollPane = new JScrollPane (editorPane,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	    scrollPane.setBounds(548, 0, 427, 490);
		frame.getContentPane().add(scrollPane);
		
		JRadioButton title = new JRadioButton("Title");
		title.setBounds(30, 345, 103, 21);
		title.setForeground(new Color(153, 0, 102));
		title.setFont(new Font("Poor Richard", Font.BOLD, 14));
		title.setOpaque(false);
		title.setContentAreaFilled(false);
		title.setFocusable(false);
		title.setBorderPainted(true); // I'd like to enable it.
		title.setBorder(null);
		frame.getContentPane().add(title);
		
		JRadioButton year = new JRadioButton("Year");
		year.setBounds(30, 360, 103, 21);
		year.setForeground(new Color(153, 0, 102));
		year.setFont(new Font("Poor Richard", Font.BOLD, 14));
		year.setOpaque(false);
		year.setContentAreaFilled(false);
		year.setFocusable(false);
		year.setBorderPainted(true); // I'd like to enable it.
		year.setBorder(null);
		frame.getContentPane().add(year);
		
		JRadioButton imdb_rating = new JRadioButton("Imdb Rating");
		imdb_rating.setBounds(30, 375, 103, 21);
		imdb_rating.setForeground(new Color(153, 0, 102));
		imdb_rating.setFont(new Font("Poor Richard", Font.BOLD, 14));
		imdb_rating.setOpaque(false);
		imdb_rating.setContentAreaFilled(false);
		imdb_rating.setFocusable(false);
		imdb_rating.setBorderPainted(true); // I'd like to enable it.
		imdb_rating.setBorder(null);
		frame.getContentPane().add(imdb_rating);
		
		JRadioButton genre = new JRadioButton("Genre");
		genre.setBounds(30, 390, 103, 21);
		genre.setForeground(new Color(153, 0, 102));
		genre.setFont(new Font("Poor Richard", Font.BOLD, 14));
		genre.setOpaque(false);
		genre.setContentAreaFilled(false);
		genre.setFocusable(false);
		genre.setBorderPainted(true); // I'd like to enable it.
		genre.setBorder(null);
		frame.getContentPane().add(genre);
		
		JRadioButton sortByYear = new JRadioButton("Sort By year");
		sortByYear.setBounds(140, 345, 103, 21);
		sortByYear.setForeground(new Color(153, 0, 102));
		sortByYear.setFont(new Font("Poor Richard", Font.BOLD, 14));
		sortByYear.setOpaque(false);
		sortByYear.setContentAreaFilled(false);
		sortByYear.setFocusable(false);
		sortByYear.setBorderPainted(true); // I'd like to enable it.
		sortByYear.setBorder(null);
		frame.getContentPane().add(sortByYear);
		
		JRadioButton sortByRate = new JRadioButton("Sort By rate");
		sortByRate.setBounds(140, 360, 103, 21);
		sortByRate.setForeground(new Color(153, 0, 102));
		sortByRate.setFont(new Font("Poor Richard", Font.BOLD, 14));
		sortByRate.setOpaque(false);
		sortByRate.setContentAreaFilled(false);
		sortByRate.setFocusable(false);
		sortByRate.setBorderPainted(true); // I'd like to enable it.
		sortByRate.setBorder(null);
		frame.getContentPane().add(sortByRate);
		
		search_button = new JButton("Search \r\n");
		Document doc = editorPane.getDocument();
		
		search_button.addActionListener(new ActionListener() {	
			public void actionPerformed(ActionEvent e) {
				
				
				String t = (String) comboBox.getSelectedItem();
				if(e.getSource() == search_button) {
					
					if(title.isSelected()) {
						fields.add("Title");
					}
					if(year.isSelected()) {
						fields.add("Year");
					}
					if(imdb_rating.isSelected()) {
						fields.add("Imdb Rating");
					}
					if(genre.isSelected()) {
						fields.add("Genre");
					}
					if(sortByYear.isSelected()) {
						sort="Year";
					}
					if(sortByRate.isSelected()) {
						sort="Imdb Rating";
					}
					queryS = textField.getText();
					try {
						holdQueries(queryS);
					} catch (IOException e2) {
									// TODO Auto-generated catch block
						e2.printStackTrace();
					}

					try {
						displayResults= writer.searchIndex(queryS,100,fields.toArray(new String[0]),sort);
						if(!displayResults.isEmpty()) {
							for(int i=0; i< displayResults.size(); i++) {
								sb.append(displayResults.get(i)+"\n");
								editorPane.setText(sb.toString());
								highLight(editorPane,queryS);
							}
						
						}else {
							sb.append("Sorry, I can't help you,try something else");
							editorPane.setText(sb.toString()+"\n");
						}
						
						if(sb.length() > 0) {
							sb.replace(0, sb.length(), " ");		
						}
						
					} catch (IOException e1) {
									// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ParseException e1) {
									// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
				}
			}
		});
		
		search_button.setForeground(new Color(153, 0, 102));
		search_button.setFont(new Font("Poor Richard", Font.BOLD, 31));
		search_button.setBounds(10, 299, 168, 46);
		search_button.setOpaque(false);
		search_button.setContentAreaFilled(false);
		search_button.setFocusable(false);
		search_button.setBorderPainted(true); // I'd like to enable it.
		search_button.setBorder(null);
		frame.getContentPane().add(search_button);
		
		search_button.addMouseListener((MouseListener) new MouseAdapter(){
            private Color blue;
			@Override
            public void mouseEntered(MouseEvent e){
				search_button.setBorder(BorderFactory.createLineBorder(blue, 2,true));
            }
            @Override
            public void mouseExited(MouseEvent e){
            	search_button.setBorder(null);
                //When mouse exits no border.
            }
        });
		

		JLabel backround = new JLabel("");
		backround.setBackground(Color.BLACK);
		backround.setToolTipText("");
		backround.setBounds(-17, -37, 907, 525);
		backround.setForeground(new Color(0, 0, 0));
		backround.setFont(new Font("Snap ITC", Font.PLAIN, 30));
		backround.setIcon(new ImageIcon("src\\good_films.jpg"));
		frame.getContentPane().add(backround);
	}	
	
	private void createIndex() throws IOException {
		
		Path indexDirectory = Paths.get("src","directory");
		String indexPath = indexDirectory.toFile().getAbsolutePath();
		writer = new Indexer();
		if(indexDirectory.toFile().list().length==0) {
			writer.makeIndex(indexPath,true);
		}else {
			writer.makeIndex(indexPath,false);

		}
	}
	
	public void holdQueries(String queryS) throws IOException {
		FileWriter fWriter = new FileWriter("src\\queries.txt",true);
		BufferedWriter writer= new BufferedWriter(fWriter);
		writer.write(queryS);
		writer.newLine();
		writer.close();
	}
	
	public Vector<String> holdHistory() throws IOException {
		Vector<String> history = new Vector<String>();
		HashMap<String,Integer> score = new HashMap<String,Integer>();
		BufferedReader reader=null;
		try {
			reader = new BufferedReader(new FileReader("src\\queries.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String line = reader.readLine();
			while(line != null) {
				boolean flag = false;
				for(String key:score.keySet()) {
					if(line.equals(key)) {
						flag = true;
						score.replace(line, score.get(line)+1);
					}
				}
				if(flag==false) {
					score.put(line, 1);
				}
				line = reader.readLine();
			}
			for(String key:score.keySet()) {
				history.add(key);
			}
			return history;
		}
	
	public void highLight(JEditorPane editorPane,String query) throws BadLocationException {
		Highlighter hil = editorPane.getHighlighter();
		String [] queryS =  query.split(" ");
		javax.swing.text.Document doc = editorPane.getDocument();
		for(String s:queryS) {
			s = s.toLowerCase();
			for(int i=0; i+s.length()<doc.getLength(); i++) {
				String text = doc.getText(i,s.length()).toLowerCase();
				if(s.equals(text)) {
					javax.swing.text.DefaultHighlighter.DefaultHighlightPainter highlightPainter =
					new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);			// Search for query
					editorPane.getHighlighter().addHighlight(i, i+s.length(), highlightPainter);
				}
			}	
		}
	}

	
}

