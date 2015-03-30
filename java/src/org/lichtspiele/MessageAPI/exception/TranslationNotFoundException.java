package org.lichtspiele.MessageAPI.exception;

import java.io.IOException;

public class TranslationNotFoundException extends IOException {

	private static final long serialVersionUID = 3756571635311032998L;

	public TranslationNotFoundException(String string) {
		super(string);
	}	
	
}
