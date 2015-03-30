package org.lichtspiele.MessageAPI.exception;

import java.io.IOException;

public class TranslationFileNotFoundException extends IOException {

	private static final long serialVersionUID = 3756571635311032998L;

	public TranslationFileNotFoundException(String string) {
		super(string);
	}	
	
}
