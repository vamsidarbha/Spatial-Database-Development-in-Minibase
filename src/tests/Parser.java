package tests;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String fileName = "/home/akarsh/Downloads/query.txt";
		String line = "";
		String query = "";
		boolean valid = true;
		BufferedReader b=null;
		try {
			FileReader f = new FileReader(fileName);
			b = new BufferedReader(f);
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			while((line = b.readLine()) != null) {
				query = query +" "+ line.trim();
				query.trim();
				if (query.contains(";"))
				{
					query=query.toUpperCase();
					String words[] = query.split(" ");
					valid = true;
					switch (words[1]) {
					case "CREATE":
						//Pattern pattern = Pattern.compile("CREATE TABLE [a-zA-Z_]+[\\s]*[(][ a-zA-Z_0-9()]+[)]");
						Pattern pattern = Pattern.compile("CREATE TABLE [a-zA-Z_]+[\\s]*[(].+[)]");
						Matcher matcher = pattern.matcher(query);
						//String arguments[] = query.split("(",2);
						//System.out.println(arguments[1]);
						if (matcher.find()) {
							System.out.println("I found the text " + matcher.group()+" starting at " +	
								"index "+ matcher.start()+" and ending at index "+matcher.end());
							int index1=query.indexOf("(");
							int index2=query.indexOf(";");
							String substring=query.substring(index1+1, index2-1);
							String[] arguments=null;
							if(!substring.isEmpty()){
								 arguments=substring.split(",");							
								}
							int n= arguments.length;
							int i=0;
							boolean flag=false;
							while(i<n){
								System.out.println(arguments[i]);
								Pattern arg = Pattern.compile("[a-zA-Z_]+[\\s]*[NUMBER|VARCHAR[(][0-9]+[)]|SDO_GEOMETRY][\\s]*[[PRIMARY KEY]|[[UNIQUE]?[NOT NULL]?]]?");
//								Pattern arg = Pattern.compile("[a-zA-Z_]+[\\s]*(NUMBER|VARCHAR[(][0-9]+[)]|SDO_GEOMETRY)[\\s]*(PRIMARY KEY)?|((UNIQUE)?(NOT NULL)?)");

								Matcher match = arg.matcher(arguments[i]);
								if (match.find()){
									System.out.println("I found the text " + match.group()+" starting at " +	
											"index "+ match.start()+" and ending at index "+match.end());
									if(arguments[i].contains("PRIMARY KEY") && !flag){
										flag=true;
									}
									else if(arguments[i].contains("PRIMARY KEY") && flag){
										valid=false;
									}
								}
								else
									valid = false;
								
								i++;
							}
						}
						else{
							valid = false;
						}
						break;
					case "INSERT":
						
						break;
					case "SELECT":
						//Pattern select_pattern = Pattern.compile("SELECT [a-zA-Z_,.\\s]* SDO_GEOM.SDO_AREA[(][a-zA-Z_,.0-9\\s]+[)] FROM [a-zA-Z_.\\s]+ WHERE [a-zA-Z_,.='\\s]+");
						//Pattern select_pattern=Pattern.compile("SELECT [[[a-zA-Z][a-zA-Z_]*]*[\\.]?[[a-zA-Z][a-zA-Z_]*]+[,]]*SDO_GEOM.SDO_AREA[(][[[a-zA-Z][a-zA-Z_]*]+[\\.][[a-zA-Z][a-zA-Z_]*]+][,][0-9]*[\\.]?[0-9]+[)] FROM [a-zA-Z_.\\s]+ WHERE [a-zA-Z_]+[\\.]?[a-zA-Z_]* [=] [a-zA-Z_']+[\\.]?[a-zA-Z_0-9']*");
						Pattern select_pattern=Pattern.compile("SELECT [([a-zA-Z][a-zA-Z_]*)*[\\.]?[[a-zA-Z][a-zA-Z_]*]+[,]]*SDO_GEOM.SDO_AREA[(][a-zA-Z_,.0-9\\s]+[)] FROM [a-zA-Z_.\\s]+ WHERE [a-zA-Z_]+[\\.]?[a-zA-Z_]* [=] [a-zA-Z_']+[\\.]?[a-zA-Z_0-9']*");
						//Pattern select_pattern=Pattern.compile("SELECT [[[a-zA-Z][a-zA-Z_]*]*[\\.]?[[a-zA-Z][a-zA-Z_]*]+[,]]*SDO_GEOM.SDO_AREA[(][a-zA-Z_,.0-9\\s]+[)] FROM [a-zA-Z][a-zA-Z_]*[\\s]+[a-zA-Z][a-zA-Z_]*[\\s]+ WHERE [[a-zA-Z][a-zA-Z_]*]*[\\.]?[[a-zA-Z][a-zA-Z_]*]+ [=] ([[a-zA-Z][a-zA-Z_]*]*[\\.]?[[a-zA-Z][a-zA-Z_]*]+|[0-9]+|[\\s]*['][a-zA-Z][a-zA-Z_0-9]*[']");
						
						
						Matcher select_matcher = select_pattern.matcher(query);
						//boolean valid_query=false;
						
						Pattern from=Pattern.compile("FROM");
						Matcher match_from=from.matcher(query);
						int i=0;
						while(match_from.find()){
							i++;
						}
						
						Pattern where=Pattern.compile("WHERE");
						Matcher match_where=where.matcher(query);
						int j=0;
						while(match_where.find()){
							j++;
						}
						
						if(select_matcher.find()&& i<2 && j<2){
							System.out.println("Valid query");
						}
						else{
							System.out.println("Not Valid Query");
						}
						break;
						
						
					default:
						valid = false;
						break;
					}
					if (!valid) {
						System.out.println("Invalid Line Input");
						b.close();
						System.exit(1);
					}
					query = "";
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		b.close();
		System.exit(0);
	}

}
