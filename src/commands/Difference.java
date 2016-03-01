package commands;

import java.util.List;

public class Difference extends Command implements Executable {

	public Difference() {
		numParams = 2;
	}
	
	public double execute(List<Object> params) {
		// need to figure out how to communicate with front-end
		double minuend = (Double) params.get(0);
		double subtrahend = (Double) params.get(1);
		return minuend - subtrahend;
	}
	
	public String checkParamTypes(List<Object> params) {
		for (Object param : params) {
			if (!(param instanceof Integer || param instanceof Double)) {
				return String.format(errors.getString("WrongParamType"), param.toString());
			}			
		}
		return null;
	}
	
	
}