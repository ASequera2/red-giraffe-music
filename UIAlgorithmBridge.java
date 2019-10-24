/* Class:
*   _    _ _____          _                  _ _   _               ____       _     _            
*  | |  | |_   _|   /\   | |                (_) | | |             |  _ \     (_)   | |           
*  | |  | | | |    /  \  | | __ _  ___  _ __ _| |_| |__  _ __ ___ | |_) |_ __ _  __| | __ _  ___ 
*  | |  | | | |   / /\ \ | |/ _` |/ _ \| '__| | __| '_ \| '_ ` _ \|  _ <| '__| |/ _` |/ _` |/ _ \
*  | |__| |_| |_ / ____ \| | (_| | (_) | |  | | |_| | | | | | | | | |_) | |  | | (_| | (_| |  __/
*   \____/|_____/_/    \_\_|\__, |\___/|_|  |_|\__|_| |_|_| |_| |_|____/|_|  |_|\__,_|\__, |\___|
*                            __/ |                                                     __/ |     
*                           |___/                                                     |___/    
*  
* The goal of this class is to provide a way for the UI and the music generation algorthm to communicate.
* This is done indirectly by having the UI and the Algorithm components communicate solely with this class.
* The UI does this by passing in all the options which have been selected by the user. At this point, the UI
* has finished it's role in the particular music generation. Programming wise, this is done by passing a
* selection object to the bridge, which is responsible for unpacking it, creating the necessary subcomponents
* from the generator algorithm, and making sure the correct calls are made. The bridge's responsibility ends
* when the request from the UI is fulfilled. 
*/

import java.util.ArrayList;
import org.jfugue.pattern.Pattern;

public class UIAlgorithmBridge {
   private UIRequest request;
   
   public UIAlgorithmBridge() { /* do nothing */  } 
   
   public boolean acceptRequest(UIRequest newAction) {
      // Check the type of the request
      request = newAction;
      if(request.GetRequestType() == UIEnums.RequestType.STOP) {
         // OurPlayer.stop()
      }
      if(request.GetRequestType() == UIEnums.RequestType.SAVE) {
         // OurPlayer.save()?
      }
      if(request.GetRequestType() == UIEnums.RequestType.GENERATE) {
         // Check the pattern type
         ArrayList<Pattern> patterns = new ArrayList<Pattern>();
         if(request.GetPattern() == UIEnums.PatternType.CONJUNCTMELODY) {
            // Use the melody.java ruleset.
            melody melodyGenerator = new melody();
            // Create patterns
            for(int i = 0; i < 5; i++)
               patterns.add(melodyGenerator.generatePattern());
         }
         else if(request.GetPattern() == UIEnums.PatternType.MELODYCHORDS) {
            // Use a specific emotional pattern
			// Check if "Joy" or "Saddness"
			
			// Sadness: Use the ChordMelodyPattern.java ruleset.
			if(request.GetEmotion() == UIEnums.Emotion.SADNESS) {
				ChordMelodyPattern patternGen = new ChordMelodyPattern();
            // Create patterns
				for(int i = 0; i < 5; i++)
					patterns.add(patternGen.generatePattern());
			}
			else /*if(request.GetEmotion() == UIEnums.Emotion.JOY)*/ {
				HappyPattern patternGen = new HappyPattern();
				// Create patterns
				for(int i = 0; i < 5; i++)
					patterns.add(patternGen.generatePattern());
			}
         }
         
         // Send created patterns to the pattern bank (is this necessary?)
         
         // Have the song accept the patterns
         Song thisSong = new Song();
         thisSong.AcceptPatterns(patterns);
		   //patterns.clear();
         
         // Send the completed song to the player
         OurPlayer thisPlayer = new OurPlayer();
         thisPlayer.playSong(thisSong);
      }
      return true;
   }
}