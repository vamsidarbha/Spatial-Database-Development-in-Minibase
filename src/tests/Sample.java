package tests;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sample {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
				String input = "I have a catdog, but I like my dog better.";

				Pattern p = Pattern.compile("I have a (mouse|cat|dog|wolf|bear|human), but I like my dog better");
				Matcher m = p.matcher(input);

				List<String> animals = new ArrayList<String>();
				while (m.find()) {
					System.out.println("Found a " + m.group() + ".");
					animals.add(m.group());
				}
			}
		

}
