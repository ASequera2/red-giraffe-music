/*

   _____                 _           _             _____                           _             
  / ____|               | |         (_)           |  __ \                         | |            
 | |     ___  _ __   ___| |_   _ ___ _  ___  _ __ | |  | | ___  ___ ___  _ __ __ _| |_ ___  _ __ 
 | |    / _ \| '_ \ / __| | | | / __| |/ _ \| '_ \| |  | |/ _ \/ __/ _ \| '__/ _` | __/ _ \| '__|
 | |___| (_) | | | | (__| | |_| \__ \ | (_) | | | | |__| |  __/ (_| (_) | | | (_| | || (_) | |   
  \_____\___/|_| |_|\___|_|\__,_|___/_|\___/|_| |_|_____/ \___|\___\___/|_|  \__,_|\__\___/|_|   
  
This file is a decorator which takes in a song object and adds a conclusion to that song based on
the key that the song is in. This is one of the decorators that can be applied to a song after it
has been modified by going through a pattern rule set.

*/

/*public class ConclusionDecorator implements SongRuleSet {
	String key;
	
	public ConclusionDecorator(String k) { key = k; };
	
	public Song generateSong(Song s) {
		String jfugueSong = s.GetSong();
		// Last measure will either have an C major, F major, or A minor chord.
		int decider = (int)(Math.random()*3);
		String lastMeasure = "";
		if(decider == 0)
			lastMeasure = "Cmajw+Cq_Eq_Gq_Cq";
		else if(decider == 1)
			lastMeasure = "Fmajw+Fq_Aq_Aq_Cq";
		else // if(decider == 2)
			lastMeasure = "Aminw+Eq_Gq_Aq_Cq";
		return new Song(jfugueSong+lastMeasure);
	}
}*/