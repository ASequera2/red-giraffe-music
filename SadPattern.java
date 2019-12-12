/*

   _____           _ _____      _   _                  
  / ____|         | |  __ \    | | | |                 
 | (___   __ _  __| | |__) |_ _| |_| |_ ___ _ __ _ __  
  \___ \ / _` |/ _` |  ___/ _` | __| __/ _ \ '__| '_ \ 
  ____) | (_| | (_| | |  | (_| | |_| ||  __/ |  | | | |
 |_____/ \__,_|\__,_|_|   \__,_|\__|\__\___|_|  |_| |_|
                                                       
                                                       
The file contains the code that modifies the song object to include a calm melody and chord progression.
This includes the possible chord progressions, how the notes and rhythms of the melody are determined,
and how the pieces are fit together, in what patterns.

*/


import java.util.ArrayList;

public class SadPattern {
   Voice melody;
   Voice chords;
   Voice percussion;
   String key;
   String[] keyNotes;
   String[] allowedNotes;
   int measureLength = 0;
   
	// Empty constructor
   public SadPattern(String k, String[] kn, String[] an, String lead) {
      key = k;
      keyNotes = kn;
      allowedNotes = an;
      melody = new Voice(lead);
      chords = new Voice("Piano");
      percussion = new Voice("Tubular_Bells");
   }
   
   // Enum which contains all the chord types, which can be used to determine chords regardless of key.
   private enum Chords { OneMinor, TwoDiminished, ThreeMajor, FourMinor, FiveMajor, SixMajor; }
   
   // Method to get what the next chord should be, given that the previous chord was.
   private Chords getNextChord(Chords prevChord) {
    // Viable chords: 2, 3, 4, 5, 6
      if(prevChord == Chords.OneMinor) {
         int decider = (int)(Math.random() * 5);
         if(decider == 0)
            return Chords.TwoDiminished;
         else if(decider == 1)
            return Chords.ThreeMajor;
         else if(decider == 2)
            return Chords.FourMinor;
         else if(decider == 3)
            return Chords.FiveMajor;
         else // if(decider == 4)
            return Chords.SixMajor;
      }
      // Viable chords: 3, 5
      else if(prevChord == Chords.TwoDiminished) {
         int decider = (int)(Math.random()*2);
         if(decider == 0)
            return Chords.FiveMajor;
         else // if(decider == 1
            return Chords.ThreeMajor;
      }
      // Viable chords: 4, 6
      else if(prevChord == Chords.ThreeMajor) {
         int decider = (int)(Math.random()*2);
         if(decider == 0)
            return Chords.FourMinor;
         else // if(decider == 1
            return Chords.SixMajor;
      }
      // Viable chords: 1, 2, 5
      else if(prevChord == Chords.FourMinor) {
         int decider = (int)(Math.random()*3);
         if(decider == 0)
            return Chords.FiveMajor;
         else if(decider == 1)
            return Chords.OneMinor;
         else // if(decider == 2
            return Chords.TwoDiminished;
      }
      // Viable chords: 1
      else if(prevChord == Chords.FiveMajor) {
         return Chords.OneMinor;
      }
      // Viable chords: 2, 4
      else /*if(prevChord == SixMajor*/ {
         int decider = (int)(Math.random()*2);
         if(decider == 0)
            return Chords.TwoDiminished;
         else // if(decider == 1
            return Chords.FourMinor;
      }
   }
      
   // Returns a string of notes, in jfugue notation, that can be played for the chord
   // Important extension: This can eventually be modified such that a key is also a parameter.
   // Currently uses the jfugue notation, which means major chords use root, fifth, and octive.
   private String getChordNotes(Chords chord) {
      Chords[] Choices = {Chords.OneMinor, Chords.TwoDiminished, Chords.ThreeMajor, Chords.FourMinor, Chords.FiveMajor, Chords.SixMajor};
   // D minor scale: D E F G A Bb C D
      String[] DChords = {"Dmin", "Edim", "Fmaj", "Gmin", "Amaj", "Bbmaj"};
   // A minor Scale: A B C D E F G
      String[] AChords = {"Amin", "Bdim", "Cmaj", "Dmin", "Emaj", "Fmaj"};
   
      for(int i = 0; i < Choices.length; i++) {
         if(chord == Choices[i]) {
            if(key.equals("D"))
               return DChords[i];
            else
               return AChords[i];
         }
      }
      return "";
   }
   
   // Returns all three notes of the tritone of the chord, in order of root, skip, fifth.
   private String[] getChordTones(Chords measureChord) {
      Chords[] Choices = {Chords.OneMinor, Chords.TwoDiminished, Chords.ThreeMajor, Chords.FourMinor, Chords.FiveMajor, Chords.SixMajor};
     
      String[] tones = new String[3];
      for(int i = 0; i < Choices.length; i++) {
         if(Choices[i] == measureChord) {
            tones[0] = keyNotes[i];
            tones[1] = keyNotes[(i+2)%keyNotes.length];
            tones[2] = keyNotes[(i+4)%keyNotes.length];
         }
      }
      return tones;
   }

