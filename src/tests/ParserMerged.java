package tests;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserMerged {

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
						System.out.println("This is validation for create query");
						Pattern pattern = Pattern.compile("CREATE TABLE [a-zA-Z][a-zA-Z_]+[\\s]*[(].+[)]");
						Matcher matcher = pattern.matcher(query);
						
						
						
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
							boolean createFlag=false;
							while(i<n){
								//System.out.println(arguments[i]);
								//System.out.println(arguments[i].length());
								Pattern arg = Pattern.compile("[a-zA-Z][a-zA-Z_]+[\\s]*(NUMBER|VARCHAR2[(][0-9]+[)]|SDO_GEOMETRY)[\\s]*(PRIMARY KEY|UNIQUE NOT NULL|UNIQUE|NOT NULL)?");
//								Pattern arg = Pattern.compile("[a-zA-Z_]+[\\s]*(NUMBER|VARCHAR[(][0-9]+[)]|SDO_GEOMETRY)[\\s]*(PRIMARY KEY)?|((UNIQUE)?(NOT NULL)?)");NOT NULL?))?

								Matcher match = arg.matcher(arguments[i]);
								if (match.find()){
									System.out.println("I found the text " + match.group()+" starting at " +	
											"index "+ match.start()+" and ending at index "+match.end());
									createFlag=true;
									if(!(match.start()==1&&match.end()==arguments[i].length()))
									{
									 valid=false;
									 System.out.println("Invalid Create Query");
									 createFlag=false;
									 break;
									}
									if(arguments[i].contains("SDO_GEOMETRY") && (arguments[i].contains("PRIMARY KEY") || arguments[i].contains("UNIQUE NOT NULL") || arguments[i].contains("UNIQUE") || arguments[i].contains("NOT NULL")))
									{
										valid=false;
										System.out.println("Invalid Create Query");
										createFlag=false;
										break;
									}
									
									if(arguments[i].contains("PRIMARY KEY") && !flag){
										flag=true;
									}
									else if(arguments[i].contains("PRIMARY KEY") && flag){
										valid=false;
										System.out.println("Invalid Create Query");
										createFlag=false;
										break;
									}
									if(createFlag){
										System.out.println("Valid create query");
									}
								}
								else
									valid = false;
									System.out.println("Invalid Create Query");
								
								i++;
							}
						}
						
						else{
							valid = false;
							System.out.println("Invalid Create Query");
						}
						break;
					case "INSERT":
						System.out.println("This is validation for Insert query");
						Pattern patternI = Pattern.compile("INSERT INTO [a-zA-Z][a-zA-Z_]+[\\s]*VALUES[\\s]*[(].+[)]");
						Matcher matcherI = patternI.matcher(query);
						//String arguments[] = query.split("(",2);
						Pattern patternD = Pattern.compile("SDO_GEOMETRY");
						Matcher matcherD = patternD.matcher(query);
						//System.out.println("U");
													
						if (matcherI.find()) {
							System.out.println("I found the text " + matcherI.group()+" starting at " +	
								"index "+ matcherI.start()+" and ending at index "+matcherI.end());
							int index1=query.indexOf("(");
							//int index2=query.indexOf(";");
							int index2=query.lastIndexOf(")");
							int index3;
							
						if(matcherD.find()){
							 index3=matcherD.start();
						   }
						else 
						{
						    index3=0;
						}
						String substring=query.substring(index1+1, index3-1);
						String substring2=query.substring(index3,index2);
						System.out.println("S1:"+substring);
						System.out.println("S2:"+substring2);
						String[] arguments=null;
						
						
						  if(!substring.isEmpty()){
								 arguments=substring.split(",");							
								}
							int n= arguments.length;
							int i=0;
							boolean flag1=false;
							boolean flag2=false;
							//while(i<n){
								//System.out.println(arguments[i]);
								//System.out.println(arguments[i].length());
//								([\\s]*[0-9]+|[\\s]*['][\\s]*[a-zA-Z][a-zA-Z_]*[\\s]*['][\\s]*)*
								//Pattern arg1=Pattern.compile("([\\s]*[0-9]+|[\\s]*['][\\s]*[a-zA-Z][a-zA-Z_]*[\\s]*['][\\s]*)*");
								Pattern arg1=Pattern.compile("[[\\s]*[']?[0-9a-zA-z][0-9a-zA-z_]*[']?[,][\\s]*]*");
								//Pattern arg = Pattern.compile("[\\s]*SDO_GEOMETRY[\\s]*[(][\\s]*[0-9]+[,][\\s]*,[\\s]*NULL[\\s]*[,][\\s]*NULL[\\s]*[,][\\s]*SDO_ELEM_INFO_ARRAY[\\s]*[(][0-9,\\s]+[)][\\s]*[,][\\s]*SDO_ORDINATE_ARRAY[\\s]*[(][0-9,\\s]+[)][\\s]*[)]");
										//+ "[,]NULL[,]NULL[,]SDO_ELEM_INFO_ARRAY[(][0-9]+[,][0-9]+[,][0-9]+[)][,]SDO_ORDINATE_ARRAY[(][0-9]+[,][0-9]+[,][0-9]+[,][0-9]+[)][)])?");
//								Pattern arg = Pattern.compile("[a-zA-Z_]+[\\s]*(NUMBER|VARCHAR[(][0-9]+[)]|SDO_GEOMETRY)[\\s]*(PRIMARY KEY)?|((UNIQUE)?(NOT NULL)?)");NOT NULL?))?
								Pattern arg=Pattern.compile("[\\s]*SDO_GEOMETRY[\\s]*[(][\\s]*[0-9]+[,][\\s]*NULL[\\s]*,[\\s]*NULL[\\s]*,[\\s]*SDO_ELEM_INFO_ARRAY[\\s]*[(][\\s]*[0-9,\\s]+[)][\\s]*[,][\\s]*SDO_ORDINATE_ARRAY[\\s]*[(][\\s]*[0-9,\\s]+[)][\\s]*[)]");
								substring.trim();
								substring2.trim();
								Matcher match = arg1.matcher(substring);
								if (match.find()){
									//System.out.println("S1");
									System.out.println("I found the text " + match.group()+" starting at " +	
											"index "+ match.start()+" and ending at index "+match.end());
									flag1=true;
									/*System.out.println(match.start());
									System.out.println(match.end());
									System.out.println(arguments[i].length());*/		
									if(!(match.start()==0&&match.end()==substring.length()))
									{
									 valid=false;
									 flag1=false;
									 break;
									}
								}
								Matcher match1=arg.matcher(substring2);
								if(match1.find()){
									//System.out.println("S2");
									//System.out.println(substring2);
									System.out.println(match1.group()+" starting at " +	
											"index "+ match1.start()+" and ending at index "+match1.end());
									flag2=true;
									if(!(match1.start()==0&&match1.end()==substring2.length()-1))
									{
									 valid=false;
									 flag2=false;
									 break;
									}
								}
								if(flag1&&flag2){
									System.out.println("Valid Insert Query");
								}
								else{
									System.out.println("InValid Insert Query");
								}
								
						//		i++; }
						}		
						break;
					
					case "SELECT":
						if(query.contains("SDO_GEOM.SDO_AREA")){
							System.out.println("This is validation for Spatial Area query");
						//Pattern select_pattern = Pattern.compile("SELECT [a-zA-Z_,.\\s]* SDO_GEOM.SDO_AREA[(][a-zA-Z_,.0-9\\s]+[)] FROM [a-zA-Z_.\\s]+ WHERE [a-zA-Z_,.='\\s]+");
						//Pattern select_pattern=Pattern.compile("SELECT [[[a-zA-Z][a-zA-Z_]*]*[\\.]?[[a-zA-Z][a-zA-Z_]*]+[,]]*SDO_GEOM.SDO_AREA[(][[[a-zA-Z][a-zA-Z_]*]+[\\.][[a-zA-Z][a-zA-Z_]*]+][,][0-9]*[\\.]?[0-9]+[)] FROM [a-zA-Z_.\\s]+ WHERE [a-zA-Z_]+[\\.]?[a-zA-Z_]* [=] [a-zA-Z_']+[\\.]?[a-zA-Z_0-9']*");
						Pattern select_pattern=Pattern.compile("SELECT [([a-zA-Z][a-zA-Z_]*)*[\\.]?[[a-zA-Z][a-zA-Z_]*]+[,]]*SDO_GEOM.SDO_AREA[(][a-zA-z][a-zA-Z_,.0-9\\s]+[)] FROM [a-zA-Z_.\\s]+ WHERE [a-zA-Z_]+[\\.]?[a-zA-Z_]* [=] [a-zA-Z_']+[\\.]?[a-zA-Z_0-9']*");
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
						
						}	
					default:
						valid = false;
						break;
					}
					if (!valid) {
						//System.out.println("Invalid Line Input");
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
