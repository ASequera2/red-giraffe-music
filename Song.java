/*
   _____                   
  / ____|                  
 | (___   ___  _ __   __ _ 
  \___ \ / _ \| '_ \ / _` |
  ____) | (_) | | | | (_| |
 |_____/ \___/|_| |_|\__, |
                      __/ |
                     |___/ 
A class which is able to store a song, it contains all the voices which make up the song,
and methods to transform that song (adding and removing voices), and transform the song
into jfugue notation so that it can be read and played by the player, as midi sound.

It is composed of multiple voices.

*/
import java.util.ArrayList;

public class Song {
   int tempo;
   ArrayList<Voice> voices;

   public Song() {
      voices = new ArrayList<Voice>();
      tempo = 120;
   }
   
   public boolean addVoice(Voice v) {
      voices.add(v);
      return true;
   }
   
   public boolean setTempo(int t) {
      tempo = t;
      return true;
   }
   
   public String toString() {
      String song = ""; //"T"+tempo+" ";
      for(int i = 0; i < voices.size(); i++) {
         song = song+" V"+i+" T"+tempo+" "+voices.get(i).toString();
      }
      return song;
   }
   
   public boolean addSong(Song other) {
      for(int i = 0; i < other.voices.size(); i++) {
         if(i < voices.size())
            voices.set(i, voices.get(i).concatVoices(other.voices.get(i)));
         else
            voices.add(other.voices.get(i));
      }
      return true;
   }
}