	// Generate chords for a specific type of chord progression, which will generate four chords,
	// with the last chord always being the I chord.
   private Chords[] GenerateFourChordProgression() {
      Chords[] progression = new Chords[4];
   	// First chord is random and not OneMinor or FiveMajor.
      int decider = (int)(Math.random()*4);
      if(decider == 0)
         progression[0] = Chords.TwoDiminished;
      else if(decider == 1)
         progression[0] = Chords.ThreeMajor;
      else if(decider == 2)
         progression[0] = Chords.FourMinor;
      else
         progression[0] = Chords.SixMajor;
   	// Second chord depends on the first chord, but must be 2, 3, 4, or 6
      boolean resolved = false;
      Chords secondChord;
      while(!resolved) {
         secondChord = getNextChord(progression[0]);
         if(secondChord == Chords.TwoDiminished || secondChord == Chords.ThreeMajor || secondChord == Chords.SixMajor || secondChord == Chords.FourMinor) {
            resolved = true;
            progression[1] = secondChord;
         }
      }
   	// Third chord depends on second, but must be 4 or 5
      resolved = false;
      Chords thirdChord;
      while(!resolved) {
         thirdChord = getNextChord(progression[1]);
         if(thirdChord == Chords.FourMinor || thirdChord == Chords.FiveMajor) {
            resolved = true;
            progression[2] = thirdChord;
         }
      }
   	// Fourth chord is 1 always.
      progression[3] = Chords.OneMinor;
      return progression;
   }
   
	
   private String GetStepUp(String note) {
      for(int i = 0; i < allowedNotes.length; i++) {
         if(note.equals(allowedNotes[i])) {
            if(i == allowedNotes.length-1) {
               return allowedNotes[i];
            }
            else
               return allowedNotes[i+1];
         }
      }
      return "";
   }
   private String GetStepDown(String note) {
      for(int i = 0; i < allowedNotes.length; i++) {
         if(note.equals(allowedNotes[i])) {
            if(i == 0) {
               return allowedNotes[i];
            }
            else
               return allowedNotes[i-1];
         }
      }
      return "";
   }
	
	// Returns the valid notes which are chord tones
   private ArrayList<String> GetAllowedChordTones(Chords chord) {
      String[] noteLetters = getChordTones(chord);
      ArrayList<String> valid = new ArrayList<String>();
      for(int i = 0; i < allowedNotes.length; i++) {
         for(int j = 0; j < noteLetters.length; j++) {
         	// If the letter equals the first character of the allowed notes
            if(noteLetters[j].equals(allowedNotes[i].substring(0,1))) {
               valid.add(allowedNotes[i]);
               j = noteLetters.length+1;
            }
         }
      }
      return valid;
   }
	
   private String[] createSingleMeasure(Chords progression) {
      String[] phrase;
   	
   	// Source and destination, source must be lower than the destination
   	
   	// Have set number of notes to reach destination: 1 to 4
   // 1(5%): w 2(15%): h h 3(30%): h q q 4(50%): q q q q
      int numNotes;
      int noteDecider = (int)(Math.random()*100);
      if(noteDecider >= 50) 
         numNotes = 4;
      else if(noteDecider >= 20)
         numNotes = 3;
      else if(noteDecider >= 5)
         numNotes = 2;
      else
         numNotes = 1;
   
      phrase = new String[numNotes];
      for(int i = 0; i < phrase.length; i++) {
         phrase[i] = ""; // just init
      }
     
     
   	// Valid source and destination notes
      ArrayList<String> validChordTones = GetAllowedChordTones(progression);
      int sourceDecider = (int)(Math.random()*validChordTones.size()/2)+(validChordTones.size()/2);
      String source = validChordTones.get(sourceDecider);
      
      int destDecider = (int)(Math.random()*(int)validChordTones.size()/2);
      String destination = validChordTones.get(destDecider);
      
      // If 1 note: just play source as a whole note
      if(numNotes == 1) {
         phrase[0] = source+"w";
      }
      // If two notes: play source and destination as two half notes
      else if(numNotes == 2) {
         phrase[0] = source+"h";
         phrase[1] = destination+"h";
      }
      // If three notes: either h q q or q q h
      // If h q q(35%): source - step down (65%) or chord tone down (35%), destination
      // If q q h(65%): source - step up (20%) or down (80%) - destination
      else if(numNotes == 3) {
         int halfDecider = (int)(Math.random()*20);
         if(halfDecider >= 6) {
         // q q h
            phrase[0] = source+"q";
            int nextDecider = (int)(Math.random()*20);
            if(nextDecider >= 6) {
            // Step down
               phrase[1] = GetStepDown(source)+"q";
            }
            else {
            // Chord tone down
               phrase[1] = validChordTones.get(sourceDecider-1)+"q";
            }
            phrase[2] = destination+"h";
         }
         else  {
         // h q q
            phrase[0] = source+"h";
            int moarDeciders = (int)(Math.random()*5);
            if(moarDeciders == 0) {
            // Step up
               phrase[1] = GetStepUp(source);
            }
            else {
            // Step down
               phrase[1] = GetStepDown(source);
            }
            phrase[2] = destination+"q";
         }
      }
      // If four notes: source - step up step down (30%) / step down step down (70%) - dest
      else /* if(numNotes == 4) */ {
         int flipDecider = (int)(Math.random()*10);
         phrase[0] = source+"q";
         if(flipDecider <= 2) {
         // Step up step down
            phrase[1] = GetStepUp(source)+"q";
            phrase[2] = GetStepDown(source)+"q";
         }
         else {
         // step down step down 
            phrase[1] = GetStepDown(source)+"q";
            String prevNote = GetStepDown(source); // Without this everything breaks. Thanks for the 30 minutes of debugging fun.
            phrase[2] = GetStepDown(prevNote)+"q";
         }
         phrase[3] = destination+"q";
      }
   	
      return phrase;
   }
	
