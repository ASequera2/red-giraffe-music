/*
   ____             _____  _                       
  / __ \           |  __ \| |                      
 | |  | |_   _ _ __| |__) | | __ _ _   _  ___ _ __ 
 | |  | | | | | '__|  ___/| |/ _` | | | |/ _ \ '__|
 | |__| | |_| | |  | |    | | (_| | |_| |  __/ |   
  \____/ \__,_|_|  |_|    |_|\__,_|\__, |\___|_|   
                                    __/ |          
                                   |___/           
A file which is used to play, pause, and save songs. The playing, pausing, and unpausing of songs is done via
threading. This is because the regular jfugue player play methods do not return until after the pattern that is
being played as completed, thus making the use of the rest of the program impossible. In addition, the jfugue player
used here is a ManagedPlayer, not a Player, which is required because of the fact that the pause functionality is not
inherient in the regular player. 

The managed player is accessed through a seperate class, ThreadPlayer, which is included in this file due to
the fact that OurPlayer is the only class which instantiates or uses it.

*/

import java.io.File;
import java.io.IOException;


import org.jfugue.pattern.Pattern;
import org.jfugue.player.ManagedPlayer;

import javax.sound.midi.Sequence;
import javax.sound.midi.MidiSystem;

import org.jfugue.midi.MidiFileManager;


public class OurPlayer {

   // The current song that is being stored in memory from a genereate
   private Song currSong;
   
   // Player which does the playing, pausing, resuming, etc..
   private ThreadPlayer tp = new ThreadPlayer();
   
   // Loaded file from desktop
   private File loadedFile;
   
   // Constructor
   public OurPlayer() {
      currSong = new Song();
   }
   
   // Sets a loaded file for use by this player.
   public boolean setLoadedFile(File f) {
      loadedFile = f;
      System.out.println("Loaded file set.");
      return true;
   }
   
   
   // Play whatever the loaded file is.
   public boolean playLoadedFile() {
      
      Sequence savedSequence = null;
      try { 
         savedSequence = MidiSystem.getSequence(loadedFile);
      }
      catch(Exception e) {
         // If it failed to get the sequence, stacktrace and return
         System.err.println(e.getMessage());
         e.printStackTrace();
         return false;
      }
   
      tp.setSequence(savedSequence);
      tp.run();
      return true;
   }
   
   public void playMultiSong(MultiSong ms) {
      // Save multisong into a single sequence
      Pattern p = new Pattern(ms.toString());
      try {
         MidiFileManager.savePatternToMidi(p,new File("currSong"+".mid"));
      }
      catch (IOException e) {
         System.err.println(e.getMessage());
         e.printStackTrace();
      }
      // Play that sequence
      File savedFile = new File("currSong.mid");
      Sequence savedSequence = null;
      try { 
         savedSequence = MidiSystem.getSequence(savedFile);
      }
      catch(Exception e) {
         System.err.println(e.getMessage());
         e.printStackTrace();
      }
   
      tp.setSequence(savedSequence);
      tp.run();
   }

   // Sets a song to be the current song, and then plays the song.
   public void playSong(Song s) {
      
      currSong = s;
      saveSong("currSong");
      
      File savedFile = new File("currSong.mid");
      Sequence savedSequence = null;
      try { 
         savedSequence = MidiSystem.getSequence(savedFile);
      }
      catch(Exception e) {
         System.err.println(e.getMessage());
         e.printStackTrace();
      }
   
      tp.setSequence(savedSequence);
      tp.run();
   }
   
   // Pauses the currently playing song, if there is one.
   public void pauseSong() {
      tp.pauseSong();
   }
   
   // Unpauses the currently playing song, if there is one that is paused.
   public void unpauseSong() {
      tp.unpauseSong();
   }
   
   // Save the current song
   public void saveSong(String name) {
      Pattern p = new Pattern(currSong.toString());
      try {
         MidiFileManager.savePatternToMidi(p,new File(name+".mid"));
      }
      catch (IOException e) {
         System.err.println(e.getMessage());
         e.printStackTrace();
      }
   }
}

// ThreadPlayer needs to be the one that is actually playing or saving or whatever.
// When the threadplayer run is called, all sequences that have been added to the player
// are played.
// Has it's own player
class ThreadPlayer extends Thread {
   private ManagedPlayer jfplayer;
   private Sequence seq;
   
   public ThreadPlayer() {
      jfplayer = new ManagedPlayer();
      seq = null;
   }
   
   public void setSequence(Sequence s) {
      seq = s;
   }
   
   public void pauseSong() {
      if(jfplayer.isPlaying())
         jfplayer.pause();
   }
   
   public void unpauseSong() {
      if(jfplayer.isPaused())
         jfplayer.resume();
   }
   
   public void stopSong() {
      jfplayer.finish();
   }
   
   @Override
   public void run() {
      try { 
         jfplayer.start(seq);
      }
      catch(Exception e) {
         System.err.println(e.getMessage());
         e.printStackTrace();
      }
   }
}
