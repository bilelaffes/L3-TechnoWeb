package services;

public class Operation {
	
	
	public double calcul(double a, double b, String operation){
		if (operation.equals("add"))
			return a+b;
		if (operation.equals("mul"))
			return a*b;
		if (operation.equals("div"))
			return a/b;
		else 
			return 0;
		
	}
}
