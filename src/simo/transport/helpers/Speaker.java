package simo.transport.helpers;

import java.util.Locale;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

public class Speaker implements OnInitListener {

	private TextToSpeech tts;

	public Speaker(Context context) {
		tts = new TextToSpeech(context, this);
	}
	
	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			tts.setLanguage(Locale.ENGLISH);
		}
	}
	
	public void speak(String message, int mode) {
		tts.speak(message, mode, null);
	}
	
	public boolean isSpeaking() {
		return tts.isSpeaking();
	}
	
	public void shutdown() {
		tts.shutdown();
	}
}