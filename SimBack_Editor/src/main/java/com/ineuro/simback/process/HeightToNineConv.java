package com.ineuro.simback.process;

public class HeightToNineConv {

	public static boolean isLocalNumber(String numero) {
		boolean result = false;
		
		/* Détermination de la présence d'un indicatif */
		if( numero.startsWith("00") || numero.startsWith("+") ) {
			// Il s'agit ici de déterminer si l'indicatif est celui du Cameroun
			if( numero.startsWith("00237") || numero.startsWith("+237") )
				result = true;
			else
				result = false;
		} else 
			result = true;		// L'absence d'indicatif connote la nature locale du numero
		return result;
	}
	
	public static boolean isConvertible(String numero) {
		return isLocalNumber(numero) && getLocalNumberWithoutIndicator(numero).length() == 8;
	}
	
	public static boolean hasIndicator(String numero) {
		return numero.startsWith("00") || numero.startsWith("+");
	}
	
	public static String getLocalIndicator(String numero) {
		String prefix = "";

		if( numero.startsWith("00237") )
			prefix = "00237";
		if( numero.startsWith("+237") )
			prefix = "+237";
		return prefix;
	}
	
	public static String getLocalNumberWithoutIndicator(String numero) {
		String workingNumber = null;
		
		if( isLocalNumber(numero) )
			if( hasIndicator(numero) ) {
				if( numero.startsWith("00237") )
					workingNumber = numero.substring(5);
				if( numero.startsWith("+237") )
					workingNumber = numero.substring(4);
			} else
				workingNumber = numero.substring(0);
		return workingNumber;
	}
	
	public static String convHeightToNine(String origNum) {
		String destNum = null, workNum = null, prefix = getLocalIndicator(origNum);
		
		if( isLocalNumber(origNum) ) {
			workNum = getLocalNumberWithoutIndicator(origNum);
			
			if(workNum.length() == 8) {
				char start = workNum.charAt(0), newStart = 'u';
				
				/* Détermination du chiffre à ajouter */
				switch (start) {
				case '2':
//					newStart = '2';
//					break;
					
				case '3':
					newStart = '2';
					destNum = prefix
							.concat(newStart=='u' ? "" : "24")
							.concat(workNum.substring(1));
					break;

				case '5':

				case '7':

				case '9':
					newStart = '6';
					destNum = prefix
							.concat(newStart=='u' ? "" : "" + newStart)
							.concat(workNum);
					break;
				}
//				destNum = prefix
//						.concat(newStart=='u' ? "" : "" + newStart)
//						.concat(workNum);
			} else
				destNum = origNum.substring(0);
		} else
			destNum = origNum.substring(0);
		
//		System.out.println("HeightToNineConv.convHeightToNine() Original : " + origNum + " >>> " + destNum);
		return destNum;
	}
	
	public static void main(String[] args) {
		/* Declarations of Samples Numbers to use for testing
		 *  the migration to Nine Numbers System */
		String[] sampleNumbers = {
				"24512486",
				"33102236",
				"+23726531584",
				"915",				// Expected Result	:	"915"
				"+24548756",		// Expected Result	:	"+24548756"
				"+2485",			// Expected Result	:	"+2485"
				"004856",			// Expected Result	:	"004856"
				"99986355",			// Expected Result	:	"699986355"
				"0023799156637",	// Expected Result	:	"00237699156637"
				"+23777564852",		// Expected Result	:	"+237677564852"
				"622354215",		// Expected Result	:	"677854215"
				"00237677854215",	// Expected Result	:	"00237677854215"
				"+237677854215",	// Expected Result	:	"+237677854215""
				""					// Expected Result	:	""
		};
		
		System.out.println("Test de la classe de conversion des numéros du Cameroun de 8 à 9 chiffres :");
		/** Passage au cribble des numeros échantillons **/
		for(String number : sampleNumbers) {
			String result = convHeightToNine(number);
			
			System.out.println("La Conversion de " + number + " a donné " + result );
//			System.out.println("La Conversion de " 
//					+ null!=number ? number : "null" 
//					+ " a donné "
//					+ null!=result ? result : number);
		}
		System.out.println("### *** FIN DU TEST *** ###");
	}
}
