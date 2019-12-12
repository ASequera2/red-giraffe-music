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


public class UIAlgorithmBridge {
   // Storage of the allowed notes for various keys, used by the pattern rule sets.
   private String[] CNotes = {"C","D","E","F","G","A","B"};
   private String[] CAllowedNotes = {"G4","A5","B5","C5","D5","E5","F5","G5","A6","B6","C6","D6","E6","F6","G6"};
   private String[] GNotes = {"G","A","B","C","D","E","F#"};
   private String[] GAllowedNotes = {"D5","E5","F#5","G5","A6","B6","C6","D6","E6","F#6","G6","A7","B7","C7","D7"};
   private String[] DNotes = {"D", "E", "F", "G", "A", "Bb", "C"};
   private String[] DAllowedNotes = {"D4","E4","F4","G4","A5","Bb5","C5","D5","E5","F5","G5","A6"};
   private String[] ANotes = {"A", "B", "C", "D", "E", "F", "G"};
   private String[] AAllowedNotes = {"A4","B4","C4","D4","E4","F4","G4","A5","B5","C5","D5","E5"};
   private String[] BNotes = {"B","C#","D#","E","F#","G#","A#"};
   private String[] BAllowedNotes = {"B4","C#4","D#4","E4","F#4","G#4","A#4","B5","C#5","D#5","E5","F#5","G#5","A#5","B6","C#6","D#6"};
   
   // Current request being processed.
   private UIRequest request;
   // Player used to play song objects.
   private OurPlayer thisPlayer = new OurPlayer();
   
   public UIAlgorithmBridge() { /* do nothing */  } 
   
   public boolean acceptRequest(UIRequest newAction) {
      // Check the type of the request
      request = newAction;
      if(request.GetRequestType() == UIEnums.RequestType.GENERATE)
         handleGenerate();
      else if(request.GetRequestType() == UIEnums.RequestType.PAUSE)
         handlePause();
      else if(request.GetRequestType() == UIEnums.RequestType.SAVE)
         handleSave();
      else if(request.GetRequestType() == UIEnums.RequestType.BROWSE)
         handleBrowse();  
      else if(request.GetRequestType() == UIEnums.RequestType.PLAYLOADED)
         handlePlayLoaded(); 
      else if(request.GetRequestType() == UIEnums.RequestType.UNPAUSE)
         handleUnpause();
         
      return true;   
   }
   
   // Private helper methods for each type of request
   private void handleGenerate() {
     // Create a multi song
     MultiSong bigSong = new MultiSong();
     // ArrayList<Song> songs = new ArrayList<Song>();
      // Generate a number of songs equal to the number of themes selected.
      for(int i = 0; i < request.GetNumThemes(); i++) {
         TempoDecorator t;
         Song singlePart = new Song();
         // Determine the lead instrument 
         UIEnums.Instrument instru;
         if(i < request.GetNumInstruments())
            instru = request.GetInstrument(i);
         else
            instru = UIEnums.Instrument.PIANO;
            
         String lead;
         if(instru == UIEnums.Instrument.PIANO) {
            lead = "Piano";
         }
         else if(instru == UIEnums.Instrument.BRASS) {
            lead = "Trumpet";
         }
         else if(instru == UIEnums.Instrument.SYNTH) {
            lead = "Square";
         }
         else if(instru == UIEnums.Instrument.GUITAR) {
            lead = "Guitar";
         }
         else /*if(instru == UIEnums.Instrument.STRINGS)*/ {
            lead = "Violin";
         }
         
         // Determine the key seed
         int keyDecider = (int)(Math.random()*2);
         
         
         // Check the pattern type
         UIEnums.Theme theme = request.GetTheme(i);
         if(theme == UIEnums.Theme.HAPPY) {
            t = new TempoDecorator((int)(Math.random()*100)+120);
            HappyPattern happy;
            if(keyDecider == 0) {
               happy = new HappyPattern("C", CNotes, CAllowedNotes, lead);
            }
            else {
               happy = new HappyPattern("G", GNotes, GAllowedNotes, lead);
            }
            happy.generatePattern(singlePart);
         }
         else if(theme == UIEnums.Theme.CALM) {
            t = new TempoDecorator((int)(Math.random()*10)+60);
            CalmPattern calm = new CalmPattern("B", BNotes, BAllowedNotes, lead);
            calm.generatePattern(singlePart);
         }
         else if(theme == UIEnums.Theme.SAD) {
            SadPattern sad;
            t = new TempoDecorator((int)(Math.random()*60)+50);
            if(keyDecider == 0) {
               sad = new SadPattern("D", DNotes, DAllowedNotes, lead);
            }
            else {
               sad = new SadPattern("A", ANotes, AAllowedNotes, lead);
            }
            sad.generatePattern(singlePart);
         }
         else if(theme == UIEnums.Theme.INTENSE) {
            t = new TempoDecorator((int)(Math.random()*50)+200);
            CalmPattern calm = new CalmPattern("B", BNotes, BAllowedNotes, lead);
            calm.generatePattern(singlePart);
         }
         else if(theme == UIEnums.Theme.SPOOKY) {
            t = new TempoDecorator((int)(Math.random()*80)+50);
            // ??
         }
         else /*oriental*/ {
            t = new TempoDecorator((int)(Math.random()*100)+60);
            // ??
         }
         // Add the tempo to the song
         t.generateSong(singlePart);
         // Add the section
         bigSong.addSection(singlePart);
         System.out.println(singlePart.toString());
      }
      // Play the songs
      thisPlayer.playMultiSong(bigSong);
      /*for(int i = 0; i < songs.size(); i++) {
         System.out.println("Section #"+(i+1));
         System.out.println(songs.get(i).toString());
         thisPlayer.playSong(songs.get(i));
      }*/
   }
   private void handlePause() {
      thisPlayer.pauseSong();
   }
   private void handleSave() {
      thisPlayer.saveSong("Song");
   }
   private void handleBrowse() {
      thisPlayer.setLoadedFile(request.getFile());
   }
   private void handlePlayLoaded() {
      thisPlayer.playLoadedFile();
   }
   private void handleUnpause() {
      thisPlayer.unpauseSong();
   }
}