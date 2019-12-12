/*

  _    _                         _____      _   _                  
 | |  | |                       |  __ \    | | | |                 
 | |__| | __ _ _ __  _ __  _   _| |__) |_ _| |_| |_ ___ _ __ _ __  
 |  __  |/ _` | '_ \| '_ \| | | |  ___/ _` | __| __/ _ \ '__| '_ \ 
 | |  | | (_| | |_) | |_) | |_| | |  | (_| | |_| ||  __/ |  | | | |
 |_|  |_|\__,_| .__/| .__/ \__, |_|   \__,_|\__|\__\___|_|  |_| |_|
              | |   | |     __/ |                                  
              |_|   |_|    |___/                                  
  
The file contains the code that modifies the song object to include a calm melody and chord progression.
This includes the possible chord progressions, how the notes and rhythms of the melody are determined,
and how the pieces are fit together, in what patterns.

*/

import java.util.ArrayList;
import java.util.Collections;

public class HappyPattern implements PatternRuleSet {
   Voice melody;
   Voice chords;
   Voice percussion;
   String key;
   String[] keyNotes;
   String[] allowedNotes;
   int measureLength = 0;
   
   // Empty constructor
   public HappyPattern(String k, String[] kn, String[] an, String lead) {
      key = k;
      keyNotes = kn;
      allowedNotes = an;
   	/*if(k.equals("C")) {
         keyNotes{"C","D","E","F","G","A","B"};
   		allowedNotes = {"G4","A5","B5","C5","D5","E5","F5","G5,","A6","B6","C6","D6","E6","F6","G6"};
   	}
   	else {
   		keyNotes = {"G","A","B","C","D","E","F#"};
   		allowedNotes = {"D5","E5","F#5","G5","A6","B6","C6","D6","E6","F#6","G6","A7","B7","C7","D7"};
   	}*/
      melody = new Voice(lead);
      chords = new Voice("Piano");
      percussion = new Voice("Tubular_Bells");
   }
   
   // Enum which contains all the chord types, which can be used to determine chords regardless of key.
   private enum Chords { OneMajor, TwoMinor, ThreeMinor, FourMajor, FiveMajor, SixMinor; }
   
   // Method to get what the next chord should be, given that the previous chord was.
   private Chords getNextChord(Chords prevChord) {
    // Viable chords: 2, 3, 4, 5, 6
      if(prevChord == Chords.OneMajor) {
         int decider = (int)(Math.random() * 5);
         if(decider == 0)
            return Chords.TwoMinor;
         else if(decider == 1)
            return Chords.ThreeMinor;
         else if(decider == 2)
            return Chords.FourMajor;
         else if(decider == 3)
            return Chords.FiveMajor;
         else // if(decider == 4)
            return Chords.SixMinor;
      }
      // Viable chords: 3, 5
      else if(prevChord == Chords.TwoMinor) {
         int decider = (int)(Math.random()*2);
         if(decider == 0)
            return Chords.FiveMajor;
         else // if(decider == 1
            return Chords.ThreeMinor;
      }
      // Viable chords: 4, 6
      else if(prevChord == Chords.ThreeMinor) {
         int decider = (int)(Math.random()*2);
         if(decider == 0)
            return Chords.FourMajor;
         else // if(decider == 1
            return Chords.SixMinor;
      }
      // Viable chords: 1, 2, 5
      else if(prevChord == Chords.FourMajor) {
         int decider = (int)(Math.random()*3);
         if(decider == 0)
            return Chords.FiveMajor;
         else if(decider == 1)
            return Chords.OneMajor;
         else // if(decider == 2
            return Chords.TwoMinor;
      }
      // Viable chords: 1, 3, 6
      else if(prevChord == Chords.FiveMajor) {
         int decider = (int)(Math.random()*3);
         if(decider == 0)
            return Chords.OneMajor;
         else if(decider == 1)
            return Chords.ThreeMinor;
         else // if(decider == 2
            return Chords.SixMinor;
      }
      // Viable chords: 2, 4
      else /*if(prevChord == SixMinor*/ {
         int decider = (int)(Math.random()*2);
         if(decider == 0)
            return Chords.TwoMinor;
         else // if(decider == 1
            return Chords.FourMajor;
      }
   }
      