	// Creates a single 4 or 8 measure phrase.
   private ArrayList<String> generateSmallPhrase(Chords[] progression) {
      ArrayList<String> measureNotes = new ArrayList<String>();
      for(int i = 0; i < 4; i++) {
         String[] thisMeasure = createSingleMeasure(progression[i]);
         /*System.out.print("Print single measure:");
         for(int j = 0; j < thisMeasure.length; j++) {
            System.out.print(thisMeasure[j]);
         }
         System.out.println("");*/
         for(int j = 0; j < thisMeasure.length; j++) {
            measureNotes.add(thisMeasure[j]);
         }
         measureLength++;
      }
     
     // Check if it should be extended - tabled until after demo.
     
      return measureNotes;
   }
	
   private ArrayList<String> makeChordNotes(Chords[] progression) {
      ArrayList<String> measure = new ArrayList<String>();
      String chordMeasure;
      for(int i = 0; i < progression.length; i++) {
         chordMeasure = "";
        // Whole notes as chord or half note root, half note chord
         String chord = getChordNotes(progression[i]);
         String chordRoot = chord.charAt(0)+"";
         int rhythmDecider = (int)(Math.random()*2);
         if(rhythmDecider == 0)
            chordMeasure = chord+"w ";
         else
            chordMeasure = chord+"h "+chordRoot+"h ";
         measure.add(chordMeasure);
      }
      return measure;
   }
   
   private ArrayList<String> makePercussionNotes(Chords[] progression) {
      ArrayList<String> percus = new ArrayList<String>();
      for(int i = 0; i < progression.length; i++) {
         String chord = getChordNotes(progression[i]);
         percus.add(chord.charAt(0)+"w");
      }
      return percus;
   }
	
    // Method to play the melody that is created
   public void generatePattern(Song song) {
      Chords[] baseProgression = GenerateFourChordProgression();
     
      Phrase[] melodyPhrases = new Phrase[3];
      int[] phraseLength = new int[3];
   	// Create melody based on chord progressions
   	// Measure phrases
      for(int i = 0; i < 3; i++) {
         measureLength = 0;
         ArrayList<String> a = generateSmallPhrase(baseProgression);
         /*System.out.println("First phrase: ");
         for(int j = 0; j < a.size(); j++) {
            System.out.print(a.get(j));
         }
         System.out.println("");*/
         ArrayList<String> b = generateSmallPhrase(baseProgression);
         ArrayList<String> c = generateSmallPhrase(baseProgression);
        
      	// ABCBCA pattern
         Phrase p = new Phrase(a);
         p.addNotes(b);
         p.addNotes(c);
         //p.addNotes(b);
         //p.addNotes(c);
         //p.addNotes(a);
         phraseLength[i] = measureLength; // * 2;
         melodyPhrases[i] = p;
      }
   	// Add phrases to the voice - ABAC pattern
      melody.addPhrase(melodyPhrases[0]);
      //melody.addPhrase(melodyPhrases[1]);
      //melody.addPhrase(melodyPhrases[0]);
      //melody.addPhrase(melodyPhrases[2]);
      
      // Add four beats of rests to the melody to balance out chords.
     Phrase blankMelody = new Phrase();
     String[] fourMeasureBlanks = {"Rw Rw Rw Rw"};
     blankMelody.addNotes(fourMeasureBlanks);
     melody.addPhrase(blankMelody);
     
     // Create the chord voice
      song.addVoice(melody);
     
     // Create the chord voice
      //int measuresOfChords = (phraseLength[0]*2) + phraseLength[1] + phraseLength[2];
      int measuresOfChords = phraseLength[0];
      ArrayList<String> fourMeasures = makeChordNotes(baseProgression);
     // Repeat fourMeasures measuresOfChords times
      for(int i = 0; i < measuresOfChords/3; i++) {
         Phrase p = new Phrase();
         p.addNotes(fourMeasures);
         chords.addPhrase(p);
      }
     
      song.addVoice(chords);
     
     // Create the percussion accompaniment
      ArrayList<String> perMeasures = makePercussionNotes(baseProgression);
      for(int i = 0; i < measuresOfChords/3; i++) {
         Phrase p = new Phrase();
         p.addNotes(perMeasures);
         percussion.addPhrase(p);
      }
    
      song.addVoice(percussion);
   }
}