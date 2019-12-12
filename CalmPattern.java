/*

   _____      _           _____      _   _                  
  / ____|    | |         |  __ \    | | | |                 
 | |     __ _| |_ __ ___ | |__) |_ _| |_| |_ ___ _ __ _ __  
 | |    / _` | | '_ ` _ \|  ___/ _` | __| __/ _ \ '__| '_ \ 
 | |___| (_| | | | | | | | |  | (_| | |_| ||  __/ |  | | | |
  \_____\__,_|_|_| |_| |_|_|   \__,_|\__|\__\___|_|  |_| |_|
  
The file contains the code that modifies the song object to include a calm melody and chord progression.
This includes the possible chord progressions, how the notes and rhythms of the melody are determined,
and how the pieces are fit together, in what patterns.

*/

import java.util.ArrayList;
import java.util.Collections;

public class CalmPattern {
   Voice melody;
   Voice chords;
   Voice percussion;
   String key;
   String[] keyNotes;
   String[] allowedNotes;
   int measureLength = 0;
   
	// Empty constructor
   public CalmPattern(String k, String[] kn, String[] an, String lead) {
      key = k;
      keyNotes = kn;
      allowedNotes = an;
      melody = new Voice(lead);
      chords = new Voice("Piano");
      int percussiveDecider = (int)(Math.random()*3);
      if(percussiveDecider == 0)
         percussion = new Voice("Tubular_Bells");
      else if(percussiveDecider == 1)
         percussion = new Voice("Bass_Lead");
      else
         percussion = new Voice("Fifths");
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
            if(noteLetters[j].substring(0,1).equals(allowedNotes[i].substring(0,1))) {
               valid.add(allowedNotes[i]);
               j = noteLetters.length+1;
            }
         }
      }
      return valid;
   }
	
   private String[] createSingleMeasure(Chords progression) {
      String[] phrase;
   	
   	// Source and destination - no relation to each other
   	
   	// Have set number of notes to reach destination: 2 to 6
   // 1(10%): rw 4(40%): q rh i i | rh ri i q | h q i i | q rh i i 5(35%): i h i i i | q ri i q q 6(15%): i q i i i q
      int numNotes;
      int noteDecider = (int)(Math.random()*100);
      if(noteDecider >= 90) 
         numNotes = 1;
      else if(noteDecider >= 50)
         numNotes = 4;
      else if(noteDecider >= 15)
         numNotes = 5;
      else
         numNotes = 6;
   
      phrase = new String[numNotes];
      for(int i = 0; i < phrase.length; i++) {
         phrase[i] = ""; // just init
      }
     
     
   	// Valid source and destination notes - dest within 2 of source
      ArrayList<String> validChordTones = GetAllowedChordTones(progression);
      int sourceDecider = (int)(Math.random()*validChordTones.size());
      String source = validChordTones.get(sourceDecider);
      
      int addDecider = (int)(Math.random()*2);
      int amountDecider = (int)(Math.random()*3);
      
      int destDecider;
      if(addDecider == 0)
         destDecider = sourceDecider - amountDecider;
      else
         destDecider = sourceDecider + amountDecider;
      if(destDecider < 0)
         destDecider = 0;
      if(destDecider > validChordTones.size()-1)
         destDecider = validChordTones.size()-1;
      
      String destination = validChordTones.get(destDecider);
      
      int optionDecider;
      
      // If 0 notes: whole rest
      if(numNotes == 1) {
         phrase[0] = "Rw";
      }
      // 4(40%): q rh i i | rh ri i q | h q i i | i i q h
      // If four notes: Four options
      else if(numNotes == 4) {
         optionDecider = (int)(Math.random()*4);
         if(source.equals(destination) && sourceDecider != 0) {
         // source - rest - chordtonedown - destination
            phrase[0] = source+"q";
            phrase[1] = "Rh";
            phrase[2] = validChordTones.get(destDecider-1);
            phrase[3] = destination+"q";
         }
         else if(sourceDecider+1 != validChordTones.size() && sourceDecider != 0) {
         // source h - skipup/skipdownq - stepup/stepdowni - destinationi
            int upDecider1 = (int)(Math.random()*2);
            int upDecider2 = (int)(Math.random()*2);
            phrase[0] = source+"h";
            if(upDecider1 == 0)
               phrase[1] = validChordTones.get(sourceDecider+1)+"q";
            else 
               phrase[1] = validChordTones.get(sourceDecider-1)+"q";
            if(upDecider2 == 0)
               phrase[2] = GetStepUp(source)+"i";
            else
               phrase[2] = GetStepDown(source)+"i";
            phrase[3] = destination+"i";
         }
         else if(optionDecider != 0) {
         // source i - stepupi-stepupq / stepdowni-stepdownq - opposite h
            int upDecider = (int)(Math.random()*2);
            phrase[0] = source+"i";
            if(upDecider == 0) {
               phrase[1] = GetStepUp(source)+"i";
               phrase[2] = GetStepUp(GetStepUp(source))+"q";
               phrase[3] = GetStepUp(source)+"h";
            }
            else {
               phrase[1] = GetStepDown(source)+"i";
               phrase[2] = GetStepDown(GetStepDown(source))+"q";
               phrase[3] = GetStepDown(source)+"h";
            }
         }
         else {
         // rest - rest - source - destination
            phrase[0] = "Rh";
            phrase[1] = "Ri";
            phrase[2] = source+"i";
            phrase[3] = destination+"q";
         }
      }
      // If 5 notes:
      // 5(35%): i h i i i | q ri i q q
      // Option 1: sourcei - skip downh - repeati - sourcei - chordtoneup
      // Option 2: sourceq - resti - destfourthupi - deststepupq - destq
      else if(numNotes == 5) {
         optionDecider = (int)(Math.random()*2);
         if(sourceDecider != validChordTones.size()-1 && optionDecider == 0) {
            phrase[0] = source+"q";
            phrase[1] = GetStepDown(GetStepDown(source))+"h";
            phrase[2] = GetStepDown(GetStepDown(source))+"i";
            phrase[3] = source+"i";
            phrase[4] = validChordTones.get(sourceDecider+1);
         }
         else {
            phrase[0] = source+"q";
            phrase[1] = "Ri";
            phrase[2] = GetStepUp(GetStepUp(GetStepUp(destination)))+"i";
            phrase[3] = GetStepUp(destination)+"q";
            phrase[4] = destination+"q";
         }
      }
      // If 6 notes:
      // 6(15%): i q i i i q
      // Option 1: sourcei - skipdownq - repeati - stepupi - stepdowni - skipdownq
      // Option 2: sourcei - skipupq - repeati - stepdowni - stepupi - skipupq
      else /*if(numNotes == 6) */ {
         optionDecider = (int)(Math.random()*2);
         phrase[0] = source+"i";
         if(optionDecider == 0) {
            phrase[1] = GetStepDown(GetStepDown(source))+"q";
            phrase[2] = GetStepDown(GetStepDown(source))+"i";
            phrase[3] = GetStepDown(source)+"i";
            phrase[4] = phrase[2];
            phrase[5] = GetStepDown(GetStepDown(GetStepDown(GetStepDown(source))))+"q";
         }
         else {
            phrase[1] = GetStepUp(GetStepUp(source))+"q";
            phrase[2] = GetStepUp(GetStepUp(source))+"i";
            phrase[3] = GetStepUp(source)+"i";
            phrase[4] = phrase[2];
            phrase[5] = GetStepUp(GetStepUp(GetStepUp(GetStepUp(source))))+"q";
         }
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
     // For calm, use arpeggios
      String chordMeasure;
      ArrayList<Integer> measureNums = new ArrayList<Integer>();
      measureNums.add(1); measureNums.add(2); measureNums.add(3); measureNums.add(0);
      Collections.shuffle(measureNums);
      for(int i = 0; i < progression.length; i++) {
         ArrayList<String> chordNotes = GetAllowedChordTones(progression[i]);
         chordMeasure = "";
         int thisMeasureChoice = measureNums.get(i);
      // Will use one of each:
      // Arpeggiate in a 1i 2i 3i 4i 3i 2i 1q pattern
         if(thisMeasureChoice == 0) {
            chordMeasure = chordMeasure+chordNotes.get(0)+"i "+chordNotes.get(1)+"i "+chordNotes.get(2)+"i "+chordNotes.get(3)+"i "+chordNotes.get(2)+"i "
               +chordNotes.get(1)+"i "+chordNotes.get(0)+"q ";
         }
         // Or in a 1i 1o+i ctdowni ctdownq skipdi stepui skipdq
         else if(thisMeasureChoice == 1) {
            int octaveUp = Integer.parseInt(chordNotes.get(0).substring(chordNotes.get(0).length()-1))+1;
            String secondNote = chordNotes.get(0).substring(0,1)+octaveUp;
            String thirdNote = GetStepDown(GetStepDown(secondNote));
            String fourthNote = GetStepDown(GetStepDown(thirdNote));
            String fifthNote = GetStepDown(GetStepDown(fourthNote));
            chordMeasure = chordMeasure+chordNotes.get(0)+"i "+secondNote+"i "+thirdNote+"i "+fourthNote+"q "+fifthNote+"i "+GetStepUp(fifthNote)+"i "
               +GetStepDown(fifthNote)+"q ";
         }
         // Or in a 2i 3i 1i 2i 3i 2i r
         else if(thisMeasureChoice == 2) {
            chordMeasure = chordMeasure+chordNotes.get(1)+"i "+chordNotes.get(2)+"i "+chordNotes.get(0)+"i "+chordNotes.get(1)+"i "+chordNotes.get(2)+"i "
               +chordNotes.get(1)+"i "+"Rq ";
         }
         // 1i 2i 3i 1i 3i 2i 1q
         else {
            chordMeasure = chordMeasure+chordNotes.get(0)+"i "+chordNotes.get(1)+"i "+chordNotes.get(2)+"i "+chordNotes.get(0)+"i "+chordNotes.get(2)+"i "
               +chordNotes.get(1)+"i "+chordNotes.get(0)+"q ";
         }
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
        // p.addNotes(b);
        // p.addNotes(c);
        // p.addNotes(a);
         phraseLength[i] = measureLength; // * 2;
         melodyPhrases[i] = p;
      }
   	// Add phrases to the voice - ABAC pattern
     // melody.addPhrase(melodyPhrases[0]);
     // melody.addPhrase(melodyPhrases[1]);
      melody.addPhrase(melodyPhrases[0]);
     // melody.addPhrase(melodyPhrases[2]);
   	
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
     
     // Create the percussion accompaniment - blank for calm
     Phrase blankPercussion = new Phrase();
      for(int i = 0; i < 4; i++)
         blankPercussion.addNotes(fourMeasureBlanks);
      percussion.addPhrase(blankPercussion);
     
      song.addVoice(percussion);

    /*ArrayList<String> perMeasures = makePercussionNotes(baseProgression);
    for(int i = 0; i < measuresOfChords; i++) {
   	Phrase p = new Phrase();
   	p.addNotes(perMeasures);
   	percussion.addPhrase(p);
    }
    
    song.addVoice(percussion);*/
   }
}