   // Returns a string of notes, in jfugue notation, that can be played for the chord
   // Important extension: This can eventually be modified such that a key is also a parameter.
   // Currently uses the jfugue notation, which means major chords use root, fifth, and octive.
   private String getChordNotes(Chords chord) {
      Chords[] Choices = {Chords.OneMajor, Chords.TwoMinor, Chords.ThreeMinor, Chords.FourMajor, Chords.FiveMajor, Chords.SixMinor};
   // C scale: C D E F G A B
      String[] CChords = {"Cmaj", "Dmin", "Emin", "Fmaj", "Gmaj", "Amin"};
   // G Scale: G A B C D E F#
      String[] GChords = {"Gmaj", "Amin", "Bmin", "Cmaj", "Dmaj", "Emin"};
   
      for(int i = 0; i < Choices.length; i++) {
         if(chord == Choices[i]) {
            if(key.equals("C"))
               return CChords[i];
            else
               return GChords[i];
         }
      }
      return "";
   }
   
   // Returns all three notes of the tritone of the chord, in order of root, skip, fifth.
   private String[] getChordTones(Chords measureChord) {
      Chords[] Choices = {Chords.OneMajor, Chords.TwoMinor, Chords.ThreeMinor, Chords.FourMajor, Chords.FiveMajor, Chords.SixMinor};
     
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

    // Generate a passing tone, given what the previous chord tone was.
   // Requires the previous note, as a position within the tritone, and the chord.
   // For example, if the previous chord was I in C major, and the position of the note was 3 (E)
   // then the possible passing tones are 4 and 2, and the next notes after that would be 5 and 1 respectively.
   // This method will return two notes together in JFugue format.
   // Example: Given 3, and that the current chord is TwoMinor, it will return either "E5 D5" or "G5 A5" 
   // Alternate:
   // Given the previous note and the next note, return the passing tone that is between them, if it exists.
   // If such a passing tone does not exist, return an empty string.
   private String getPassingTone(String prevNote, String nextNote) {
      String[] CMajor = {"C","D","E","F","G","A","B"};
     // Find which note prevNote is within the scale
      int prevNoteIndex = 0;
      for(int i = 0; i < CMajor.length; i++) {
         if(prevNote.equals(CMajor[i])) {
            prevNoteIndex = i;
            break;
         }
      }
     // check if the nextNote is two up or two down from the prev note
      if(CMajor[(prevNoteIndex+2)%7].equals(nextNote)) { // two notes up
         return CMajor[(prevNoteIndex+1)%7];
      }
      else if(CMajor[((prevNoteIndex-2)+7)%7].equals(nextNote)) { // two notes down
         return CMajor[((prevNoteIndex-1)+7)%7];
      }
      else { // No passing tone possible
         return "";
      }
   }

	// Generate chords for a specific type of chord progression, which will generate four chords,
	// with the last chord always being the I chord.
   private Chords[] GenerateFourChordProgression() {
      Chords[] progression = new Chords[4];
   	// First chord is random and not OneMajor.
      int decider = (int)(Math.random()*5);
      if(decider == 0)
         progression[0] = Chords.TwoMinor;
      else if(decider == 1)
         progression[0] = Chords.ThreeMinor;
      else if(decider == 2)
         progression[0] = Chords.FourMajor;
      else if(decider == 3)
         progression[0] = Chords.FiveMajor;
      else
         progression[0] = Chords.SixMinor;
   	// Second chord depends on the first chord, but must be 2, 3, 4, or 6
      boolean resolved = false;
      Chords secondChord;
      while(!resolved) {
         secondChord = getNextChord(progression[0]);
         if(secondChord == Chords.TwoMinor || secondChord == Chords.ThreeMinor || secondChord == Chords.SixMinor || secondChord == Chords.FourMajor) {
            resolved = true;
            progression[1] = secondChord;
         }
      }
   	// Third chord depends on second, but must be 4 or 5
      resolved = false;
      Chords thirdChord;
      while(!resolved) {
         thirdChord = getNextChord(progression[1]);
         if(thirdChord == Chords.FourMajor || thirdChord == Chords.FiveMajor) {
            resolved = true;
            progression[2] = thirdChord;
         }
      }
   	// Fourth chord is 1 always.
      progression[3] = Chords.OneMajor;
      return progression;
   }
   
	// Method to generate all the chord progressions at once, that way we can
	// know what the next chord is going to be before the measure has started.
	// This is necessary for passing tones at the start of measures.
   private Chords[] generateProgression() {
      Chords[] progression = new Chords[8];
     
      progression[1] = Chords.OneMajor;
     // First four and second four match
      for(int i = 1; i < 6; i++) {
         progression[i+1] = getNextChord(progression[i]); // Get next chord based on previous
      }
     
   	// 7th and 8th chords are special, to ensure that these chord progressions can chain together nicely,
   	// the 8th chord has to be a IV or V so it can resolve to a I.
   	// Thus the 7th chord must be something that can resolve to a IV or a V, 
   	// the chords which fit that description are: ii, iii, IV, vi.
   	// At least ONE of these chords is reachable from any position in the chord progression, therefore, for the
   	// 7th chord, we just keep generating chords until it fits with what we require. Same for the 8th chord.
      boolean resolved = false;
      while(resolved == false) {
         Chords seventhChord = getNextChord(progression[5]);
         if(seventhChord == Chords.TwoMinor || seventhChord == Chords.ThreeMinor || seventhChord == Chords.FourMajor || seventhChord == Chords.SixMinor) {
            resolved = true;
            progression[6] = seventhChord;
         }
      }
   	// Do the same for the 8th chord
      while(resolved == true) {
         Chords eighthChord = getNextChord(progression[6]);
         if(eighthChord == Chords.FourMajor || eighthChord == Chords.FiveMajor) {
            resolved = false;
            progression[7] = eighthChord;
         }
      }
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
   	
   	// Source and destination, source must be higher than the destination
   	
   	// Have set number of notes to reach destination: 4 to 8
   	// 4: qqqq, 5: qqqii, 6: qqiiii, 7: qiiiiii 8: iiiiiiii
      int numNotes = (int)(Math.random()*4)+4;
      phrase = new String[numNotes];
      for(int i = 0; i < phrase.length; i++) {
         phrase[i] = ""; // just init
      }
      boolean[] isEighth = {false, false, false, false};
      ArrayList<Integer> measureNums = new ArrayList<Integer>();
      measureNums.add(1); measureNums.add(2); measureNums.add(3); measureNums.add(0);
      Collections.shuffle(measureNums);
      for(int i = 0; i < numNotes-4; i++) {
         isEighth[measureNums.get(i)] = true;
      }
      int currNote = 0;
   	
   	// Donward movement is chord tone skips
   	// Upward movement is steps or skips
   	// First note: Any note on the lower half of the allowed notes, a chord tone
      ArrayList<String> validChordTones = GetAllowedChordTones(progression);
      
      int sourceDecider = (int)(Math.random()*(int)validChordTones.size()/2);
      String source = validChordTones.get(sourceDecider);
   	
      String destination = validChordTones.get(validChordTones.size()-1);
   	
      if(isEighth[0])
         phrase[currNote] = source+"i";
      else
         phrase[currNote] = source+"q";
      currNote++;
      if(isEighth[0] == true) {
      	// Can either step up (75%) or step down (25%)
      	// If as low as can be, step up only
         boolean stepUp = true;
         if(!source.equals("D5") && !source.equals("G4")) {
            int downDecider = (int)(Math.random()*4);
            if(downDecider == 0)
               stepUp = false;
         }
      	
         if(stepUp) {
            phrase[currNote] = GetStepUp(phrase[currNote-1].substring(0, phrase[currNote-1].length()-1))+"i";
         }
         else {
            phrase[currNote] = GetStepDown(phrase[currNote-1].substring(0, phrase[currNote-1].length()-1))+"i";
         }
         currNote++;
      }
   	// Second note: Chord tone down (25%), or steps up (75%)
   	// Check if the chord tone down exists
      boolean secondStepsUp = false;
      if(validChordTones.indexOf(source) != 0) { // can chord tone down
         int downDecider = (int)(Math.random()*4);
         if(downDecider != 0)
            secondStepsUp = true;
      }
      else
         secondStepsUp = true;
   	
      if(secondStepsUp) { // Steps upward
         if(isEighth[1]) {
         	// Two steps upward
            phrase[currNote] = GetStepUp(phrase[currNote-1].substring(0, phrase[currNote-1].length()-1))+"i";
            phrase[currNote+1] = GetStepUp(phrase[currNote].substring(0, phrase[currNote].length()-1))+"i";
            currNote = currNote + 2;
         }
         else {
         	// One step upward
            phrase[currNote] = GetStepUp(phrase[currNote-1].substring(0, phrase[currNote-1].length()-1))+"q";
            currNote++;
         }
      }
      else { // Chord tone down
         String chordDown = validChordTones.get(validChordTones.indexOf(source)-1);
         if(isEighth[1])
            phrase[currNote] = chordDown+"i";
         else
            phrase[currNote] = chordDown+"q";
         currNote++;
         if(isEighth[1]) { // Repeat if eighth
            phrase[currNote] = chordDown+"i";
            currNote++;
         }
      }
   	
   	// Third note: Chord tone, up from source
   	// If eighth, two chord tones upward, if possible 
   	// Check if two chord tones exist between source and destination
      if((validChordTones.indexOf(source)+2 <= validChordTones.size()-1) && isEighth[2] ) { // two chord tone leaps upward
      	// Can have two chord tone leaps
         phrase[currNote] = validChordTones.get(validChordTones.indexOf(source)+1)+"i";
         phrase[currNote+1] = validChordTones.get(validChordTones.indexOf(source)+2)+"i";
         currNote = currNote+2;
      }
      else {
         phrase[currNote] = validChordTones.get(validChordTones.indexOf(source)+1)+"q";
         currNote++;
      }
   	
   	// Fourth note: Destination, steps up or leap to
      if(isEighth[3]) {
         phrase[currNote] = GetStepDown(destination)+"i";
         phrase[currNote+1] = destination+"i";
         currNote = currNote+2;
      }
      else {
         phrase[currNote] = destination+"q";
         currNote++;
      }
   	
      return phrase;
   }
	
	// Creates a single 4 or 8 measure phrase.
   private ArrayList<String> generateSmallPhrase(Chords[] progression) {
      ArrayList<String> measureNotes = new ArrayList<String>();
      for(int i = 0; i < 4; i++) {
         String[] thisMeasure = createSingleMeasure(progression[i]);
         for(int j = 0; j < thisMeasure.length; j++) {
            measureNotes.add(thisMeasure[j]);
         }
         measureLength++;
      }
   	// Check if it needs to be extended - Cut for demo.
      /*char lastNote = measureNotes.get(measureNotes.size()-1).charAt(0);
      if(lastNote == keyNotes[4].charAt(0) || lastNote == keyNotes[1].charAt(0)) {
   	 System.out.println("Ever extended?");
      	// repeat with an ending of a 1
         measureLength++;
         if(key.equals("C")) {
         	// c key
            int curSize = measureNotes.size();
            for(int i = 0; i < curSize; i++) {
               measureNotes.add(measureNotes.get(i));
            }
            String oldRhythm1 = measureNotes.get(measureNotes.size()-1).substring(measureNotes.get(measureNotes.size()-1).length()-1);
            String oldRhythm2 = measureNotes.get(measureNotes.size()-2).substring(measureNotes.get(measureNotes.size()-2).length()-1);
            measureNotes.set(measureNotes.size()-1, "C6"+oldRhythm1);
            measureNotes.set(measureNotes.size()-2, GetStepDown(measureNotes.get(measureNotes.size()-1))+oldRhythm2);
         }
         else {
         	// g key
            String oldRhythm1 = measureNotes.get(measureNotes.size()-1).substring(measureNotes.get(measureNotes.size()-1).length()-1);
            String oldRhythm2 = measureNotes.get(measureNotes.size()-2).substring(measureNotes.get(measureNotes.size()-2).length()-1);
            measureNotes.set(measureNotes.size()-1, "G6"+oldRhythm1);
            measureNotes.set(measureNotes.size()-2, GetStepDown(measureNotes.get(measureNotes.size()-1))+oldRhythm2);
         }
      }*/
      return measureNotes;
   }
	
   private ArrayList<String> makeChordNotes(Chords[] progression) {
      ArrayList<String> measure = new ArrayList<String>();
      for(int i = 0; i < progression.length; i++) {
         String chord = getChordNotes(progression[i])+"q";
         String chordRoot = chord.charAt(0)+"q";
         String chordMeasure = chordRoot+" "+chord+" "+chordRoot+" "+chord+" ";
         measure.add(chordMeasure);
      }
      return measure;
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
         //p.addNotes(c);
         //p.addNotes(a);
         phraseLength[i] = measureLength; //* 2;
         melodyPhrases[i] = p;
         //melodyPhrases[i].setVolume(100, 50);
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
       //p.setVolume(100, 50);
         chords.addPhrase(p);
      }
     
      song.addVoice(chords);
     
     // Create the percussion accompaniment - Needs to be blank for matching purposes
      Phrase blankPercussion = new Phrase();
      for(int i = 0; i < 4; i++)
         blankPercussion.addNotes(fourMeasureBlanks);
      percussion.addPhrase(blankPercussion);
     
      song.addVoice(percussion);
     
   }

}